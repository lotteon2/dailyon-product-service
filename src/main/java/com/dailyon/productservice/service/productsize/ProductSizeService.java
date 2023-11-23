package com.dailyon.productservice.service.productsize;

import com.dailyon.productservice.dto.request.CreateProductSizeRequest;
import com.dailyon.productservice.dto.response.ReadProductSizeListResponse;
import com.dailyon.productservice.entity.Category;
import com.dailyon.productservice.entity.ProductSize;
import com.dailyon.productservice.exception.NotExistsException;
import com.dailyon.productservice.exception.UniqueException;
import com.dailyon.productservice.repository.category.CategoryRepository;
import com.dailyon.productservice.repository.productsize.ProductSizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductSizeService {
    private final ProductSizeRepository productSizeRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ProductSize createProductSize(CreateProductSizeRequest createProductSizeRequest) {
        Category category = categoryRepository
                .findById(createProductSizeRequest.getCategoryId())
                .orElseThrow(NotExistsException::new);

        if(productSizeRepository.isDuplicated(category, createProductSizeRequest.getName())) {
            throw new UniqueException();
        }

        return productSizeRepository.save(ProductSize.create(category, createProductSizeRequest.getName()));
    }

    public ReadProductSizeListResponse readProductSizeListByCategory(Long id) {
        return ReadProductSizeListResponse.fromEntity(productSizeRepository.readProductSizesByCategoryId(id));
    }
}
