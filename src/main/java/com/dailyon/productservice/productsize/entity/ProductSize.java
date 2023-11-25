package com.dailyon.productservice.productsize.entity;

import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "category_id"}))
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSize extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN default false")
    private boolean deleted;

    public static ProductSize create(Category category, String name) {
        return ProductSize.builder()
                .category(category)
                .name(name)
                .build();
    }

    public void updateName(String name) {
        this.name = name;
    }
}
