package com.myth.service.micro;

import java.util.List;

import com.myth.entity.Weakness;

public interface WeaknessService {
	
	// Create
	Weakness createWeakness(Weakness weakness);
	
	// Read
	List<Weakness> getAllWeakness();
	Weakness getWeaknessById(int weaknessPK);
	Weakness getWeaknessByName(String weaknessName);
	
	// Update
	Boolean updateWeakness(Weakness weakness);
	
	// Delete
	Boolean deleteWeakness(int weaknessPK);
	

}
