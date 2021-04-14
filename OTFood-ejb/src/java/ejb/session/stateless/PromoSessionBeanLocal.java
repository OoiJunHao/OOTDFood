/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PromoCodeEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.PromoCodeExistException;
import util.exception.PromotionNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Ong Bik Jeun
 */
@Local
public interface PromoSessionBeanLocal {

    public PromoCodeEntity createNewPromoCode(PromoCodeEntity promo) throws UnknownPersistenceException, PromoCodeExistException, InputDataValidationException;

    public List<PromoCodeEntity> retreieveAllPromoCode();

    public PromoCodeEntity retrieveCodeByDiscountCode(String code) throws PromotionNotFoundException;

    public Boolean checkPromoCode(String code);

    public PromoCodeEntity retrieveCodeById(Long promoCodeId) throws PromotionNotFoundException;

    public void updateCode(PromoCodeEntity promoCode) throws PromotionNotFoundException;

}
