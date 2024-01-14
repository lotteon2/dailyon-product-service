package com.dailyon.productservice.product.service;

import com.dailyon.productservice.brand.entity.Brand;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.common.exception.UniqueException;
import com.dailyon.productservice.common.feign.response.ReadOOTDProductListResponse;
import com.dailyon.productservice.describeimage.entity.DescribeImage;
import com.dailyon.productservice.product.cache.NewProductCacheRepository;
import com.dailyon.productservice.product.dto.UpdateProductDto;
import com.dailyon.productservice.product.dto.request.CreateProductRequest;
import com.dailyon.productservice.product.dto.request.ProductStockRequest;
import com.dailyon.productservice.product.dto.request.UpdateProductRequest;
import com.dailyon.productservice.product.dto.response.*;
import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.common.exception.NotExistsException;
import com.dailyon.productservice.brand.repository.BrandRepository;
import com.dailyon.productservice.category.repository.CategoryRepository;
import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.productsize.entity.ProductSize;
import com.dailyon.productservice.describeimage.repository.DescribeImageRepository;
import com.dailyon.productservice.product.repository.ProductRepository;
import com.dailyon.productservice.productsize.repository.ProductSizeRepository;
import com.dailyon.productservice.productstock.entity.ProductStock;
import com.dailyon.productservice.productstock.repository.ProductStockRepository;
import com.dailyon.productservice.reviewaggregate.repository.ReviewAggregateRepository;
import com.dailyon.productservice.reviewaggregate.entity.ReviewAggregate;
import com.dailyon.productservice.common.util.S3Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
    private final S3Util s3Util;

    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductSizeRepository productSizeRepository;

    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;
    private final DescribeImageRepository describeImageRepository;
    private final ReviewAggregateRepository reviewAggregateRepository;

    private final NewProductCacheRepository newProductCacheRepository;

    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest createProductRequest) {
        if(productRepository.findProductByCode(createProductRequest.getCode()).isPresent()) {
            throw new UniqueException(UniqueException.DUPLICATE_PRODUCT_CODE);
        }

        Brand brand = brandRepository.findById(createProductRequest.getBrandId())
                .orElseThrow(() -> new NotExistsException(NotExistsException.BRAND_NOT_FOUND));

        Category category = categoryRepository.findById(createProductRequest.getCategoryId())
                .orElseThrow(() -> new NotExistsException(NotExistsException.CATEGORY_NOT_FOUND));

        // {productSizeId, quantity}의 목록에서 productSizeId의 목록만 추출
        Set<Long> productSizeIds = createProductRequest.extractProductSizeIds();

        // productSizeId의 목록으로부터 product의 목록 조회(db는 id 기준으로 자동 오름차순 정렬) ... [1]
        List<ProductSize> productSizes = productSizeRepository.findProductSizesByIds(productSizeIds);
        if(productSizes.size() != productSizeIds.size()) {
            throw new NotExistsException(NotExistsException.PRODUCT_SIZE_NOT_FOUND);
        }
        // productSizeId 기준으로 오름차순 정렬 ... [2]
        Collections.sort(createProductRequest.getProductStocks());

        // 상품 이미지 경로(bucket/random_uuid.확장자) 생성
        String filePath = s3Util.createFilePath(createProductRequest.getImage());

        // 상품 설명 이미지(bucket/random_uuid.확장자) 목록 생성
        List<String> describeImgUrls = createProductRequest.getDescribeImages().stream()
                .map(s3Util::createFilePath)
                .collect(Collectors.toList());

        // create product
        Product product = productRepository.save(Product.create(
                brand,
                category,
                ProductType.validate(createProductRequest.getType()),
                Gender.validate(createProductRequest.getGender()),
                createProductRequest.getName(),
                createProductRequest.getCode(),
                filePath,
                createProductRequest.getPrice()
        ));

        try { // 신상품 캐싱
            newProductCacheRepository.putNewProductCache(product.getId(), product);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // create productStocks
        // [1], [2]를 병렬로 돌면서 상품의 치수당 재고를 생성
        List<ProductStock> productStocks = new ArrayList<>();
        for(int i=0; i<productSizes.size(); i++) {
            productStocks.add(ProductStock.create(
                    product, productSizes.get(i), createProductRequest.getProductStocks().get(i).getQuantity()
            ));
        }
        productStockRepository.saveAll(productStocks);

        // create describeImages
        List<DescribeImage> describeImages = describeImgUrls.stream()
                .map(descImgUrl -> DescribeImage.create(product, descImgUrl))
                .collect(Collectors.toList());
        describeImageRepository.saveAll(describeImages);

        // create reviewAggregate
        reviewAggregateRepository.save(ReviewAggregate.create(product, 0D, 0L));

        // presignedUrls for response dto
        String imgPresignedUrl = s3Util.getPreSignedUrl(filePath);
        Map<String, String> describeImgPresignedUrls =
                createDescribeImgPresignedUrls(createProductRequest.getDescribeImages(), describeImgUrls);

        return CreateProductResponse.create(product.getId(), imgPresignedUrl, describeImgPresignedUrls);
    }

    @Transactional
    public UpdateProductDto updateProduct(Long productId, UpdateProductRequest updateProductRequest) {
        if(!updateProductRequest.getDescribeImages().isEmpty()) {
            describeImageRepository.deleteByProductId(productId);
        }

        productStockRepository.deleteByProductId(productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotExistsException(NotExistsException.PRODUCT_NOT_FOUND));

        Brand newBrand = brandRepository.findById(updateProductRequest.getBrandId())
                .orElseThrow(() -> new NotExistsException(NotExistsException.BRAND_NOT_FOUND));

        Category newCategory = categoryRepository.findById(updateProductRequest.getCategoryId())
                .orElseThrow(() -> new NotExistsException(NotExistsException.CATEGORY_NOT_FOUND));

        // 다른 상품에 존재하는 코드라면 exception
        Optional<Product> productByCode = productRepository.findProductByCode(updateProductRequest.getCode());
        if(productByCode.isPresent() && !productByCode.get().getId().equals(product.getId())) {
            throw new UniqueException(UniqueException.DUPLICATE_PRODUCT_CODE);
        }

        // {productSizeId, quantity}의 목록에서 productSizeId의 목록만 추출
        Set<Long> productSizeIds = updateProductRequest.extractProductSizeIds();

        // productSizeId의 목록으로부터 product의 목록 조회(db는 id 기준으로 자동 오름차순 정렬)
        List<ProductSize> productSizes = productSizeRepository.findProductSizesByIds(productSizeIds);
        if(productSizes.size() != productSizeIds.size()) {
            throw new NotExistsException(NotExistsException.PRODUCT_SIZE_NOT_FOUND);
        }

        List<ProductStock> productStocks = productStockRepository.findProductStocksByProductOrderByProductSize(product);
        List<ProductStock> productStocksToNotify = new ArrayList<>();
        // TODO : REFACTOR for-for-if
        for(ProductStockRequest productStockRequest: updateProductRequest.getProductStocks()) {
            for(ProductStock productStock: productStocks) {
                if(productStock.getProductSize().getId().equals(productStockRequest.getProductSizeId())) {
                    if(productStock.getQuantity().equals(0L)) { // 기존 재고가 0개였다면
                        productStocksToNotify.add(productStock);
                    }
                }
            }
        }

        List<ProductStock> newProductStocks = new ArrayList<>();
        for(int i=0; i<productSizes.size(); i++) {
            newProductStocks.add(ProductStock.create(
                    product, productSizes.get(i), updateProductRequest.getProductStocks().get(i).getQuantity()
            ));
        }

        product.setBrand(newBrand);
        product.setCategory(newCategory);
        product.setPrice(updateProductRequest.getPrice());
        product.setName(updateProductRequest.getName());
        product.setGender(Gender.validate(updateProductRequest.getGender()));
        product.setCode(updateProductRequest.getCode());

        try { // 신상품 캐시 업데이트
            newProductCacheRepository.deleteNewProductCache(productId);
            newProductCacheRepository.putNewProductCache(productId, product);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        productStockRepository.saveAll(newProductStocks);

        // presignedUrls for response dto
        String imgPresignedUrl = null;
        if(!updateProductRequest.getImage().isEmpty()) {
            product.setImgUrl(s3Util.createFilePath(updateProductRequest.getImage()));
            imgPresignedUrl = s3Util.getPreSignedUrl(product.getImgUrl());
        }

        Map<String, String> describeImgPresignedUrls = null;
        if(!updateProductRequest.getDescribeImages().isEmpty()) {
            // 상품 설명 이미지(bucket/random_uuid.확장자) 목록 생성
            List<String> newDescribeImgUrls = updateProductRequest.getDescribeImages().values().stream()
                    .map(s3Util::createFilePath)
                    .collect(Collectors.toList());

            List<DescribeImage> newDescribeImages = newDescribeImgUrls.stream()
                    .map(descImgUrl -> DescribeImage.create(product, descImgUrl))
                    .collect(Collectors.toList());

            describeImageRepository.saveAll(newDescribeImages);
            describeImgPresignedUrls = createDescribeImgPresignedUrls(
                    (List<String>) updateProductRequest.getDescribeImages().values(), newDescribeImgUrls);
        }

        return UpdateProductDto.create(imgPresignedUrl, describeImgPresignedUrls, productStocksToNotify);
    }

    private Map<String, String> createDescribeImgPresignedUrls(List<String> describeImages, List<String> describeImgUrls) {
        Map<String, String> describeImgPresignedUrls = new HashMap<>();
        for(int i=0; i<describeImages.size(); i++) {
            describeImgPresignedUrls.put(
                    describeImages.get(i), s3Util.getPreSignedUrl(describeImgUrls.get(i))
            );
        }
        return describeImgPresignedUrls;
    }

    @Transactional
    public void deleteProductsByIds(List<Long> ids) {
        productRepository.findAllById(ids).forEach(Product::softDelete);
    }

    public ReadProductDetailResponse readProductDetail(Long productId) {
        Product product = productRepository.findProductDetailById(productId)
                .orElseThrow(() -> new NotExistsException(NotExistsException.PRODUCT_NOT_FOUND));
        return ReadProductDetailResponse.fromEntity(product);
    }

    public Slice<Product> readProductSlice(Long lastId, Long brandId, Long categoryId, Gender gender, ProductType type) {
        List<Category> childCategories = null;
        if(categoryId != null) {
            childCategories = categoryRepository.findAllChildCategories(categoryId);
        }
        return productRepository.findProductSlice(lastId, brandId, childCategories, gender, type);
    }

    public ReadProductPageResponse readProductPage(Long brandId, Long categoryId, ProductType type, Pageable pageable) {
        List<Category> childCategories = null;
        if(categoryId != null) {
            childCategories = categoryRepository.findAllChildCategories(categoryId);
        }
        return ReadProductPageResponse.fromEntity(productRepository.findProductPage(brandId, childCategories, type, pageable));
    }

    public Slice<Product> searchProductSlice(Long lastId, String query) {
        return productRepository.searchProducts(lastId, query);
    }

    public ReadOOTDSearchSliceResponse searchFromOOTD(Long lastId, String query) {
        return ReadOOTDSearchSliceResponse.fromEntity(productRepository.searchProductsFromOOTD(lastId, query));
    }

    public ReadOOTDProductListResponse readOOTDProductDetails(List<Long> id) {
        return ReadOOTDProductListResponse.fromEntity(productRepository.findOOTDProductDetails(id));
    }

    public ReadNewProductListResponse readNewProducts() {
        return new ReadNewProductListResponse(newProductCacheRepository.readNewProductCache());
    }
}
