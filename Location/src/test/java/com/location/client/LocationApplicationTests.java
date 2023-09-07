package com.location.client;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.http.MediaType;

import com.location.dao.LocationDao;
import com.location.entity.Location;
import com.location.service.LocationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@AutoConfigureMockMvc
class LocationApplicationTests {

	@Test
	void contextLoads() {
	}
	
	//@Autowired
	//private LocationService locationService;
	@MockBean
	private LocationDao locationDao;
	@Autowired
    private MockMvc mockMvc;
	@Autowired
	private LocationService locationService;
	
	
	// Service Tests
	@Test
	@DisplayName("Test getting location by ID")
	void testGetByID() {
		
		// Setting up mock repository
		Location location = new Location(1, "TestLocation", "Description");
		doReturn(Optional.of(location)).when(locationDao).findById(1);
		
		// Execute the service call
		Optional<Location> returnedLocation = Optional.ofNullable(locationService.getLocationById(1));
		
		// Assert the response
		Assertions.assertTrue(returnedLocation.isPresent(), "Location was not found");
		Assertions.assertSame(returnedLocation.get(), location, "The location returned was not the same as the mock");
		
	}

	@Test
	@DisplayName("Test Not Found get location by ID")
	void testGetByIdNotFound() {
		// Setup our mock repository
		doReturn(Optional.empty()).when(locationDao).findById(1);

		// Execute the service call
		Optional<Location> returnedLocation = Optional.ofNullable(locationService.getLocationById(1));

		// Assert the response
		Assertions.assertFalse(returnedLocation.isPresent(), "Location should NOT be found, but was");
	}
	
	@Test
	@DisplayName("Test findAll")
	void testFindAll() {
		// Setup our mock repository
		Location location1 = new Location(1, "Location Name", "Description");
		Location location2 = new Location(2, "Location 2 Name", "Description");
		doReturn(Arrays.asList(location1, location2)).when(locationDao).findAll();
		when(locationDao.findAll()).thenReturn(Arrays.asList(location1, location2));

		// Execute the service call
		List<Location> locations = locationService.getAllLocation();

		// Assert the response
		Assertions.assertEquals(2, locations.size(), "findAll should return 2 abilities");
	}
	
	@Test
	@DisplayName("Test create location")
	void testCreate() {
	    // Setup our mock repository
	    Location location = new Location(1, "Location Name", "Description");

	    // Mocking behaviour for locationDao.save
	    doAnswer(invocation -> {
	        Location argLocation = invocation.getArgument(0);
	        return new Location(argLocation.getLocationPK() + 1, argLocation.getLocationName(), argLocation.getLocationDescription());
	    }).when(locationDao).save(any());

	    // Mocking behaviour for getLocationByName
	    when(locationDao.findByName(anyString())).thenReturn(new Location(2, "Location Name", "Description"));

	    // Execute the service call
	    Location returnedLocation = locationService.createLocation(location);

	    // Assert the response
	    Assertions.assertNotNull(returnedLocation, "The saved location should not be null");
	    Assertions.assertEquals(2, returnedLocation.getLocationPK(), "The ID should be incremented");
	}


	
	@Test
	@DisplayName("Test delete location")
	void testDelete() {
	    // Setup our mock repository
	    Location location = new Location(1, "Location Name", "Description");
	    
	    doReturn(Optional.of(location)).when(locationDao).findById(1);

	    doNothing().when(locationDao).deleteById(1);

	    // Execute the service call
	    Boolean returnedLocation = locationService.deleteLocation(1);

	    // Assert the response
	    Assertions.assertNotNull(returnedLocation, "The deleted location should not be null, should be boolean.");
	    
	    // After deletion, the location should not be found.
	    doReturn(Optional.empty()).when(locationDao).findById(1);
	    Assertions.assertNull(locationService.getLocationById(1));

	    Assertions.assertEquals(true, returnedLocation, "Should return true if deleted.");
	}


	
	@Test
	@DisplayName("Test edit location")
	void testEdit() {
	    // Setup our mock repository
	    Location location = new Location(1, "Location Name", "Description");
	    
	    doReturn(Optional.of(location)).when(locationDao).findById(1);

	    // Execute the service call
	    Location updatedLocation = new Location(1, "Updated Location Name", "Description");
	    locationService.updateLocation(updatedLocation);
	    
	    doReturn(Optional.of(updatedLocation)).when(locationDao).findById(1);
	    
	    Location returnedLocation = locationService.getLocationById(1);

	    // Assert the response
	    Assertions.assertNotNull(returnedLocation, "The updated location should not be null");
	    Assertions.assertEquals(1, returnedLocation.getLocationPK(), "The ID should be same as the one updated");
	    Assertions.assertEquals("Updated Location Name", returnedLocation.getLocationName(), "The Name should be updated");
	}


}
