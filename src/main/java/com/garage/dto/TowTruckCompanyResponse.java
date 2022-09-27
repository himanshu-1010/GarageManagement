package com.garage.dto;

import java.sql.Timestamp;

import lombok.Data;



@Data
public class TowTruckCompanyResponse {
	private int id;
	private byte active;

	private Timestamp creationTimestamp;

	private Timestamp modificationTimestamp;

	private String createdBy;
	
	private String modifiedBy;
	private String firstName;
	private String lastName;
	private String email;
	private String mobileNumber;
	private String cityName;
	private String companyName;
	private String registrationNumber;
	private byte isApproved;
	private String role;
	private byte isEmailVerified;
	private String profilePicturePath;
	private String status;
	private String latitude;
	private String longitude;
	private byte isActive;
	public TowTruckCompanyResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TowTruckCompanyResponse(int id, byte active, Timestamp creationTimestamp, Timestamp modificationTimestamp,
			String createdBy, String modifiedBy, String firstName, String lastName, String email, String mobileNumber,
			String cityName, String companyName, String registrationNumber, byte isApproved, String role,
			byte isEmailVerified, String profilePicturePath, String status, String latitude, String longitude,
			byte isActive) {
		super();
		this.id = id;
		this.active = active;
		this.creationTimestamp = creationTimestamp;
		this.modificationTimestamp = modificationTimestamp;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.cityName = cityName;
		this.companyName = companyName;
		this.registrationNumber = registrationNumber;
		this.isApproved = isApproved;
		this.role = role;
		this.isEmailVerified = isEmailVerified;
		this.profilePicturePath = profilePicturePath;
		this.status = status;
		this.latitude = latitude;
		this.longitude = longitude;
		this.isActive = isActive;
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
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
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
	public byte getIsActive() {
		return isActive;
	}
	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}
	
}
