package com.sparta.WeatherWear.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stn", nullable = false)
    private int stn;

    @Column(name = "ta", nullable = false)
    private int ta;

    @Column(name = "sky", nullable = false)
    private String sky;

    @Column(name = "prep", nullable = false)
    private int prep;
}
