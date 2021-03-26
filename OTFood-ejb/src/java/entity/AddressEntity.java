/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.RegionEnum;

/**
 *
 * @author Ong Bik Jeun
 */
@Entity
public class AddressEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private RegionEnum region;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String address;
    @Column(nullable = false, length = 6)
    @NotNull
    @Size(min = 6, max = 6)
    private String postalCode;
    @NotNull
    @Column(nullable = false)
    private boolean isRemoved;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private OTUserEntity user;

    public AddressEntity() {
        this.isRemoved = false;
    }

    public AddressEntity(RegionEnum region, String address, String postalCode) {
        this();
        this.region = region;
        this.address = address;
        this.postalCode = postalCode;
    }

    public RegionEnum getRegion() {
        return region;
    }

    public void setRegion(RegionEnum region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public OTUserEntity getUser() {
        return user;
    }

    public void setUser(OTUserEntity user) {
        this.user = user;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (addressId != null ? addressId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the addressId fields are not set
        if (!(object instanceof AddressEntity)) {
            return false;
        }
        AddressEntity other = (AddressEntity) object;
        if ((this.addressId == null && other.addressId != null) || (this.addressId != null && !this.addressId.equals(other.addressId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AddressEntity[ id=" + addressId + " ]";
    }

    public boolean isIsRemoved() {
        return isRemoved;
    }

    public void setIsRemoved(boolean isRemoved) {
        this.isRemoved = isRemoved;
    }

}
