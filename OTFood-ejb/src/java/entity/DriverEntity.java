/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.security.CryptographicHelper;

/**
 *
 * @author Ong Bik Jeun
 */
@Entity
public class DriverEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long driverId;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String firstname;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String lastName;
    @Column(nullable = false)
    @NotNull
    @Min(21)
    private Integer age;
    @Column(nullable = false, length = 24)
    @NotNull
    @Size(max = 24)
    private String username;
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    @NotNull
    private String password;
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    private String salt;
    @Column(nullable = true)
    private String profilePicture;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2)
    private BigDecimal monthlyEarnings;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2)
    private BigDecimal dailyEarnings;

    @OneToMany(mappedBy = "driver", fetch = FetchType.EAGER)
    private List<SaleTransactionEntity> saleTransaction;

    public DriverEntity() {
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
        saleTransaction = new ArrayList<>();
        this.profilePicture = "";
    }

    public DriverEntity(String firstname, String lastName, Integer age, String username, String password, String profilePicture) {
        this();
        this.firstname = firstname;
        this.lastName = lastName;
        this.age = age;
        this.username = username;
        this.password = password;
        this.profilePicture = profilePicture;

        setPassword(password);
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public BigDecimal getMonthlyEarnings() {
        return monthlyEarnings;
    }

    public void setMonthlyEarnings(BigDecimal monthlyEarnings) {
        this.monthlyEarnings = monthlyEarnings;
    }

    public BigDecimal getDailyEarnings() {
        return dailyEarnings;
    }

    public void setDailyEarnings(BigDecimal dailyEarnings) {
        this.dailyEarnings = dailyEarnings;
    }

    public List<SaleTransactionEntity> getSaleTransaction() {
        return saleTransaction;
    }

    public void setSaleTransaction(List<SaleTransactionEntity> saleTransaction) {
        this.saleTransaction = saleTransaction;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (driverId != null ? driverId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the driverId fields are not set
        if (!(object instanceof DriverEntity)) {
            return false;
        }
        DriverEntity other = (DriverEntity) object;
        if ((this.driverId == null && other.driverId != null) || (this.driverId != null && !this.driverId.equals(other.driverId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.DriverEntity[ id=" + driverId + " ]";
    }

}
