package com.cbfacademy.apiassessment.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class BudgetDTO {
    private Date created_at;
    private Long id;
    private double amount;
    private String category;
    private String subcategory;
    private String description;
    private Long user_id;
    private Date updated_at;

    public BudgetDTO(Long id, double amount, String category, String subcategory, String description, Long user_id){
        this.amount = amount;
        this.id = id;
        this.category = category;
        this.subcategory = subcategory;
        this.description = description;
        this.user_id = user_id;
    }
}
