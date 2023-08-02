package com.location.service;

import java.util.List;

import com.location.entity.Location;

public interface LocationService {
	
	// Create
	Location createLocation(Location location);
	
	// Read
	List<Location> getAllLocation();
	Location getLocationById(int locationPK);
	Location getLocationByName(String locationName);
	
	// Update
	Boolean updateLocation(Location location);
	
	// Delete
	Boolean deleteLocation(int locationPK);
	

}
