/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.CategoryEnum;

/**
 *
 * @author Ong Bik Jeun
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class MealEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long mealId;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal price;
    @Lob
    @Column(nullable = false)
    private String description;
    @NotNull
    @Min(0)
    private Integer calorie;
    @NotNull
    @Column(nullable = false, precision = 11, scale = 2)
    @DecimalMin("0.00")
    @DecimalMax("5.00")
    private BigDecimal averageRating;
    @Lob
    @Column(nullable = false)
    @Size(min = 0)
    @NotNull
    private String name;
    @Column(nullable = false, length = 24)
    @Size(min = 0)
    @NotNull
    private String image;
    @Column(nullable = false)
    @NotNull
    private boolean isAvailable;
 
    @Column(nullable = false)
    @NotNull
    List<CategoryEnum> categories;
    
    
    @OneToMany(mappedBy = "meal", fetch = FetchType.EAGER)
    private List<ReviewEntity> reviews;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<IngredientEntity> ingredients;

    public MealEntity() {
        categories = new ArrayList<>();
        reviews = new ArrayList<>();
        ingredients = new ArrayList<>();
        isAvailable = true;
    }

    public MealEntity(String name, BigDecimal price, String description, Integer calorie, String image, List<CategoryEnum> inputCategories) {
        this();
        this.price = price;
        this.description = description;
        this.calorie = calorie;
        this.averageRating = BigDecimal.valueOf(0);
        this.name = name;
        this.image = image;
        this.categories = inputCategories;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<IngredientEntity> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientEntity> ingredients) {
        this.ingredients = ingredients;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCalorie() {
        return calorie;
    }

    public void setCalorie(Integer calorie) {
        this.calorie = calorie;
    }

    public List<CategoryEnum> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEnum> categories) {
        this.categories = categories;
    }

    public List<ReviewEntity> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewEntity> reviews) {
        this.reviews = reviews;
    }

    public Long getMealId() {
        return mealId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mealId != null ? mealId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the mealId fields are not set
        if (!(object instanceof MealEntity)) {
            return false;
        }
        MealEntity other = (MealEntity) object;
        if ((this.mealId == null && other.mealId != null) || (this.mealId != null && !this.mealId.equals(other.mealId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.MealEntity[ id=" + mealId + " ]";
    }

    public Integer calculateAverageRating() {
        Integer totalRatings = 0;
        for (int i = 0; i < this.reviews.size(); i++) {
            totalRatings += this.reviews.get(i).getRating();
        }
        if (this.reviews.isEmpty()) { // just a placeholder for when there are no ratings yet
            return (int) totalRatings;
        } else {
            return (int) (totalRatings / this.reviews.size());
        }
        
    }

    public boolean isIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    
    

}
