package com.example.geniegoods.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_GOODS_CATEGORY")
public class GoodsCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    private Long categoryId;

    @Column(name = "KOREAN_NAME")
    private String koreanName;

    @Column(name = "PRICE")
    private Integer price;  // int로 맞춤

    @OneToMany(mappedBy = "goodsCategoryEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<GoodsEntity> goodsList = new ArrayList<>();

}