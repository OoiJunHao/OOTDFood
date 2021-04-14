/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCardEntity;
import entity.OTUserEntity;
import entity.SaleTransactionEntity;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
import util.exception.UserNotFoundException;

/**
 *
 * @author Ong Bik Jeun
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
    public CreditCardEntity createNewCreditCardForUser(CreditCardEntity card, Long userId) throws InputDataValidationException, UnknownPersistenceException, CreditCardExistException, UserNotFoundException {
        Set<ConstraintViolation<CreditCardEntity>> constraintViolations = validator.validate(card);
        if (constraintViolations.isEmpty()) {
            OTUserEntity user = oTUserEntitySessionBean.retrieveUserById(userId);
            String cardNumber = card.getCardNumber();
            Query query = em.createQuery("SELECT c FROM CreditCardEntity c WHERE c.cardNumber = :ccNum AND c.isRemoved = true");
            query.setParameter("ccNum", cardNumber);
            try {
                CreditCardEntity creditCard = (CreditCardEntity) query.getSingleResult();
                creditCard.setIsRemoved(false);
                creditCard.setCardName(card.getCardName());
                creditCard.setExpiryDate(card.getExpiryDate());
                creditCard.setType(card.getType());

                creditCard.getUser().getCreditCard().remove(creditCard); // can remove because creditCardId has already been set
                creditCard.setUser(user);
                user.getCreditCard().add(creditCard);
                em.flush();
                return creditCard;
            } catch (NoResultException ex) {
                // will continue to below in this case
            }
            try {
                em.persist(card);
                user.getCreditCard().add(card);
                card.setUser(user);
                em.flush();
                return card;
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

        if (card != null && card.isIsRemoved() == false) {
            return card;
        } else {
            throw new CreditCardNotFoundException("Credit Card ID " + id + " does not exist!");
        }
    }

    @Override
    public List<CreditCardEntity> retrieveAllCardByUser(Long userId) {
        Query query = em.createQuery("SELECT c FROM CreditCardEntity c WHERE c.user.UserId = :id AND c.isRemoved = false");
        query.setParameter("id", userId);

        return query.getResultList();
    }

    @Override
    public CreditCardEntity retrieveCardByCardNumber(String cardNumber) {
        Query query = em.createQuery("SELECT c FROM CreditCardEntity c WHERE c.cardNumber = :num AND c.isRemoved = false");
        query.setParameter("num", cardNumber);

        return (CreditCardEntity) query.getSingleResult();
    }

    @Override
    public void deleteCreditCard(Long id) throws CreditCardNotFoundException {
        try {
            CreditCardEntity card = retrieveCardById(id);

            boolean toRemove = true;
            List<SaleTransactionEntity> saleTransactions = card.getUser().getSaleTransaction();
            for (SaleTransactionEntity st : saleTransactions) {
                if (Objects.equals(st.getCreditCardEntity().getCreditCardId(), id)) {
                    toRemove = false;
                    break;
                }
            }
            if (toRemove) {
                OTUserEntity user = card.getUser();
                user.getCreditCard().remove(card);
                em.remove(card);
            } else {
                card.setIsRemoved(true);
            }              
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
