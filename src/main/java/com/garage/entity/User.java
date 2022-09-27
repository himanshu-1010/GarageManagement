package com.garage.entity;

import java.sql.Timestamp;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
@Entity
@Data
public class User implements UserDetails {
@Id
@GeneratedValue(strategy=GenerationType.AUTO)
private int id;
private String firstName;
private String lastName;
private String email;
private String password;
private String address;
private int employeeId;
private String mobileNumber;
private Timestamp creationTimestamp;
private Timestamp modificationTimestamp;
private Timestamp lastLoggedInTimestamp;
private Timestamp lastLoggedOutTimestamp;
private String profilePicturepath;
private String createdBy;
private byte isLoggedIn;
private byte isEmailVerified;
private byte isActive;
private byte active;
@OneToOne(cascade = CascadeType.ALL)
private Role role;
public User() {
	super();
	// TODO Auto-generated constructor stub
}
public User(int id, String firstName, String lastName, String email, String password, String address, int employeeId,
		Timestamp creationTimestamp, Timestamp modificationTimestamp, Timestamp lastLoggedInTimestamp,
		Timestamp lastLoggedOutTimestamp, String createdBy, byte isLoggedIn, byte isEmailVerified, byte isActive,
		byte active, Role role) {
	super();
	this.id = id;
	this.firstName = firstName;
	this.lastName = lastName;
	this.email = email;
	this.password = password;
	this.address = address;
	this.employeeId=employeeId;
	this.creationTimestamp = creationTimestamp;
	this.modificationTimestamp = modificationTimestamp;
	this.lastLoggedInTimestamp = lastLoggedInTimestamp;
	this.lastLoggedOutTimestamp = lastLoggedOutTimestamp;
	this.createdBy = createdBy;
	this.isLoggedIn = isLoggedIn;
	this.isEmailVerified = isEmailVerified;
	this.isActive = isActive;
	this.active = active;
	this.role = role;
}
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

public String getMobileNumber() {
	return mobileNumber;
}
public void setMobileNumber(String mobileNumber) {
	this.mobileNumber = mobileNumber;
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
	this.password = password;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}

public int getEmployeeId() {
	return employeeId;
}
public void setEmployeeId(int employeeId) {
	this.employeeId = employeeId;
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
public Timestamp getLastLoggedInTimestamp() {
	return lastLoggedInTimestamp;
}
public void setLastLoggedInTimestamp(Timestamp lastLoggedInTimestamp) {
	this.lastLoggedInTimestamp = lastLoggedInTimestamp;
}
public Timestamp getLastLoggedOutTimestamp() {
	return lastLoggedOutTimestamp;
}
public void setLastLoggedOutTimestamp(Timestamp lastLoggedOutTimestamp) {
	this.lastLoggedOutTimestamp = lastLoggedOutTimestamp;
}
public String getCreatedBy() {
	return createdBy;
}
public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
}
public byte getIsLoggedIn() {
	return isLoggedIn;
}
public void setIsLoggedIn(byte isLoggedIn) {
	this.isLoggedIn = isLoggedIn;
}
public byte getIsEmailVerified() {
	return isEmailVerified;
}
public void setIsEmailVerified(byte isEmailVerified) {
	this.isEmailVerified = isEmailVerified;
}
public byte getIsActive() {
	return isActive;
}
public void setIsActive(byte isActive) {
	this.isActive = isActive;
}

public String getProfilePicturepath() {
	return profilePicturepath;
}
public void setProfilePicturepath(String profilePicturepath) {
	this.profilePicturepath = profilePicturepath;
}
public byte getActive() {
	return active;
}
public void setActive(byte active) {
	this.active = active;
}
public Role getRole() {
	return role;
}
public void setRole(Role role) {
	this.role = role;
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
