/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DriverEntity;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.DriverExistsException;
import util.exception.DriverNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateDriverException;

/**
 *
 * @author yuntiangu
 */
@Stateless
public class DriverEntitySessionBean implements DriverEntitySessionBeanLocal {

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

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<DriverEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
