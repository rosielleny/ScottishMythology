package com.location.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.location.entity.Location;
import com.location.service.LocationService;


@Controller
public class LocationResource {
	
	@Autowired
	private LocationService locationService;
	
	// Create
	@CrossOrigin
	@PostMapping(path = "/location/new-location", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Location> createLocation(@RequestBody Location location) {

		Location newLocation = locationService.createLocation(location);

		if (newLocation != null) {
			return new ResponseEntity<>(newLocation, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}


	// Read
	// Get by ID
	@CrossOrigin
	@GetMapping(path = "/location/{locationPK}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Location> getLocationById(@PathVariable int locationPK){

		Location location = locationService.getLocationById(locationPK);

		if (location != null) {
			return new ResponseEntity<Location>(location, HttpStatus.OK);
		}else {
			return new ResponseEntity<Location>(location, HttpStatus.NO_CONTENT);
		}
	}
	
	// Read
	// Get by Name
	@CrossOrigin
	@GetMapping(path = "/location/location-by-name/{locationName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Location> getLocationByName(@PathVariable String locationName){

		Location location = locationService.getLocationByName(locationName);

		if (location != null) {
			return new ResponseEntity<Location>(location, HttpStatus.OK);
		}else {
			return new ResponseEntity<Location>(location, HttpStatus.NO_CONTENT);
		}
	}

	// Read
	// Get all
	@CrossOrigin
	@GetMapping(path = "/location/all-location", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Location>> getAllLocation(){

		List<Location> locationList = locationService.getAllLocation();

		if(locationList.size() > 0) {
			return new ResponseEntity<List<Location>>(locationList, HttpStatus.OK);
		}else {
			return new ResponseEntity<List<Location>>(locationList, HttpStatus.NO_CONTENT);
		}
	}
	
	// Update
	@CrossOrigin
	@PutMapping(path = "/location/update-location/{locationPK}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Location> updateLocation(@PathVariable int locationPK, @RequestBody Location location){

		boolean updated = locationService.updateLocation(location);
		Location locationUpdated = locationService.getLocationById(locationPK);
		if(updated) {
			return new ResponseEntity<Location>(location, HttpStatus.OK);
		}else {
			return new ResponseEntity<Location>(location, HttpStatus.NO_CONTENT);
		}

	}

	// Delete
	@CrossOrigin
	@DeleteMapping(path = "/location/delete-location/{locationPK}")
	public ResponseEntity<Boolean> deleteLocation(@PathVariable int locationPK){

		boolean deleted = locationService.deleteLocation(locationPK);

		if(deleted) {
			return new ResponseEntity<Boolean>(deleted, HttpStatus.OK);
		}else {
			return new ResponseEntity<Boolean>(deleted, HttpStatus.NO_CONTENT);
		}
	}
}
