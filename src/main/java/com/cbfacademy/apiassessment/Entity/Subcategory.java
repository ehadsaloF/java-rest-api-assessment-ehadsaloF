package com.cbfacademy.apiassessment.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Subcategory")
public class Subcategory extends BaseEntity implements Serializable {

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Subcategory(String name, Category category) {
        this.name = name;
        this.category = category;
    }
}

