package com.myth.service.micro;

import java.util.List;

import com.myth.entity.Ability;

public interface AbilityService {
	
	// Create
	Ability createAbility(Ability ability);
	
	// Read
	List<Ability> getAllAbility();
	Ability getAbilityById(int abilityPK);
	Ability getAbilityByName(String abilityName);
	
	// Update
	Boolean updateAbility(Ability ability);
	
	// Delete
	Boolean deleteAbility(int abilityPK);
	

}
