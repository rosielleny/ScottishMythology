package com.location.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.location.entity.Location;
import com.location.dao.LocationDao;
import com.location.entity.Location;

@Service
public class LocationServiceImpl implements LocationService {

	@Autowired
	private LocationDao locationDao;
	
	// Create
	
	@Override
	public Location createLocation(Location location) {
		
		if(locationDao.save(location) != null) {
			Location createdLocation = getLocationByName(location.getLocationName());
			return createdLocation;
		} 
		else {
			return null;
		}
		
	}
	
	//Read

	@Override
	public List<Location> getAllLocation() {
		
		return locationDao.findAll();
	}

	@Override
	public Location getLocationById(int locationPK) {
		
		Location location = locationDao.findById(locationPK).orElse(null);
		return location;
	}

	@Override
	public Location getLocationByName(String locationName) {
		
		return locationDao.findByName(locationName);
	}

	// Update
	
	@Override
	public Boolean updateLocation(Location location) {
		
		Location locationExists = getLocationById(location.getLocationPK());
		
		if(locationExists !=null) {
			
			locationDao.save(location);
			return true;
			
		}
		return false;
	}

	// Delete
	
	@Override
	public Boolean deleteLocation(int locationPK) {
		
		Location location = getLocationById(locationPK);
		
		if(location !=null) {
			
			locationDao.deleteById(locationPK);
			return true;
			
		}
		return false;
	}

}
