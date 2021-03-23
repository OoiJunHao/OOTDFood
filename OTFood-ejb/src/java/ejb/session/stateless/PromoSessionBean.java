/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PromoCodeEntity;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.PromoCodeExistException;
import util.exception.PromotionNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Mitsuki
 */
@Stateless
public class PromoSessionBean implements PromoSessionBeanLocal {

    @EJB
    private SaleTransactionEntitySessionBeanLocal saleTransactionEntitySessionBean;

    @PersistenceContext(unitName = "OTFood-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public PromoSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public PromoCodeEntity createNewPromoCode(PromoCodeEntity promo) throws UnknownPersistenceException, PromoCodeExistException, InputDataValidationException {
        try {
            Set<ConstraintViolation<PromoCodeEntity>> constraintViolations = validator.validate(promo);

            if (constraintViolations.isEmpty()) {
                em.persist(promo);
                em.flush();

                return promo;
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new PromoCodeExistException();
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public List<PromoCodeEntity> retreieveAllPromoCode() {
        Query query = em.createQuery("SELECT p FROM PromoCodeEntity p");
        return query.getResultList();
    }
    
    @Override
    public PromoCodeEntity retrieveCodeById(Long promoCodeId) throws PromotionNotFoundException {       
        PromoCodeEntity promoCode = em.find(PromoCodeEntity.class, promoCodeId);
        if (promoCode != null) {
            return promoCode;
        } else {
            throw new PromotionNotFoundException("Promotion Code ID " + promoCodeId + " does not exist!");
        }
    }

    @Override
    public PromoCodeEntity retrieveCodeByDiscountCode(String code) throws PromotionNotFoundException {
        Query query = em.createQuery("SELECT q FROM PromoCodeEntity q WHERE q.discountCode = :code");
        query.setParameter("code", code);

        try {
            return (PromoCodeEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new PromotionNotFoundException("Promotion Code " + code + " does not exist!");
        }

    }

    @Override
    public Boolean checkPromoCode(String code) {
        Boolean valid = false;
        try {

            PromoCodeEntity promo = retrieveCodeByDiscountCode(code);

            Date date = new Date();

            if (promo.getStartDate().compareTo(date) < 0 && promo.getEndDate().compareTo(date) > 0) {
                if (promo.getSaleTransaction().size() < promo.getNumAvailable()) {
                    valid = true;
                }
            }
        } catch (PromotionNotFoundException ex) {
            Logger.getLogger(PromoSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valid;
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<PromoCodeEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
