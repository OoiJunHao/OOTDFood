/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FaqEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.FaqExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Mitsuki
 */
@Local
public interface FaqSessionBeanLocal {

    public FaqEntity createNewFaq(FaqEntity faq) throws UnknownPersistenceException, InputDataValidationException, FaqExistException;

    public List<FaqEntity> retrieveFaqByCategories(String cat);

}
