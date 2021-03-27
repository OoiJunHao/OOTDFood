/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

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
import util.exception.UpdateStaffException;
import util.security.CryptographicHelper;

/**
 *
 * @author Mitsuki
 */
@Stateless
public class StaffEntitySessionBean implements StaffEntitySessionBeanLocal {

    @PersistenceContext(unitName = "OTFood-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public StaffEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewStaff(StaffEntity newStaff) throws StaffUsernameExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<StaffEntity>> constraintViolations = validator.validate(newStaff);

        if (constraintViolations.isEmpty()) {
            try {

                em.persist(newStaff);
                em.flush();

                return newStaff.getStaffId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new StaffUsernameExistException();
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
    public List<StaffEntity> retrieveAllStaff() {
        Query query = em.createQuery("SELECT s FROM StaffEntity s");
        return query.getResultList();
    }

    @Override
    public StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException {
        Query query = em.createQuery("SELECT s FROM StaffEntity s WHERE s.username = :username");
        query.setParameter("username", username);

        try {
            return (StaffEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new StaffNotFoundException("Staff Username " + username + " does not exist!");
        }
    }

    @Override
    public StaffEntity retrieveStaffById(Long id) throws StaffNotFoundException {
        StaffEntity staff = em.find(StaffEntity.class, id);

        if (staff != null) {
            return staff;
        } else {
            throw new StaffNotFoundException("Staff ID " + id + " does not exist!");
        }
    }

    @Override
    public StaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException {
        System.out.println(">>>>>>>> Login <<<<<<<");
        try {
            StaffEntity staff = retrieveStaffByUsername(username);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + staff.getSalt()));

            if (staff.getPassword().equals(passwordHash)) {
                return staff;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password");
            }
        } catch (StaffNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password");
        }
    }

    @Override
    public void updateStaff(StaffEntity staff) throws StaffNotFoundException, UpdateStaffException, InputDataValidationException {
        if (staff != null && staff.getStaffId() != null) {
            Set<ConstraintViolation<StaffEntity>> constraintViolations = validator.validate(staff);

            if (constraintViolations.isEmpty()) {
                StaffEntity staffToUpdate = retrieveStaffById(staff.getStaffId());

                if (staffToUpdate.getUsername().equals(staff.getUsername())) {
                    staffToUpdate.setFirstname(staff.getFirstname());
                    staffToUpdate.setLastName(staff.getLastName());
                    staffToUpdate.setType(staff.getType());
                    staffToUpdate.setProfilePicture(staff.getProfilePicture());
                } else {
                    throw new UpdateStaffException("Username of staff to be updated does not match exiting records");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new StaffNotFoundException("StaffID not provided");
        }
    }

    @Override
    public void updatePassword(StaffEntity staff, String oldPassword, String newPassword) throws InvalidLoginCredentialException, StaffNotFoundException {
        if (staff.getStaffId() != null) {
            try {
                StaffEntity staffToUpdate = retrieveStaffById(staff.getStaffId());
                String oldPasswordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(oldPassword + staff.getSalt()));

                if (staff.getPassword().equals(oldPasswordHash)) {
                    staffToUpdate.setSalt(CryptographicHelper.getInstance().generateRandomString(32));
                    staffToUpdate.setPassword(newPassword);
                } else {
                    throw new InvalidLoginCredentialException("Old Password is incorrect!");
                }
            } catch (StaffNotFoundException ex) {
                throw new StaffNotFoundException("Staff ID not provided");
            }

        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<StaffEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
