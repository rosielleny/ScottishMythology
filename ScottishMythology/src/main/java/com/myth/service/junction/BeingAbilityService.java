package com.myth.service.junction;

import java.util.List;

import com.myth.entity.junction.BeingAbility;
import com.myth.entity.junction.KeyBeingAbility;


public interface BeingAbilityService{

	// Create
	BeingAbility createBeingAbility(BeingAbility beingAbility);

	// Read
	List<BeingAbility> getAllBeingAbility();
	BeingAbility getBeingAbilityById(KeyBeingAbility beingAbilityPK);
	List<BeingAbility> getBeingAbilityByBeingId(int beingPK);
	List<BeingAbility> getBeingAbilityByAbilityId(int abilityPK);

	// Update
	Boolean updateBeingAbility(BeingAbility beingAbility);

	// Delete
	Boolean deleteBeingAbility(KeyBeingAbility beingAbilityPK);

}
