package com.myth.service;

import java.util.List;

import com.myth.entity.Being;

public interface BeingService{
	
	// Create
	Being createBeing(Being being);
	
	// Read
	List<Being> getAllBeing();
	Being getBeingById(int beingPK);
	Being getBeingByName(String beingName);
	List<Being> getBeingByFaction(int factionPK);
	List<Being> getBeingByGender(int genderPK);
	List<Being> getBeingBySpecies(int speciesPK);
	
	// Update
	Boolean updateBeing(Being being);
	
	// Delete
	Boolean deleteBeing(int beingPK);

	

	



}
