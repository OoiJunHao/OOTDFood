/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.BentoEntity;

/**
 *
 * @author benny
 */
public class CreateMealReq {
    private String username;
    private String password;
    private BentoEntity bentoEntity;

    public CreateMealReq(String username, String password, BentoEntity bentoEntity) {
        this.username = username;
        this.password = password;
        this.bentoEntity = bentoEntity;
    }

    public CreateMealReq() {
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

    public BentoEntity getBentoEntity() {
        return bentoEntity;
    }

    public void setBentoEntity(BentoEntity bentoEntity) {
        this.bentoEntity = bentoEntity;
    }

}
