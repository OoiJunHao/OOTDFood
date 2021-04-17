/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FaqEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.DriverExistsException;
import util.exception.FaqExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Ong Bik Jeun
 */
@Stateless
public class FaqSessionBean implements FaqSessionBeanLocal {

    @PersistenceContext(unitName = "OTFood-ejbPU")
    private EntityManager em;

    public FaqSessionBean() {
    }

    @Override
    public FaqEntity createNewFaq(FaqEntity faq) throws UnknownPersistenceException, InputDataValidationException, FaqExistException {
        try {
            em.persist(faq);
            em.flush();

            return faq;
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new FaqExistException();
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }

    }

    @Override
    public List<FaqEntity> retrieveFaqByCategories(String cat) {
        Query query = em.createQuery("SELECT f FROM FaqEntity f WHERE f.category = :cat");
        query.setParameter("cat", cat);

        return query.getResultList();
    }

}
