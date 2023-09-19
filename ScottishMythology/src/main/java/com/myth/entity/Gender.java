package com.myth.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



@Entity
public class Gender {
	
//	CREATE TABLE Gender(
//			GenderPK int auto_increment primary key,
//		    GenderType varchar(20),
//		    GenderDescription varchar(255)
//		);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int genderPK;
	private String genderType;

	
	
	public Gender() {
		
	}
	
	public Gender(int genderPK, String genderType) {
		super();
		this.genderPK = genderPK;
		this.genderType = genderType;
		
	}
	
	public int getGenderPK() {
		return genderPK;
	}
	public void setGenderPK(int genderPK) {
		this.genderPK = genderPK;
	}
	public String getGenderType() {
		return genderType;
	}
	public void setGenderType(String genderType) {
		this.genderType = genderType;
	}

	@Override
	public String toString() {
		return "Gender [genderPK=" + genderPK + ", genderType=" + genderType + "]";
	}
	
	
	
	
}
