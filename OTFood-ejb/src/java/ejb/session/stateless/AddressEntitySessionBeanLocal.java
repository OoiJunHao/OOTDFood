/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AddressEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.AddressExistException;
import util.exception.DeleteAddressException;
import util.exception.InputDataValidationException;
import util.exception.NoAddressFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateAddressException;
import util.exception.UserNotFoundException;

/**
 *
 * @author benny
 */
@Local
public interface AddressEntitySessionBeanLocal {

    public void removeAddress(long addressId) throws NoAddressFoundException;

    public void updateAddressByUserId(Long userId, AddressEntity newAddress) throws NoAddressFoundException, UserNotFoundException, UpdateAddressException;

    public AddressEntity retrieveAddressByUserId(long userId, Long addressId) throws UserNotFoundException, NoAddressFoundException;

    public List<AddressEntity> retrieveAddressesByUserId(Long userId);

    public Long addAddressWithUserId(AddressEntity address, Long userId) throws UnknownPersistenceException, InputDataValidationException, UserNotFoundException, AddressExistException;

    public AddressEntity retrieveAddressById(Long addressId) throws NoAddressFoundException;
    
}
