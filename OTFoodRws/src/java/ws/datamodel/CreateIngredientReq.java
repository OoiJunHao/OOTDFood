/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.IngredientEntity;

/**
 *
 * @author Ong Bik Jeun
 */
public class CreateIngredientReq {

    private String username;
    private String password;
    private IngredientEntity ingredient;

    public CreateIngredientReq() {
    }

    public CreateIngredientReq(String username, String password, IngredientEntity ingredient) {
        this.username = username;
        this.password = password;
        this.ingredient = ingredient;
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

    public IngredientEntity getIngredient() {
        return ingredient;
    }

    public void setIngredient(IngredientEntity ingredient) {
        this.ingredient = ingredient;
    }

}
