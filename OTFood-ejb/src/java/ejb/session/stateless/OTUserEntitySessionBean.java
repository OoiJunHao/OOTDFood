/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AddressEntity;
import entity.BentoEntity;
import entity.MealEntity;
import entity.OTUserEntity;
import entity.StaffEntity;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateUserException;
import util.exception.UserExistException;
import util.exception.UserNotFoundException;
import util.security.CryptographicHelper;

/**
 *
 * @author Mitsuki
 */
@Stateless
public class OTUserEntitySessionBean implements OTUserEntitySessionBeanLocal {

    @PersistenceContext(unitName = "OTFood-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public OTUserEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewUser(OTUserEntity user) throws UserExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<OTUserEntity>> constraintViolations = validator.validate(user);
        if (constraintViolations.isEmpty()) {
            try {

                em.persist(user);
                em.flush();

                return user.getUserId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new UserExistException();
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
    public List<OTUserEntity> retrieveAllUser() {
        Query query = em.createQuery("SELECT u FROM OTUserEntity u");
        return query.getResultList();
    }

    @Override
    public OTUserEntity retrieveUserById(Long id) throws UserNotFoundException {
        OTUserEntity user = em.find(OTUserEntity.class, id);
        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException("User with ID: " + id + " does not exist!");
        }
    }

    @Override
    public OTUserEntity retrieveUserByEmail(String email) throws UserNotFoundException {
        Query query = em.createQuery("SELECT u FROM OTUserEntity u WHERE u.email = :email");
        query.setParameter("email", email);

        try {
            return (OTUserEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new UserNotFoundException("User email " + email + " does not exist!");
        }

    }

    @Override
    public OTUserEntity retrieveUserByName(String firstname, String lastname) throws UserNotFoundException {
        Query query = em.createQuery("SELECT u FROM OTUserEntity u WHERE u.firstname = :first AND u.lastName = :last");
        query.setParameter("first", firstname);
        query.setParameter("last", lastname);

        try {
            return (OTUserEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new UserNotFoundException("User does not exist!");
        }
    }

    @Override
    public void updateUserDetails(OTUserEntity user) throws UpdateUserException, UserNotFoundException, InputDataValidationException {

        Set<ConstraintViolation<OTUserEntity>> constraintViolations = validator.validate(user);
        if (user.getUserId() != null && user != null) {
            if (constraintViolations.isEmpty()) {

                try {
                    OTUserEntity userToUpdate = retrieveUserById(user.getUserId());

                    if (userToUpdate.getEmail().equals(user.getEmail())) {
                        userToUpdate.setFirstname(user.getFirstname());
                        userToUpdate.setLastName(user.getLastName());
                        userToUpdate.setContactNum(user.getContactNum());
                        userToUpdate.setDob(user.getDob());
                        userToUpdate.setProfilePic(user.getProfilePic());
                    } else {
                        throw new UpdateUserException("Email of user does not match with existing record");
                    }
                } catch (UserNotFoundException ex) {
                    throw new UserNotFoundException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public void updatePassword(OTUserEntity user, String oldPassword, String newPassword) throws InvalidLoginCredentialException, UserNotFoundException {
        if (user.getUserId() != null && user != null) {
            try {
                OTUserEntity userToUpdate = retrieveUserById(user.getUserId());
                String oldPasswordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(oldPassword + user.getSalt()));

                if (user.getPassword().equals(oldPasswordHash)) {
                    userToUpdate.setSalt(CryptographicHelper.getInstance().generateRandomString(32));
                    userToUpdate.setPassword(newPassword);
                } else {
                    throw new InvalidLoginCredentialException("Old password is incorrect");
                }
            } catch (UserNotFoundException ex) {
                throw new UserNotFoundException(ex.getMessage());
            }
        }
    }
    // needs to be updated when BentoSessionBean is developed
//    public void addFavouriteBento(Long userId, Long bentoId) {
//        if(userId != null) {
//            OTUserEntity user = retrieveUserById(userId);
//            //BentoEntity bento = retrieveBentoById(bentoId);
//
//            user.getMeals().add(bento);
//            bento.getUser().add(user);
//        }
//    }

//    public void removeFavouriteBento(Long userId, Long mealId) {
//        if(userId != null) {
//            OTUserEntity user = retrieveUserById(userId);
//            MealEntity meal = retrieveMealById(mealId);
//
//            user.getMeals().remove(meal);
//            meal.getUsers().remove(user);
//        }
//    }
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<OTUserEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
