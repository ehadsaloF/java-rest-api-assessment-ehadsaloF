package com.cbfacademy.apiassessment.Entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    private String symbol;
    private String name;
    private double lastPrice;
    private double changePercentage;
    private long volume;
    private long marketCap;
}
