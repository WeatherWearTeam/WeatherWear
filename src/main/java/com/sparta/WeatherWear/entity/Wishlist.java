package com.sparta.WeatherWear.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
/*
작성자 : 이승현
위시리스트 Entity
 */
@Getter
@Entity
@NoArgsConstructor // 기본 생성자 추가
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private NaverProduct product;

    public Wishlist(User user, NaverProduct product){
        this.user = user;
        this.product = product;
    }
}
