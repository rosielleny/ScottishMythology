package com.ability.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



@Entity
public class Ability {
	
//	CREATE TABLE Ability(
//			AbilityPK int auto_increment primary key,
//		    AbilityName varchar(20)
//		);
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int abilityPK;
	private String abilityName;
	
	
	public Ability() {
		
	}
	
	public Ability(int abilityPK, String abilityName) {
		super();
		this.abilityPK = abilityPK;
		this.abilityName = abilityName;

	}
	
	public int getAbilityPK() {
		return abilityPK;
	}
	public void setAbilityPK(int abilityPK) {
		this.abilityPK = abilityPK;
	}
	public String getAbilityName() {
		return abilityName;
	}
	public void setAbilityName(String abilityName) {
		this.abilityName = abilityName;
	}

	
}
