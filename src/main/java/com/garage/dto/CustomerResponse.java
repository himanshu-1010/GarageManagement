package com.garage.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class CustomerResponse {

	private int id;
	private String firstName;
	private String lastName;
	private Timestamp creationTimestamp;
	private Timestamp modificationTimestamp;
	private String createdBy;
	private String modifiedBy;
	private String email;
	private String mobileNumber;
	private String cityName;
	private String nationalId;
	private String drivingLicense;
	private byte isApproved;
	private byte isEmailVerified;
	private String profilePicturePath;
	private String status;
	private String latitude;
	private String longitude;
	private byte isLoggedIn;
	private byte isActive;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
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

	public byte getIsLoggedIn() {
		return isLoggedIn;
	}

	public void setIsLoggedIn(byte isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public byte getIsActive() {
		return isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

}
