package com.cbfacademy.apiassessment.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Category")
public class Category extends BaseEntity implements Serializable {

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Subcategory> subcategories;

    public Category(String name) {
        this.name = name;
    }
}
