package com.garage.entity;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class Vehicle {

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
	private String name;
	private String image;
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "VEHICLE_TYPE_ID", insertable = true, updatable = true)
	private VehicleType vehicleType;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

}
