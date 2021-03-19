/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelperClasses;

import entity.IngredientEntity;

/**
 *
 * @author benny
 */
public class IngredientCount {
    private IngredientEntity ingredient;
    private int count;
    
    public IngredientCount(IngredientEntity ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    public IngredientEntity getIngredient() {
        return ingredient;
    }

    public void setIngredient(IngredientEntity ingredient) {
        this.ingredient = ingredient;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
}
