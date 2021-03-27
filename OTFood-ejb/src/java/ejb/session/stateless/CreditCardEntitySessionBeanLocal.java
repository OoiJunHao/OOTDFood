/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCardEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CardCreationException;
import util.exception.CreditCardExistException;
import util.exception.CreditCardNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UserNotFoundException;

/**
 *
 * @author Mitsuki
 */
@Local
public interface CreditCardEntitySessionBeanLocal {

    public CreditCardEntity createNewCreditCardForUser(CreditCardEntity card, Long userId) throws InputDataValidationException, UnknownPersistenceException, CreditCardExistException, UserNotFoundException;

    public CreditCardEntity retrieveCardById(Long id) throws CreditCardNotFoundException;

    public List<CreditCardEntity> retrieveAllCardByUser(Long userId);

    public CreditCardEntity retrieveCardByCardNumber(String cardNumber);

    public void deleteCreditCard(Long id) throws CreditCardNotFoundException;

}
