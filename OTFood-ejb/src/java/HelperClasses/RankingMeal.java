/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelperClasses;

import entity.MealEntity;

/**
 *
 * @author benny
 */
public class RankingMeal {
    private MealEntity meal;
    private int rank;
    
    public RankingMeal(MealEntity meal, int rank) {
        this.meal = meal;
        this.rank = rank;
    }

    public MealEntity getMeal() {
        return meal;
    }

    public void setMeal(MealEntity meal) {
        this.meal = meal;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
    
    
    
    
}
