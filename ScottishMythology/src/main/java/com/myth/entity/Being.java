package com.myth.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Being {
	
//	CREATE TABLE Being(
//			BeingPK int auto_increment primary key,
//		    BeingName varchar(100), 
//		    BeingSpecies int,
//		    BeingDescription varchar(255),
//		    BeingGender int,
//		    BeingArt BLOB, -- BLOB stores image data
//		    BeingFaction int,
//		    foreign key (BeingSpecies) references Species(SpeciesPK),
//		    foreign key (BeingGender) references Gender(GenderPK),
//		    foreign key (BeingFaction) references Faction(FactionPK)
//		);
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int beingPK;
	private String beingName;
	private int beingSpecies;
	private String beingDescription;
	private int beingGender;
	@Lob
    private byte[] beingArt;
	private int beingFaction;
	
	
	public Being() {
		
	}
	
	public Being(int beingPK, String beingName, int beingSpecies, String beingDescription, int beingGender,
			byte[] beingArt, int beingFaction) {
		super();
		this.beingPK = beingPK;
		this.beingName = beingName;
		this.beingSpecies = beingSpecies;
		this.beingDescription = beingDescription;
		this.beingGender = beingGender;
		this.beingArt = beingArt;
		this.beingFaction = beingFaction;
	}
	
	public int getBeingPK() {
		return beingPK;
	}
	public void setBeingPK(int beingPK) {
		this.beingPK = beingPK;
	}
	public String getBeingName() {
		return beingName;
	}
	public void setBeingName(String beingName) {
		this.beingName = beingName;
	}
	public int getBeingSpecies() {
		return beingSpecies;
	}
	public void setBeingSpecies(int beingSpecies) {
		this.beingSpecies = beingSpecies;
	}
	public String getBeingDescription() {
		return beingDescription;
	}
	public void setBeingDescription(String beingDescription) {
		this.beingDescription = beingDescription;
	}
	public int getBeingGender() {
		return beingGender;
	}
	public void setBeingGender(int beingGender) {
		this.beingGender = beingGender;
	}
	public byte[] getBeingArt() {
		return beingArt;
	}
	public void setBeingArt(byte[] beingArt) {
		this.beingArt = beingArt;
	}
	public int getBeingFaction() {
		return beingFaction;
	}
	public void setBeingFaction(int beingFaction) {
		this.beingFaction = beingFaction;
	} 
	

}
