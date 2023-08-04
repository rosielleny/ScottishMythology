package com.myth.service.micro;

import java.util.List;

import com.myth.entity.Location;

public interface LocationService {
	
	// Create
	Location createLocation(Location ability);
	
	// Read
	List<Location> getAllLocation();
	Location getLocationById(int abilityPK);
	Location getLocationByName(String abilityName);
	
	// Update
	Boolean updateLocation(Location ability);
	
	// Delete
	Boolean deleteLocation(int abilityPK);
	

}
