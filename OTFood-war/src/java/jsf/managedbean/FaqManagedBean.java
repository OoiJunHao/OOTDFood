/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.FaqSessionBeanLocal;
import entity.FaqEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Ong Bik Jeun
 */
@Named(value = "faqManagedBean")
@RequestScoped
public class FaqManagedBean {

    @EJB
    private FaqSessionBeanLocal faqSessionBean;

    private List<FaqEntity> faqOrders;
    private List<FaqEntity> faqDelivery;
    private List<FaqEntity> faqProduct;

    /**
     * Creates a new instance of FaqManagedBean
     */
    public FaqManagedBean() {
    }

    @PostConstruct
    public void postConstrct() {
        faqOrders = faqSessionBean.retrieveFaqByCategories("Orders");
        faqDelivery = faqSessionBean.retrieveFaqByCategories("Delivery");
        faqProduct = faqSessionBean.retrieveFaqByCategories("Product");

    }

    public List<FaqEntity> getFaqOrder() {
        return faqOrders;
    }

    public void setFaqOrder(List<FaqEntity> faqOrder) {
        this.faqOrders = faqOrder;
    }

    public List<FaqEntity> getFaqDelivery() {
        return faqDelivery;
    }

    public void setFaqDelivery(List<FaqEntity> faqDelivery) {
        this.faqDelivery = faqDelivery;
    }

    public List<FaqEntity> getFaqProduct() {
        return faqProduct;
    }

    public void setFaqProduct(List<FaqEntity> faqProduct) {
        this.faqProduct = faqProduct;
    }

}
