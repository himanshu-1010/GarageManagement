package com.garage.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
@Entity
@Data
public class Role {
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private int id;
private String name;
private byte active;
private Timestamp creationTimestamp;
private Timestamp modificationTimestamp;
private String createdBy;
public Role() {
	super();
	// TODO Auto-generated constructor stub
}
public Role(int id, String name, byte active, Timestamp creationTimestamp, Timestamp modificationTimestamp,
		String createdBy) {
	super();
	this.id = id;
	this.name = name;
	this.active = active;
	this.creationTimestamp = creationTimestamp;
	this.modificationTimestamp = modificationTimestamp;
	this.createdBy = createdBy;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
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

}
