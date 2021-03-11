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
import util.exception.UserExistException;
import util.exception.UserNotFoundException;

/**
 *
 * @author benny
 */
@Local
public interface AddressEntitySessionBeanLocal {

    public void removeAddressByUserId(Long userId, Long addressId) throws UserNotFoundException, NoAddressFoundException, DeleteAddressException;

    public void updateAddressByUserId(Long userId, AddressEntity newAddress) throws NoAddressFoundException, UserNotFoundException, UpdateAddressException;

    public AddressEntity retrieveAddressByUserId(long userId, Long addressId) throws UserNotFoundException, NoAddressFoundException;

    public List<AddressEntity> retrieveAddressesByUserId(Long userId) throws NoAddressFoundException, UserNotFoundException;

    public Long addAddressWithUserId(AddressEntity address, Long userId) throws UnknownPersistenceException, InputDataValidationException, UserNotFoundException, AddressExistException;
    
}
