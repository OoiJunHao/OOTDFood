/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Ong Bik Jeun
 */
@Entity
public class CreditCardEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creditCardId;
    @Column(nullable = false, length = 10)
    @NotNull
    @Size(max = 10)
    private String type;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String cardName;
    @Column(nullable = false, length = 19)
    @NotNull
    @Size(max = 19)
    private String cardNumber;
    @Column(nullable = false, length = 5)
    @NotNull
    @Size(max = 5)
    private String expiryDate;
    @Column
    @NotNull
    private Boolean isRemoved;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private OTUserEntity user;

    public CreditCardEntity() {
    }

    public CreditCardEntity(String type, String cardName, String cardNumber, String expiryDate) {
        this.type = type;
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.isRemoved = false;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public OTUserEntity getUser() {
        return user;
    }

    public void setUser(OTUserEntity user) {
        this.user = user;
    }

    public Long getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(Long creditCardId) {
        this.creditCardId = creditCardId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creditCardId != null ? creditCardId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the creditCardId fields are not set
        if (!(object instanceof CreditCardEntity)) {
            return false;
        }
        CreditCardEntity other = (CreditCardEntity) object;
        if ((this.creditCardId == null && other.creditCardId != null) || (this.creditCardId != null && !this.creditCardId.equals(other.creditCardId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CreditCardEntity[ id=" + creditCardId + " ]";
    }

    /**
     * @return the isRemoved
     */
    public Boolean getIsRemoved() {
        return isRemoved;
    }

    /**
     * @param isRemoved the isRemoved to set
     */
    public void setIsRemoved(Boolean isRemoved) {
        this.isRemoved = isRemoved;
    }

}
