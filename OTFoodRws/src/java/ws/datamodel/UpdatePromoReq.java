/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.MealEntity;
import entity.PromoCodeEntity;

/**
 *
 * @author benny
 */
public class UpdatePromoReq {
    private String username;
    private String password;
    private PromoCodeEntity promoEntity;

    public UpdatePromoReq(String username, String password, PromoCodeEntity codeEntity) {
        this.username = username;
        this.password = password;
        this.promoEntity = codeEntity;
    }

    public UpdatePromoReq() {
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

    public PromoCodeEntity getPromoEntity() {
        return promoEntity;
    }

    public void setPromoEntity(PromoCodeEntity promoEntity) {
        this.promoEntity = promoEntity;
    }

    
}
