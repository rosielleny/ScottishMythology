package com.myth.service.junction;

import java.util.List;

import com.myth.entity.junction.BeingWeakness;
import com.myth.entity.junction.KeyBeingWeakness;
import com.myth.service.EntityService;

public interface BeingWeaknessService{

	// Create
	BeingWeakness createBeingWeakness(BeingWeakness beingWeakness);

	// Read
	List<BeingWeakness> getAllBeingWeakness();
	BeingWeakness getBeingWeaknessById(KeyBeingWeakness beingWeaknessPK);
	List<BeingWeakness> getBeingWeaknessByBeingId(int beingPK);
	List<BeingWeakness> getBeingWeaknessByWeaknessId(int weaknessPK);

	// Update
	Boolean updateBeingWeakness(BeingWeakness beingWeakness);

	// Delete
	Boolean deleteBeingWeakness(KeyBeingWeakness beingWeaknessPK);

}
