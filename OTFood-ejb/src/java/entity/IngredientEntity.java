/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.IngredientTypeEnum;

/**
 *
 * @author Ong Bik Jeun
 */
@Entity
public class IngredientEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientId;
    @Column(nullable = false, length = 24)
    @Size(min = 0)
    @NotNull
    private String name;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal price;
    @NotNull
    @Min(0)
    private Integer calorie;
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private IngredientTypeEnum type;

    public IngredientEntity() {
    }

    public IngredientEntity(String name, BigDecimal price, Integer calorie, IngredientTypeEnum type) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.price = price;
        this.calorie = calorie;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCalorie() {
        return calorie;
    }

    public void setCalorie(Integer calorie) {
        this.calorie = calorie;
    }

    public IngredientTypeEnum getType() {
        return type;
    }

    public void setType(IngredientTypeEnum type) {
        this.type = type;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ingredientId != null ? ingredientId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the ingredientId fields are not set
        if (!(object instanceof IngredientEntity)) {
            return false;
        }
        IngredientEntity other = (IngredientEntity) object;
        if ((this.ingredientId == null && other.ingredientId != null) || (this.ingredientId != null && !this.ingredientId.equals(other.ingredientId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.IngredientEntity[ id=" + ingredientId + " ]";
    }

}
