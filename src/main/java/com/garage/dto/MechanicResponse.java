package com.garage.dto;

import java.sql.Timestamp;

public class MechanicResponse {

	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String mobileNumber;
	private String cityName;
	private String mechanicStoreName;
	private String idNumber;
	private Timestamp creationTimestamp;
	private Timestamp modificationTimestamp;
	private String createdBy;
	private String modifiedBy;
	private String profilePicturePath;
	private byte isEmailVerified;
	private byte isApproved;
	private byte isAvailable;
	private byte isBooked;
	private byte isDone;
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
