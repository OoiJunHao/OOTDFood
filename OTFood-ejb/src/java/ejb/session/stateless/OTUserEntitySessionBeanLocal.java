/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OTUserEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateUserException;
import util.exception.UserExistException;
import util.exception.UserNotFoundException;

/**
 *
 * @author Mitsuki
 */
@Local
public interface OTUserEntitySessionBeanLocal {

    public Long createNewUser(OTUserEntity user) throws UserExistException, UnknownPersistenceException, InputDataValidationException;

    public List<OTUserEntity> retrieveAllUser();

    public OTUserEntity retrieveUserById(Long id) throws UserNotFoundException;

    public OTUserEntity retrieveUserByEmail(String email) throws UserNotFoundException;

    public OTUserEntity retrieveUserByName(String firstname, String lastname) throws UserNotFoundException;

    public void updateUserDetails(OTUserEntity user) throws UpdateUserException, UserNotFoundException, InputDataValidationException;

    public void updatePassword(OTUserEntity user, String oldPassword, String newPassword) throws InvalidLoginCredentialException, UserNotFoundException, InputDataValidationException;

    public OTUserEntity login(String email, String password) throws InvalidLoginCredentialException, UserNotFoundException;

}
