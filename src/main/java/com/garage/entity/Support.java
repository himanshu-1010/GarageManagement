package com.garage.entity;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Support {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private int supportNumber;
	private String details;
	private byte active;
	@ManyToOne(cascade = CascadeType.DETACH)
	private Customer customer;
	private Timestamp creationTimestamp;
	private Timestamp modificationTimestamp;
	private String createdBy;
	private String modifiedBY;
	private String status;

	public Support() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Support(int id, int supportNumber, String details, byte active, Customer customer,
			Timestamp creationTimestamp, Timestamp modificationTimestamp, String createdBy, String modifiedBY) {
		super();
		this.id = id;
		this.supportNumber = supportNumber;
		this.details = details;
		this.active = active;
		this.customer = customer;
		this.creationTimestamp = creationTimestamp;
		this.modificationTimestamp = modificationTimestamp;
		this.createdBy = createdBy;
		this.modifiedBY = modifiedBY;
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

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
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

	public String getModifiedBY() {
		return modifiedBY;
	}

	public void setModifiedBY(String modifiedBY) {
		this.modifiedBY = modifiedBY;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
