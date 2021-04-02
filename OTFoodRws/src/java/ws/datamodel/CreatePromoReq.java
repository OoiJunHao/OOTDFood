/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.PromoCodeEntity;

/**
 *
 * @author benny
 */
public class CreatePromoReq {
    private String username;
    private String password;
    private PromoCodeEntity promoCodeEntity;
    
    public CreatePromoReq(String username, String password, PromoCodeEntity entity) {
        this.username = username;
        this.password = password;
        this.promoCodeEntity = entity;
    }

    public CreatePromoReq() {
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

    public PromoCodeEntity getPromoCodeEntity() {
        return promoCodeEntity;
    }

    public void setPromoCodeEntity(PromoCodeEntity promoCodeEntity) {
        this.promoCodeEntity = promoCodeEntity;
    }
    
    
}
