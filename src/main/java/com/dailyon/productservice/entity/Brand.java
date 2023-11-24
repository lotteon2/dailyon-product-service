package com.dailyon.productservice.entity;

import com.dailyon.productservice.entity.common.BaseEntity;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Brand extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN default false")
    private boolean deleted;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    public static Brand createBrand(String name) {
        return Brand.builder().name(name).build();
    }

    public void updateName(String name) {
        this.name = name;
    }
}
