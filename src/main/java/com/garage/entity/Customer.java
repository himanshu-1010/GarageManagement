package com.garage.entity;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class Customer implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GARAGE_SEQ")
	@SequenceGenerator(sequenceName = "GARAGE_SEQUENCE", allocationSize = 1, name = "GARAGE_SEQ")
	private int id;
	private byte active;
	@Column(name = "CREATION_TIMESTAMP")
	private Timestamp creationTimestamp;
	@Column(name = "MODIFICATION_TIMESTAMP")
	private Timestamp modificationTimestamp;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	private String firstName;
	private String lastName;
	private String email;
	private String mobileNumber;
	private String cityName;
	private String nationalId;
	private String drivingLicense;
	private byte isApproved;
	private String password;
	private String role;
	private byte isLoggedIn;
	@Transient
	private String otp;
	private byte isEmailVerified;
	@Column(name = "PROFILE_PICTURE_PATH")
	private String profilePicturePath;
	private String status;
	private String latitude;
	private String longitude;
    private byte isActive;
    @JsonIgnore
    @OneToMany(mappedBy="customer",fetch =FetchType.LAZY)
    private List<Support> supports;
	public Customer() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getActive() {
		return active;
	}

	public void setActive(byte active) {
		this.active = active;
	}

	public Timestamp getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Timestamp creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public Timestamp getModificationTimestamp() {
		return modificationTimestamp;
	}

	public void setModificationTimestamp(Timestamp modificationTimestamp) {
		this.modificationTimestamp = modificationTimestamp;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	

	public List<Support> getSupports() {
		return supports;
	}

	public void setSupports(List<Support> supports) {
		this.supports = supports;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	

	public byte getIsActive() {
		return isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public String getNationalId() {
		return nationalId;
	}

	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}

	public String getDrivingLicense() {
		return drivingLicense;
	}

	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

	public byte getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(byte isApproved) {
		this.isApproved = isApproved;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	

	public byte getIsLoggedIn() {
		return isLoggedIn;
	}

	public void setIsLoggedIn(byte isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public byte getIsEmailVerified() {
		return isEmailVerified;
	}

	public void setIsEmailVerified(byte isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public String getProfilePicturePath() {
		return profilePicturePath;
	}

	public void setProfilePicturePath(String profilePicturePath) {
		this.profilePicturePath = profilePicturePath;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
