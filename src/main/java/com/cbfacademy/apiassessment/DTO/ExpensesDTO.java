package com.cbfacademy.apiassessment.DTO;

import lombok.Data;


import java.util.Date;

@Data
public class ExpensesDTO {
    private Date created;
    private double amount;
    private String category;
    private String subcategory;
    private String description;
    private Long user_id;
    private Long budget_id;
    private Date updated;
}
