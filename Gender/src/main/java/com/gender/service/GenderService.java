package com.gender.service;

import java.util.List;

import com.gender.entity.Gender;

public interface GenderService {
	
	// Create
	Gender createGender(Gender gender);
	
	// Read
	List<Gender> getAllGender();
	Gender getGenderById(int genderPK);
	Gender getGenderByType(String genderName);
	
	// Update
	Boolean updateGender(Gender gender);
	
	// Delete
	Boolean deleteGender(int genderPK);
	

}
