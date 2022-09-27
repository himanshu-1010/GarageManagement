package com.garage.entity;

import java.sql.Timestamp;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
@Entity
@Data
public class SuperAdmin implements UserDetails {
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private int id;
private String firstName;
private String lastName;
private String email;
private String mobile;
private String password;
private Timestamp creationTimestamp;
private Timestamp modificationTimestamp;
private Timestamp lastLoggedInTimestamp;
private Timestamp lastloggedOutTimestamp;
private byte isLoggedIn;
private byte active;
private String role;
public SuperAdmin() {
	super();
	// TODO Auto-generated constructor stub
}
public SuperAdmin(int id, String firstName, String lastName, String email, String mobile, String password,
		Timestamp creationTimestamp, Timestamp modificationTimestamp, Timestamp lastLoggedInTimestamp,
		Timestamp lastloggedOutTimestamp, byte isLoggedIn, byte active, String role) {
	super();
	this.id = id;
	this.firstName = firstName;
	this.lastName = lastName;
	this.email = email;
	this.mobile = mobile;
	this.password = password;
	this.creationTimestamp = creationTimestamp;
	this.modificationTimestamp = modificationTimestamp;
	this.lastLoggedInTimestamp = lastLoggedInTimestamp;
	this.lastloggedOutTimestamp = lastloggedOutTimestamp;
	this.isLoggedIn = isLoggedIn;
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
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getMobile() {
	return mobile;
}
public void setMobile(String mobile) {
	this.mobile = mobile;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
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
public Timestamp getLastloggedOutTimestamp() {
	return lastloggedOutTimestamp;
}
public void setLastloggedOutTimestamp(Timestamp lastloggedOutTimestamp) {
	this.lastloggedOutTimestamp = lastloggedOutTimestamp;
}
public byte getIsLoggedIn() {
	return isLoggedIn;
}
public void setIsLoggedIn(byte isLoggedIn) {
	this.isLoggedIn = isLoggedIn;
}
public byte getActive() {
	return active;
}
public void setActive(byte active) {
	this.active = active;
}
public String getRole() {
	return role;
}
public void setRole(String role) {
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
