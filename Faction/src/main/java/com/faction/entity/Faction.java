package com.faction.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



@Entity
public class Faction {
	
//	CREATE TABLE Faction(
//			FactionPK int auto_increment primary key,
//		    FactionName varchar(20),
//		    FactionDescription varchar(255)
//		);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int factionPK;
	private String factionName;
	private String factionDescription;
	
	
	public Faction() {
		
	}
	
	public Faction(int factionPK, String factionName, String factionDescription) {
		super();
		this.factionPK = factionPK;
		this.factionName = factionName;
		this.factionDescription = factionDescription;
	}
	
	public int getFactionPK() {
		return factionPK;
	}
	public void setFactionPK(int factionPK) {
		this.factionPK = factionPK;
	}
	public String getFactionName() {
		return factionName;
	}
	public void setFactionName(String factionName) {
		this.factionName = factionName;
	}
	public String getFactionDescription() {
		return factionDescription;
	}
	public void setFactionDescription(String factionDescription) {
		this.factionDescription = factionDescription;
	}
	
	
}
