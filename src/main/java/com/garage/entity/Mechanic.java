package com.garage.entity;

import java.sql.Timestamp;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
@Entity
public class Mechanic implements UserDetails{

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
	private String mechanicStoreName;
	private String idNumber;
	private String password;
	@Column(name = "PROFILE_PICTURE_PATH")
	private String profilePicturePath;
	private byte isEmailVerified;
	private byte isApproved;
	private byte isLoggedIn;
	private String role;
	@Transient
	private String otp;
	private byte isAvailable;
	private byte isBooked;
	private byte isDone;
	private String status;
	private String latitude;
	private String longitude;
    private byte isActive;
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
	

	public byte getIsActive() {
		return isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public byte getIsLoggedIn() {
		return isLoggedIn;
	}

	public void setIsLoggedIn(byte isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
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

	public String getMechanicStoreName() {
		return mechanicStoreName;
	}

	public void setMechanicStoreName(String mechanicStoreName) {
		this.mechanicStoreName = mechanicStoreName;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfilePicturePath() {
		return profilePicturePath;
	}

	public void setProfilePicturePath(String profilePicturePath) {
		this.profilePicturePath = profilePicturePath;
	}

	public byte getIsEmailVerified() {
		return isEmailVerified;
	}

	public void setIsEmailVerified(byte isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public byte getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(byte isApproved) {
		this.isApproved = isApproved;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public byte getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(byte isAvailable) {
		this.isAvailable = isAvailable;
	}

	public byte getIsBooked() {
		return isBooked;
	}

	public void setIsBooked(byte isBooked) {
		this.isBooked = isBooked;
	}

	public byte getIsDone() {
		return isDone;
	}

	public void setIsDone(byte isDone) {
		this.isDone = isDone;
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
