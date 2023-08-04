package com.myth.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



@Entity
public class Species {
	
//	CREATE TABLE Species(
//			SpeciesPK int auto_increment primary key,
//		    SpeciesName varchar(20),
//		    SpeciesDescription varchar(255)
//		);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int speciesPK;
	private String speciesName;
	private String speciesDescription;
	
	
	public Species() {
		
	}
	
	public Species(int speciesPK, String speciesName, String speciesDescription) {
		super();
		this.speciesPK = speciesPK;
		this.speciesName = speciesName;
		this.speciesDescription = speciesDescription;
	}
	
	public int getSpeciesPK() {
		return speciesPK;
	}
	public void setSpeciesPK(int speciesPK) {
		this.speciesPK = speciesPK;
	}
	public String getSpeciesName() {
		return speciesName;
	}
	public void setSpeciesName(String speciesName) {
		this.speciesName = speciesName;
	}
	public String getSpeciesDescription() {
		return speciesDescription;
	}
	public void setSpeciesDescription(String speciesDescription) {
		this.speciesDescription = speciesDescription;
	}
	
	
}
