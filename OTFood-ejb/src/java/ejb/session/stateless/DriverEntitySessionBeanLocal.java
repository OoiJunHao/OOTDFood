/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DriverEntity;
import entity.SaleTransactionEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.DriverAlreadyFoundException;
import util.exception.DriverExistsException;
import util.exception.DriverNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.NoSaleTransactionFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.NoSaleTransactionException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateDriverException;

/**
 *
 * @author yuntiangu
 */
@Local
public interface DriverEntitySessionBeanLocal {

    public List<DriverEntity> retrieveAllDrivers();

    public DriverEntity retrieveDriverById(Long driverId) throws DriverNotFoundException;

    public List<DriverEntity> retrieveDriverByName(String driverName);

    public DriverEntity createNewDriver(DriverEntity driver) throws UnknownPersistenceException, InputDataValidationException, DriverExistsException;

    public boolean setDriverActiveToFalse(Long driverId) throws DriverNotFoundException;

    public void updateDriver(DriverEntity driver) throws UpdateDriverException, InputDataValidationException, DriverNotFoundException;

    public SaleTransactionEntity retrieveOneSaleTransaction() throws NoSaleTransactionException ;

    public DriverEntity setDriverToSaleTransaction(long driverId, long saleTransactionId, long customerId) throws DriverNotFoundException, NoSaleTransactionFoundException, DriverAlreadyFoundException;

    public DriverEntity retrieveDriverByUsername(String username) throws DriverNotFoundException;

    public DriverEntity driverLogin(String username, String password) throws InvalidLoginCredentialException;

    public DriverEntity updateDriverIonic(DriverEntity driver) throws UpdateDriverException, InputDataValidationException, DriverNotFoundException;

    public DriverEntity completeDelivery(long driverId, long saleTransactionId) throws DriverNotFoundException, NoSaleTransactionFoundException;

    public DriverEntity cashOutEarnings(long driverId) throws DriverNotFoundException;


}
