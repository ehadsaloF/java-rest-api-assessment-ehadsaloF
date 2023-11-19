package com.cbfacademy.apiassessment.Entity;
import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;

@Entity
@Data
public class Watchlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userWatchlistId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    private LocalDateTime addedAt;

    // Add additional fields as needed
}
