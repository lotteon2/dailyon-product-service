package com.dailyon.productservice.product.service;

import com.dailyon.productservice.brand.entity.Brand;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.common.exception.UniqueException;
import com.dailyon.productservice.describeimage.entity.DescribeImage;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    private final static String IMG_BUCKET = "dailyon-static-dev";
    private final static String PRODUCT_IMG_BUCKET_PREFIX = "product-img";

    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest createProductRequest) {
        Brand brand = brandRepository.findById(createProductRequest.getBrandId())
                .orElseThrow(() -> new NotExistsException(NotExistsException.BRAND_NOT_FOUND));

        Category category = categoryRepository.findById(createProductRequest.getCategoryId())
                .orElseThrow(() -> new NotExistsException(NotExistsException.CATEGORY_NOT_FOUND));

        // {productSizeId, quantity}의 목록에서 productSizeId의 목록만 추출
        Set<Long> productSizeIds = createProductRequest.getProductStocks()
                .stream()
                .map(ProductStockRequest::getProductSizeId)
                .collect(Collectors.toSet());

        // productSizeId의 목록으로부터 product의 목록 조회(db는 id 기준으로 자동 오름차순 정렬) ... [1]
        List<ProductSize> productSizes = productSizeRepository.readProductSizesByProductSizeIds(productSizeIds);
        if(productSizes.size() != productSizeIds.size()) {
            throw new NotExistsException(NotExistsException.PRODUCT_SIZE_NOT_FOUND);
        }
        // productSizeId 기준으로 오름차순 정렬 ... [2]
        Collections.sort(createProductRequest.getProductStocks());

        // 상품 이미지 경로(bucket/random_uuid.확장자) 생성
        String imgUrl = String.format("/%s/%s.%s", PRODUCT_IMG_BUCKET_PREFIX, UUID.randomUUID(),
                createProductRequest.getImage().split("\\.")[1]);

        // 상품 설명 이미지(bucket/random_uuid.확장자) 목록 생성
        List<String> describeImgUrls = createProductRequest.getDescribeImages().stream()
                .map(descImg -> String.format("/%s/%s.%s", PRODUCT_IMG_BUCKET_PREFIX, UUID.randomUUID(), descImg.split("\\.")[1]))
                .collect(Collectors.toList());

        // create product
        Product product = productRepository.save(
                Product.create(
                        brand,
                        category,
                        ProductType.validate(createProductRequest.getType()),
                        Gender.validate(createProductRequest.getGender()),
                        createProductRequest.getName(),
                        createProductRequest.getCode(),
                        imgUrl,
                        createProductRequest.getPrice()
                )
        );

        // create productStocks
        // [1], [2]를 병렬로 돌면서 상품의 치수당 재고를 생성
        List<ProductStock> productStocks = new ArrayList<>();
        for(int i=0; i<productSizes.size(); i++) {
            productStocks.add(
                    ProductStock.create(
                            product,
                            productSizes.get(i),
                            createProductRequest.getProductStocks().get(i).getQuantity()
                    )
            );
        }
        productStockRepository.saveAll(productStocks);

        // create describeImages
        List<DescribeImage> describeImages = describeImgUrls.stream()
                .map(descImgUrl -> DescribeImage.create(product, descImgUrl))
                .collect(Collectors.toList());
        describeImageRepository.saveAll(describeImages);

        // create reviewAggregate
        reviewAggregateRepository.save(ReviewAggregate.create(product, 0F, 0L));

        // presignedUrls for response dto
        String imgPresignedUrl = s3Util.getPreSignedUrl(IMG_BUCKET, imgUrl);
        Map<String, String> describeImgPresignedUrls = new HashMap<>();
        for(int i=0; i<describeImgUrls.size(); i++) {
            describeImgPresignedUrls.put(
                    createProductRequest.getDescribeImages().get(i),
                    s3Util.getPreSignedUrl(IMG_BUCKET, describeImgUrls.get(i))
            );
        }

        return CreateProductResponse.create(imgPresignedUrl, describeImgPresignedUrls);
    }

    public ReadProductDetailResponse readProductDetail(Long productId) {
        Product product = productRepository.findProductDetailById(productId)
                .orElseThrow(() -> new NotExistsException(NotExistsException.PRODUCT_NOT_FOUND));
        return ReadProductDetailResponse.fromEntity(product);
    }

    @Transactional
    public UpdateProductResponse updateProduct(Long productId, UpdateProductRequest updateProductRequest) {
        Product product = productRepository.findByIdAndDeletedIsFalse(productId)
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
        Set<Long> productSizeIds = updateProductRequest.getProductStocks()
                .stream()
                .map(ProductStockRequest::getProductSizeId)
                .collect(Collectors.toSet());

        // productSizeId의 목록으로부터 product의 목록 조회(db는 id 기준으로 자동 오름차순 정렬)
        List<ProductSize> productSizes = productSizeRepository.readProductSizesByProductSizeIds(productSizeIds);
        if(productSizes.size() != productSizeIds.size()) {
            throw new NotExistsException(NotExistsException.PRODUCT_SIZE_NOT_FOUND);
        }

        List<ProductStock> productStocks = productStockRepository.findProductsByProduct(product);
        // TODO : REFACTOR for-for-if
        for(ProductStockRequest productStockRequest: updateProductRequest.getProductStocks()) {
            for(ProductStock productStock: productStocks) {
                if(productStock.getProductSize().getId().equals(productStockRequest.getProductSizeId())) {
                    if(productStock.getQuantity().equals(0L)) { // 기존 재고가 0개였다면
                        // TODO : 재입고 알림 발송
                    }
                }
            }
        }

        List<ProductStock> newProductStocks = new ArrayList<>();
        for(int i=0; i<productSizes.size(); i++) {
            newProductStocks.add(
                    ProductStock.create(
                            product,
                            productSizes.get(i),
                            updateProductRequest.getProductStocks().get(i).getQuantity()
                    )
            );
        }

        String newImgUrl = String.format("/%s/%s.%s", PRODUCT_IMG_BUCKET_PREFIX, UUID.randomUUID(),
                updateProductRequest.getImage().split("\\.")[1]);

        // 저장할 describe image list 생성
        List<String> describeImgUrls = updateProductRequest.getDescribeImages().stream()
                .map(descImg -> String.format("/%s/%s.%s", PRODUCT_IMG_BUCKET_PREFIX, UUID.randomUUID(), descImg.split("\\.")[1]))
                .collect(Collectors.toList());

        List<DescribeImage> newDescribeImages = describeImgUrls.stream()
                .map(descImgUrl -> DescribeImage.create(product, descImgUrl))
                .collect(Collectors.toList());

        product.setBrand(newBrand);
        product.setCategory(newCategory);
        product.setPrice(updateProductRequest.getPrice());
        product.setName(updateProductRequest.getName());
        product.setImgUrl(newImgUrl);
        product.setGender(Gender.validate(updateProductRequest.getGender()));
        product.setCode(updateProductRequest.getCode());

        productStockRepository.deleteByProduct(product);
        describeImageRepository.deleteByProduct(product);

        productStockRepository.saveAll(newProductStocks);
        describeImageRepository.saveAll(newDescribeImages);

        // presignedUrls for response dto
        String imgPresignedUrl = s3Util.getPreSignedUrl(IMG_BUCKET, newImgUrl);
        Map<String, String> describeImgPresignedUrls = new HashMap<>();
        for(int i=0; i<describeImgUrls.size(); i++) {
            describeImgPresignedUrls.put(
                    updateProductRequest.getDescribeImages().get(i),
                    s3Util.getPreSignedUrl(IMG_BUCKET, describeImgUrls.get(i))
            );
        }

        return UpdateProductResponse.create(imgPresignedUrl, describeImgPresignedUrls);
    }

    public ReadProductSliceResponse readProductSlice(Long brandId, Long categoryId, Gender gender, ProductType type, String query, Pageable pageable) {
        return ReadProductSliceResponse.fromEntity(productRepository.findProductSlice(brandId, categoryId, gender, type, query, pageable));
    }

    public ReadProductPageResponse readProductPage(Long brandId, Long categoryId, ProductType type, Pageable pageable) {
        return ReadProductPageResponse.fromEntity(productRepository.findProductPage(brandId, categoryId, type, pageable));
    }
}
