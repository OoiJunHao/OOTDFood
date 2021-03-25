/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCardEntity;
import entity.OTUserEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CardCreationException;
import util.exception.CreditCardExistException;
import util.exception.CreditCardNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UserExistException;
import util.exception.UserNotFoundException;

/**
 *
 * @author Mitsuki
 */
@Stateless
public class CreditCardEntitySessionBean implements CreditCardEntitySessionBeanLocal {

    @EJB
    private OTUserEntitySessionBeanLocal oTUserEntitySessionBean;

    @PersistenceContext(unitName = "OTFood-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CreditCardEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public CreditCardEntity createNewCreditCardForUser(CreditCardEntity card, Long userId) throws InputDataValidationException, UnknownPersistenceException, CreditCardExistException, CardCreationException, UserNotFoundException {
        Set<ConstraintViolation<CreditCardEntity>> constraintViolations = validator.validate(card);

        if (constraintViolations.isEmpty()) {
            
            //To check for already persisted card, setting isRemoved back to false
            String maybePersistedCardNumber = card.getCardNumber();
            Query query = em.createQuery("SELECT c FROM CreditCardEntity c WHERE c.cardNumber = :inCcNum");
            query.setParameter("inCcNum", maybePersistedCardNumber);
            Object maybePersistedCardEntity = query.getSingleResult();
            if( maybePersistedCardEntity != null) { //maybe-card Exists
                CreditCardEntity alreadyPersistedCard = (CreditCardEntity)maybePersistedCardEntity;
                alreadyPersistedCard.setIsRemoved(Boolean.FALSE);
            }
            
            try {
                if (userId != null) {
                    try {
                        OTUserEntity user = oTUserEntitySessionBean.retrieveUserById(userId);

                        em.persist(card);
                        user.getCreditCard().add(card);
                        card.setUser(user);

                        em.flush();
                        return card;
                    } catch (UserNotFoundException ex) {
                        throw new UserNotFoundException(ex.getMessage());
                    }
                } else {
                    throw new CardCreationException("User ID must be provided!");
                }
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new CreditCardExistException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public CreditCardEntity retrieveCardById(Long id) throws CreditCardNotFoundException {
        CreditCardEntity card = em.find(CreditCardEntity.class, id);

        if (card != null) {
            return card;
        } else {
            throw new CreditCardNotFoundException("Credit Card ID " + id + " does not exist!");
        }
    }

    @Override
    public List<CreditCardEntity> retrieveAllCardByUser(Long userId) {
        Query query = em.createQuery("SELECT c FROM CreditCardEntity c WHERE c.user.UserId = :id");
        query.setParameter("id", userId);
        
        List<CreditCardEntity> nonRemovedCards = new ArrayList<>();
        for (Object c : query.getResultList()) {
            CreditCardEntity card = (CreditCardEntity)c;
            if (!card.getIsRemoved()) {
                nonRemovedCards.add(card);
            }
        
        }
        return nonRemovedCards;
    }

    @Override
    public CreditCardEntity retrieveCardByCardNumber(String cardNumber) {
        Query query = em.createQuery("SELECT c FROM CreditCardEntity c WHERE c.cardNumber = :num");
        query.setParameter("num", cardNumber);

        return (CreditCardEntity) query.getSingleResult();
    }

    @Override
    public void deleteCreditCard(Long id) throws CreditCardNotFoundException {
        try {
            CreditCardEntity card = retrieveCardById(id);
            
            //Commmented out because we using pseudo remove
//            if (card.getUser() != null) {
//                OTUserEntity user = card.getUser();
//                user.getCreditCard().remove(card);
//            }
//            
            em.remove(card);
        } catch (CreditCardNotFoundException ex) {
            throw new CreditCardNotFoundException(ex.getMessage());
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CreditCardEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
