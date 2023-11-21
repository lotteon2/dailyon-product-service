package com.dailyon.productservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "master_category_id")
    private Category masterCategory;

    @OneToMany(mappedBy = "masterCategory", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Category> childrenCategories = new ArrayList<>();

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
}
