/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.security.CryptographicHelper;

/**
 *
 * @author Ong Bik Jeun
 */
@Entity
public class OTUserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserId;
    @Column(nullable = false, unique = true, length = 64)
    @NotNull
    @Size(max = 64)
    @Email
    private String email; //email is used as username
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    @NotNull
    private String password;
    @Column(nullable = false)
    @NotNull
    @Min(8)
    private Long contactNum;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String firstname;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String lastName;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dob;
    @Column(nullable = true)
    private String profilePic;
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    private String salt;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<CreditCardEntity> creditCard;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<AddressEntity> address;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<ReviewEntity> reviews;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<SaleTransactionEntity> saleTransaction;

    public OTUserEntity() {
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
        creditCard = new ArrayList<>();
        address = new ArrayList<>();
        reviews = new ArrayList<>();
        saleTransaction = new ArrayList<>();
        this.profilePic = "";
    }

    public OTUserEntity(String email, String password, Long contactNum, String firstname, String lastName, Date dob, String profilePic) {
        this();
        this.email = email;
        this.password = password;
        this.contactNum = contactNum;
        this.firstname = firstname;
        this.lastName = lastName;
        this.dob = dob;
        this.profilePic = profilePic;

        setPassword(password);
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long UserId) {
        this.UserId = UserId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password != null) {
            this.password = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + this.salt));
        } else {
            this.password = null;
        }
    }

    public Long getContactNum() {
        return contactNum;
    }

    public void setContactNum(Long contactNum) {
        this.contactNum = contactNum;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<CreditCardEntity> getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(List<CreditCardEntity> creditCard) {
        this.creditCard = creditCard;
    }

    public List<AddressEntity> getAddress() {
        return address;
    }

    public void setAddress(List<AddressEntity> address) {
        this.address = address;
    }

    public List<ReviewEntity> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewEntity> reviews) {
        this.reviews = reviews;
    }

    public List<SaleTransactionEntity> getSaleTransaction() {
        return saleTransaction;
    }

    public void setSaleTransaction(List<SaleTransactionEntity> saleTransaction) {
        this.saleTransaction = saleTransaction;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (UserId != null ? UserId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the UserId fields are not set
        if (!(object instanceof OTUserEntity)) {
            return false;
        }
        OTUserEntity other = (OTUserEntity) object;
        if ((this.UserId == null && other.UserId != null) || (this.UserId != null && !this.UserId.equals(other.UserId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.OTUserEntity[ id=" + UserId + " ]";
    }

}
