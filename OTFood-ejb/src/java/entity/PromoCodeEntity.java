/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import util.enumeration.PromoCodeTypeEnum;

/**
 *
 * @author Ong Bik Jeun
 */
@Entity
public class PromoCodeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promoCodeId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date endDate;
    @Column(unique = true, nullable = false)
    private String discountCode;
    @DecimalMin("0.00")
    private BigDecimal discountRate;
    @Enumerated(EnumType.STRING)
    private PromoCodeTypeEnum discountCodeTypeEnum;
    @Column(nullable = false)
    private Boolean isAvailable;
    @OneToMany(mappedBy = "promoCode", fetch=FetchType.EAGER)
    private List<SaleTransactionEntity> saleTransaction;

    public PromoCodeEntity() {
        saleTransaction = new ArrayList<>();
    }

    public PromoCodeEntity(Date startDate, Date endDate, String discountCode, BigDecimal discountRate, PromoCodeTypeEnum discountCodeTypeEnum) {
        this();
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountCode = discountCode;
        this.discountRate = discountRate;
        this.discountCodeTypeEnum = discountCodeTypeEnum;
        this.isAvailable = true;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public PromoCodeTypeEnum getDiscountCodeTypeEnum() {
        return discountCodeTypeEnum;
    }

    public void setDiscountCodeTypeEnum(PromoCodeTypeEnum discountCodeTypeEnum) {
        this.discountCodeTypeEnum = discountCodeTypeEnum;
    }

    public List<SaleTransactionEntity> getSaleTransaction() {
        return saleTransaction;
    }

    public void setSaleTransaction(List<SaleTransactionEntity> saleTransaction) {
        this.saleTransaction = saleTransaction;
    }

    public Long getPromoCodeId() {
        return promoCodeId;
    }

    public void setPromoCodeId(Long promoCodeId) {
        this.promoCodeId = promoCodeId;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (promoCodeId != null ? promoCodeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the promoCodeId fields are not set
        if (!(object instanceof PromoCodeEntity)) {
            return false;
        }
        PromoCodeEntity other = (PromoCodeEntity) object;
        if ((this.promoCodeId == null && other.promoCodeId != null) || (this.promoCodeId != null && !this.promoCodeId.equals(other.promoCodeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PromoCodeEntity[ id=" + promoCodeId + " ]";
    }

}
