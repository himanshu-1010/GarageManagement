package com.garage.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class SupportResponse {
private int id;
private int customerId;
private int supportNumber;
private String details;
private byte active;
private Timestamp modificationTimestamp;
private Timestamp crationtionTimestamp;
private String createdBY;
private String modifiedBy;
private String customerName;
private String status;
public SupportResponse() {
	super();
	// TODO Auto-generated constructor stub
}
public SupportResponse(int id, int supportNumber, String details, byte active, Timestamp modificationTimestamp,
		Timestamp crationtionTimestamp, String createdBY, String modifiedBy, String customerName) {
	super();
	this.id = id;
	this.supportNumber = supportNumber;
	this.details = details;
	this.active = active;
	this.modificationTimestamp = modificationTimestamp;
	this.crationtionTimestamp = crationtionTimestamp;
	this.createdBY = createdBY;
	this.modifiedBy = modifiedBy;
	this.customerName = customerName;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getSupportNumber() {
	return supportNumber;
}
public void setSupportNumber(int supportNumber) {
	this.supportNumber = supportNumber;
}
public String getDetails() {
	return details;
}
public void setDetails(String details) {
	this.details = details;
}
public byte getActive() {
	return active;
}
public void setActive(byte active) {
	this.active = active;
}
public Timestamp getModificationTimestamp() {
	return modificationTimestamp;
}
public void setModificationTimestamp(Timestamp modificationTimestamp) {
	this.modificationTimestamp = modificationTimestamp;
}
public Timestamp getCrationtionTimestamp() {
	return crationtionTimestamp;
}
public void setCrationtionTimestamp(Timestamp crationtionTimestamp) {
	this.crationtionTimestamp = crationtionTimestamp;
}
public String getCreatedBY() {
	return createdBY;
}
public void setCreatedBY(String createdBY) {
	this.createdBY = createdBY;
}
public String getModifiedBy() {
	return modifiedBy;
}
public void setModifiedBy(String modifiedBy) {
	this.modifiedBy = modifiedBy;
}
public String getCustomerName() {
	return customerName;
}
public void setCustomerName(String customerName) {
	this.customerName = customerName;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public int getCustomerId() {
	return customerId;
}
public void setCustomerId(int customerId) {
	this.customerId = customerId;
}


}
