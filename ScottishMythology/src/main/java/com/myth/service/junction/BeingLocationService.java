package com.myth.service.junction;

import java.util.List;

import com.myth.entity.junction.BeingLocation;
import com.myth.entity.junction.KeyBeingLocation;
import com.myth.service.EntityService;

public interface BeingLocationService{

	// Create
	BeingLocation createBeingLocation(BeingLocation beingLocation);

	// Read
	List<BeingLocation> getAllBeingLocation();
	BeingLocation getBeingLocationById(KeyBeingLocation beingLocationPK);
	List<BeingLocation> getBeingLocationByBeingId(int beingPK);
	List<BeingLocation> getBeingLocationByLocationId(int locationPK);

	// Update
	Boolean updateBeingLocation(BeingLocation beingLocation);

	// Delete
	Boolean deleteBeingLocation(KeyBeingLocation beingLocationPK);

}
