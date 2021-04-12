/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DriverEntity;
import entity.SaleTransactionEntity;
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
import util.exception.DriverAlreadyFoundException;
import util.exception.DriverExistsException;
import util.exception.DriverNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.NoSaleTransactionFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateDriverException;
import util.security.CryptographicHelper;

/**
 *
 * @author yuntiangu
 */
@Stateless
public class DriverEntitySessionBean implements DriverEntitySessionBeanLocal {

    @EJB(name = "SaleTransactionEntitySessionBeanLocal")
    private SaleTransactionEntitySessionBeanLocal saleTransactionEntitySessionBeanLocal;

    @PersistenceContext(unitName = "OTFood-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public DriverEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    

    @Override
    public Long createNewDriver(DriverEntity driver) throws UnknownPersistenceException, InputDataValidationException, DriverExistsException {
        Set<ConstraintViolation<DriverEntity>> constraintViolations = validator.validate(driver);
        if (constraintViolations.isEmpty()) {
            try {

                em.persist(driver);
                em.flush();

                return driver.getDriverId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new DriverExistsException();
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
    public List<DriverEntity> retrieveAllDrivers() {
        Query query = em.createQuery("SELECT d FROM DriverEntity d");
        return query.getResultList();
    }

    @Override
    public DriverEntity retrieveDriverById(Long driverId) throws DriverNotFoundException {
        DriverEntity driver = em.find(DriverEntity.class, driverId);
        if (driver != null) {
            return driver;
        } else {
            throw new DriverNotFoundException("Driver ID " + driverId + " does not exists!");
        }
    }

    @Override
    public DriverEntity retrieveDriverByUsername(String username) throws DriverNotFoundException {
        Query query = em.createQuery("SELECT d FROM DriverEntity d WHERE d.username = :username");
        query.setParameter("username", username);

        try {
            return (DriverEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new DriverNotFoundException("Driver Username" + username + " does not exist");
        }
    }

    @Override
    public List<DriverEntity> retrieveDriverByName(String driverName) {
        Query query = em.createQuery("SELECT d FROM DriverEntity d WHERE (d.firstname LIKE '%:driverName%') OR (d.lastName LIKE '%:driverName%')");
        query.setParameter("driverName", driverName);
        return query.getResultList();
    }

    @Override
    public void updateDriver(DriverEntity driver) throws UpdateDriverException, InputDataValidationException, DriverNotFoundException {
        if (driver != null & driver.getDriverId() != null) {
            System.out.println(driver);
            Set<ConstraintViolation<DriverEntity>> constraintViolations = validator.validate(driver);

            if (constraintViolations.isEmpty()) {

                DriverEntity driverToUpdate = retrieveDriverById(driver.getDriverId());

                if (driverToUpdate.getUsername().equals(driver.getUsername())) {
                    driverToUpdate.setFirstname(driver.getFirstname());
                    driverToUpdate.setLastName(driver.getLastName());
                    driverToUpdate.setProfilePicture(driver.getProfilePicture());
                    driverToUpdate.setAge(driver.getAge());
                    driverToUpdate.setActive(driver.isActive());
                } else {
                    throw new UpdateDriverException("Username of driver to be updated does not exist");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));

            }
        } else {
            throw new DriverNotFoundException("DriverID not provided");
        }
    }

    @Override
    public DriverEntity updateDriverIonic(DriverEntity driver) throws UpdateDriverException, InputDataValidationException, DriverNotFoundException {
        if (driver != null & driver.getDriverId() != null) {
            System.out.println(driver);
            Set<ConstraintViolation<DriverEntity>> constraintViolations = validator.validate(driver);

            if (constraintViolations.isEmpty()) {

                DriverEntity driverToUpdate = retrieveDriverById(driver.getDriverId());

                if (driverToUpdate.getUsername().equals(driver.getUsername())) {
                    driverToUpdate.setFirstname(driver.getFirstname());
                    driverToUpdate.setLastName(driver.getLastName());
                    driverToUpdate.setProfilePicture(driver.getProfilePicture());
                    driverToUpdate.setAge(driver.getAge());
                    driverToUpdate.setActive(driver.isActive());
                    return driverToUpdate;
                } else {
                    throw new UpdateDriverException("Username of driver to be updated does not exist");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));

            }
        } else {
            throw new DriverNotFoundException("DriverID not provided");
        }
    }

    @Override
    public DriverEntity driverLogin(String username, String password) throws InvalidLoginCredentialException {

        try {
            DriverEntity driver = retrieveDriverByUsername(username);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + driver.getSalt()));
            if (driver.getPassword().equals(passwordHash)) {
                return driver;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password");
            }
        } catch (DriverNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password");
        }
    }

    @Override
    public boolean setDriverActiveToFalse(Long driverId) throws DriverNotFoundException {
        DriverEntity driverToFire = em.find(DriverEntity.class,
                driverId);
        if (driverToFire != null) {
            driverToFire.setActive(false);
            em.flush();
            return driverToFire.isActive();
        }
        throw new DriverNotFoundException("Driver ID: " + driverId + " does not exist!");

    }
    
    @Override
    public SaleTransactionEntity retrieveOneSaleTransaction() {
        Query query = em.createQuery("SELECT st FROM SaleTransactionEntity st WHERE st.driver IS NULL ORDER BY st.deliveryDateTime ASC");
        List<SaleTransactionEntity> saleTransactions = query.getResultList();
        SaleTransactionEntity selected = saleTransactions.get(0); //getting the latest transaction with no driver
        return selected;      
    } 
    
    @Override
    public void setDriverToSaleTransaction(long driverId, long customerId, long saleTransactionId) throws DriverNotFoundException, NoSaleTransactionFoundException, DriverAlreadyFoundException {
        DriverEntity driver = retrieveDriverById(driverId);
        SaleTransactionEntity saleTransaction = saleTransactionEntitySessionBeanLocal.retrieveSaleTransactionByUserId(customerId, saleTransactionId);
        if (saleTransaction.getDriver() != null) {
            throw new DriverAlreadyFoundException("A driver is already assigned to this sale Transaction!");
        } else {
            saleTransaction.setDriver(driver);
            driver.getSaleTransaction().add(saleTransaction);
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<DriverEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
    
    

}
