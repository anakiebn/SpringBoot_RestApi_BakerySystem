package com.anakie.restApiBakery.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class IngredientMinQty {
    @Id
    @Column(name = "id", nullable = false)
    private Long ingredientId;

    private double ingredientMinQty;


}
