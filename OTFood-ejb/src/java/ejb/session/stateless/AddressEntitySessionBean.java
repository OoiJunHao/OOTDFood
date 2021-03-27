/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AddressEntity;
import entity.OTUserEntity;
import entity.SaleTransactionEntity;
import java.util.List;
import java.util.Set;
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
import util.exception.AddressExistException;
import util.exception.InputDataValidationException;
import util.exception.NoAddressFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateAddressException;
import util.exception.UserNotFoundException;

/**
 *
 * @author benny
 */
@Stateless
public class AddressEntitySessionBean implements AddressEntitySessionBeanLocal {

    @EJB(name = "OTUserEntitySessionBeanLocal")
    private OTUserEntitySessionBeanLocal oTUserEntitySessionBeanLocal;

    @PersistenceContext(unitName = "OTFood-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public AddressEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long addAddressWithUserId(AddressEntity address, Long userId) throws UnknownPersistenceException, InputDataValidationException, UserNotFoundException, AddressExistException {
        Set<ConstraintViolation<AddressEntity>> constraintViolations = validator.validate(address);
        if (constraintViolations.isEmpty()) {
            try {
                OTUserEntity user = oTUserEntitySessionBeanLocal.retrieveUserById(userId);
                address.setUser(user);
                em.persist(address);
                em.flush();
                user.getAddress().add(address);
                return address.getAddressId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new AddressExistException("This address already Exists"); // this is technically impossible
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } catch (UserNotFoundException ex) {
                throw new UserNotFoundException(ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<AddressEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    @Override
    public List<AddressEntity> retrieveAddressesByUserId(Long userId) {
        Query query = em.createQuery("SELECT a FROM OTUserEntity user JOIN user.address a WHERE user.UserId = :userId AND a.isRemoved = false");
        query.setParameter("userId", userId);
        List<AddressEntity> addresses = query.getResultList();
        return addresses;
    }

    @Override
    public AddressEntity retrieveAddressByUserId(long userId, Long addressId) throws UserNotFoundException, NoAddressFoundException {
        try {
            OTUserEntity user = oTUserEntitySessionBeanLocal.retrieveUserById(userId);
            Query query = em.createQuery("SELECT a FROM OTUserEntity user JOIN user.address a WHERE user.UserId = :userId AND a.addressId = :addressId AND a.isRemoved = false");
            query.setParameter("userId", userId);
            query.setParameter("addressId", addressId);
            AddressEntity address = (AddressEntity) query.getSingleResult();
            return address;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new NoAddressFoundException("userId: " + userId + " has no addresses!");
        }
    }

    @Override
    public void removeAddress(long addressId) throws NoAddressFoundException {    
        AddressEntity addressToBeRemoved = retrieveAddressById(addressId);
        List<SaleTransactionEntity> saleTransactions = addressToBeRemoved.getUser().getSaleTransaction();
        boolean toRemove = true;
        for (SaleTransactionEntity st: saleTransactions) {
            if (st.getAddress().getAddressId() == addressId) {
                toRemove = false;
                break;
            }
        }
        if (toRemove) {
            addressToBeRemoved.getUser().getAddress().remove(addressToBeRemoved); // can work as addressId field has been set
            em.remove(addressToBeRemoved);
        } else {
            addressToBeRemoved.setIsRemoved(true);
        }
    }

    @Override
    public void updateAddressByUserId(Long userId, AddressEntity newAddress) throws NoAddressFoundException, UserNotFoundException, UpdateAddressException {
        OTUserEntity user = oTUserEntitySessionBeanLocal.retrieveUserById(userId);
        AddressEntity currentAddressEntity = retrieveAddressByUserId(userId, newAddress.getAddressId());
        if (currentAddressEntity != null && user != null) {
            currentAddressEntity.setAddress(newAddress.getAddress());
            currentAddressEntity.setPostalCode(newAddress.getPostalCode());
            currentAddressEntity.setRegion(newAddress.getRegion());
        } else {
            throw new UpdateAddressException("The address or user entered is null!");
        }
    }

    @Override
    public AddressEntity retrieveAddressById(Long addressId) throws NoAddressFoundException {
        AddressEntity address = em.find(AddressEntity.class, addressId);
        if (address != null && address.isIsRemoved() == false) {
            return address;
        } else {
            throw new NoAddressFoundException("No address found!");
        }
    }

}
