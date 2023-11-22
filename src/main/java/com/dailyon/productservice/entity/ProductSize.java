package com.dailyon.productservice.entity;

import com.dailyon.productservice.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSize extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN default false")
    private boolean deleted;

    public static ProductSize create(Category category, String name) {
        return ProductSize.builder()
                .category(category)
                .name(name)
                .build();
    }
}
