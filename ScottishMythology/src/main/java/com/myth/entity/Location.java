package com.myth.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



@Entity
public class Location {
	
//	CREATE TABLE Location(
//			LocationPK int auto_increment primary key,
//		    LocationName varchar(100),
//		    LocationDescription varchar(255)
//		);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int locationPK;
	private String locationName;
	private String locationDescription;
	
	
	public Location() {
		
	}
	
	public Location(int locationPK, String locationName, String locationDescription) {
		super();
		this.locationPK = locationPK;
		this.locationName = locationName;
		this.locationDescription = locationDescription;
	}
	
	public int getLocationPK() {
		return locationPK;
	}
	public void setLocationPK(int locationPK) {
		this.locationPK = locationPK;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getLocationDescription() {
		return locationDescription;
	}
	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}
	
	
}
