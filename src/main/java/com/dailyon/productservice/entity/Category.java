package com.dailyon.productservice.entity;

import com.dailyon.productservice.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_category_id")
    private Category masterCategory;

    @BatchSize(size = 10) // https://velog.io/@joonghyun/SpringBoot-JPA-JPA-Batch-Size%EC%97%90-%EB%8C%80%ED%95%9C-%EA%B3%A0%EC%B0%B0
    @OneToMany(mappedBy = "masterCategory", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Category> childrenCategories = new ArrayList<>();

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductSize> productSizes = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN default false")
    private boolean deleted;

    public static Category createCategory(Category masterCategory, String name) {
        return Category.builder()
                .name(name)
                .masterCategory(masterCategory)
                .build();
    }

    public static Category createRootCategory(String name) {
        return Category.builder()
                .name(name)
                .build();
    }

    public List<Category> readBreadCrumbs() {
        List<Category> result = new ArrayList<>();
        result.add(this);

        Category parent = this.getMasterCategory();
        while(parent != null) {
            result.add(0, parent);
            parent = parent.getMasterCategory();
        }
        return result;
    }
}
