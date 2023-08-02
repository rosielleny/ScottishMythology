package com.weakness.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Weakness {
	
//	CREATE TABLE Weakness(
//			WeaknessPK int auto_increment primary key,
//		    WeaknessName varchar(20)
//		);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int weaknessPK;
	private String weaknessName;
	
	
	public Weakness() {
		
	}
	
	public Weakness(int weaknessPK, String weaknessName) {
		super();
		this.weaknessPK = weaknessPK;
		this.weaknessName = weaknessName;
	}
	
	public int getWeaknessPK() {
		return weaknessPK;
	}
	public void setWeaknessPK(int weaknessPK) {
		this.weaknessPK = weaknessPK;
	}
	public String getWeaknessName() {
		return weaknessName;
	}
	public void setWeaknessName(String weaknessName) {
		this.weaknessName = weaknessName;
	}
	
	
}
