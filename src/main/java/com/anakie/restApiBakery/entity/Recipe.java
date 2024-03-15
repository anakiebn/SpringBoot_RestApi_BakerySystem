package com.anakie.restApiBakery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Using @ElementCollection to represent the map of ingredients and their quantities
    @ElementCollection
    @CollectionTable(name = "recipe_ingredients_mapping", joinColumns = @JoinColumn(name = "recipe_id"))
    @MapKeyJoinColumn(name = "ingredient_id") // Specifies the foreign key column for the map key
    @Column(name = "quantity") // Specifies the column for the quantity of the ingredient
    private Map<Ingredient, Double> recipeIngredients = new HashMap<>();

    @JsonIgnore
    @OneToOne(mappedBy = "recipe")
    private Product product;

    public Recipe(String name, Map<Ingredient, Double> recipeIngredients) {
        this.name=name;
        this.recipeIngredients=recipeIngredients;
    }

    public Map<Long,Double> toRecipeIngrId(){
        return recipeIngredients.entrySet().stream()
                .collect(
                        Collectors.groupingBy(
                                e1->e1.getKey().getId(),(HashMap::new),(Collectors.summingDouble(Map.Entry::getValue)))
                );
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Recipe recipe = (Recipe) o;
        return getId() != null && Objects.equals(getId(), recipe.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
