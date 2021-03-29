/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.MealEntity;

/**
 *
 * @author benny
 */
public class UpdateMealReq {
    private String username;
    private String password;
    private MealEntity mealEntity;

    public UpdateMealReq(String username, String password, MealEntity mealEntity) {
        this.username = username;
        this.password = password;
        this.mealEntity = mealEntity;
    }

    public UpdateMealReq() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MealEntity getMealEntity() {
        return mealEntity;
    }

    public void setMealEntity(MealEntity mealEntity) {
        this.mealEntity = mealEntity;
    }
    
    
}
