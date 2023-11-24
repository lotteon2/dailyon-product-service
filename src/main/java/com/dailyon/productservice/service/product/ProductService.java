package com.dailyon.productservice.service.product;

import com.dailyon.productservice.dto.request.CreateProductRequest;
import com.dailyon.productservice.dto.request.CreateProductStockRequest;
import com.dailyon.productservice.dto.response.CreateProductResponse;
import com.dailyon.productservice.entity.*;
import com.dailyon.productservice.enums.Gender;
import com.dailyon.productservice.enums.ProductType;
import com.dailyon.productservice.exception.NotExistsException;
import com.dailyon.productservice.repository.brand.BrandRepository;
import com.dailyon.productservice.repository.category.CategoryRepository;
import com.dailyon.productservice.repository.describeimage.DescribeImageRepository;
import com.dailyon.productservice.repository.product.ProductRepository;
import com.dailyon.productservice.repository.productsize.ProductSizeRepository;
import com.dailyon.productservice.repository.productstock.ProductStockRepository;
import com.dailyon.productservice.repository.reviewaggregate.ReviewAggregateRepository;
import com.dailyon.productservice.util.S3Util;
import lombok.RequiredArgsConstructor;
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
                .orElseThrow(() -> new NotExistsException("존재하지 않는 브랜드입니다"));

        Category category = categoryRepository.findById(createProductRequest.getCategoryId())
                .orElseThrow(() -> new NotExistsException("존재하지 않는 카테고리입니다"));

        // {productSizeId, quantity}의 목록에서 productSizeId의 목록만 추출
        Set<Long> productSizeIds = createProductRequest.getProductStocks()
                .stream()
                .map(CreateProductStockRequest::getProductSizeId)
                .collect(Collectors.toSet());

        // productSizeId의 목록으로부터 product의 목록 조회(db는 id 기준으로 자동 오름차순 정렬) ... [1]
        List<ProductSize> productSizes = productSizeRepository.readProductSizesByProductSizeIds(productSizeIds);
        if(productSizes.size() != productSizeIds.size()) {
            throw new NotExistsException("존재하지 않는 치수값입니다");
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
}
