package com.myth.service.micro;

import java.util.List;

import com.myth.entity.Gender;

public interface GenderService {
	
	// Create
	Gender createGender(Gender ability);
	
	// Read
	List<Gender> getAllGender();
	Gender getGenderById(int abilityPK);
	Gender getGenderByName(String abilityName);
	
	// Update
	Boolean updateGender(Gender ability);
	
	// Delete
	Boolean deleteGender(int abilityPK);
	

}
