package com.myth.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.myth.dao.junction.BeingLocationDao;
import com.myth.entity.junction.BeingLocation;
import com.myth.entity.junction.KeyBeingLocation;
import com.myth.service.junction.BeingLocationService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
class BeingLocationServiceTests {

	@Test
	void contextLoads() {
	}
	
	KeyBeingLocation kBA = new KeyBeingLocation(1, 1);
	KeyBeingLocation kBA2 = new KeyBeingLocation(1, 2);
	BeingLocation beingLocation = new BeingLocation(kBA);
	BeingLocation beingLocation2 = new BeingLocation(kBA2);
	
	//@Autowired
	//private BeingLocationService beingLocationService;
	@MockBean
	private BeingLocationDao beingLocationDao;
	@Autowired
	private BeingLocationService beingLocationService;
	
	
	// Service Tests
	@Test
	@DisplayName("Test getting beingLocation by ID")
	void testGetByID() {
		
		// Setting up mock repository
		
		doReturn(Optional.of(beingLocation)).when(beingLocationDao).findById(kBA);
		
		// Execute the service call
		Optional<BeingLocation> returnedBeingLocation = Optional.ofNullable(beingLocationService.getBeingLocationById(kBA));
		
		// Assert the response
		Assertions.assertTrue(returnedBeingLocation.isPresent(), "BeingLocation was not found");
		Assertions.assertSame(returnedBeingLocation.get(), beingLocation, "The beingLocation returned was not the same as the mock");
		
	}

	@Test
	@DisplayName("Test Not Found get beingLocation by ID")
	void testGetByIdNotFound() {
		// Setup our mock repository
		doReturn(Optional.empty()).when(beingLocationDao).findById(kBA);

		// Execute the service call
		Optional<BeingLocation> returnedBeingLocation = Optional.ofNullable(beingLocationService.getBeingLocationById(kBA));

		// Assert the response
		Assertions.assertFalse(returnedBeingLocation.isPresent(), "BeingLocation should NOT be found, but was");
	}
	
	@Test
	@DisplayName("Test findAll")
	void testFindAll() {
		
		doReturn(Arrays.asList(beingLocation, beingLocation2)).when(beingLocationDao).findAll();
		

		// Execute the service call
		List<BeingLocation> beingLocations = beingLocationService.getAllBeingLocation();

		// Assert the response
		Assertions.assertEquals(2, beingLocations.size(), "findAll should return 2 abilities");
	}
	
	@Test
	@DisplayName("Test create beingLocation")
	void testCreate() {
	    // Setup our mock repository

	   when(beingLocationDao.save(beingLocation)).thenReturn(beingLocation);

	
	    // Execute the service call
	    BeingLocation returnedBeingLocation = beingLocationService.createBeingLocation(beingLocation);

	    // Assert the response
	    Assertions.assertNotNull(returnedBeingLocation, "The saved beingLocation should not be null");
	   
	}


	
	@Test
	@DisplayName("Test delete beingLocation")
	void testDelete() {
	    // Setup our mock repository
	    
	    doReturn(Optional.of(beingLocation)).when(beingLocationDao).findById(kBA);

	    doNothing().when(beingLocationDao).deleteById(kBA);

	    // Execute the service call
	    Boolean returnedBeingLocation = beingLocationService.deleteBeingLocation(kBA);

	    // Assert the response
	    Assertions.assertNotNull(returnedBeingLocation, "The deleted beingLocation should not be null, should be boolean.");
	    
	    // After deletion, the beingLocation should not be found.
	    doReturn(Optional.empty()).when(beingLocationDao).findById(kBA);
	    Assertions.assertNull(beingLocationService.getBeingLocationById(kBA));

	    Assertions.assertEquals(true, returnedBeingLocation, "Should return true if deleted.");
	}


	
	@Test
	@DisplayName("Test edit beingLocation")
	void testEdit() {
	   
		KeyBeingLocation updatedKey = new KeyBeingLocation(1, 3);
	    BeingLocation updatedBeingLocation = new BeingLocation(updatedKey);
	    
	    when(beingLocationDao.save(beingLocation)).thenReturn(beingLocation);
	    when(beingLocationDao.findById(kBA)).thenReturn(Optional.of(beingLocation));
	    doReturn(Optional.of(updatedBeingLocation)).when(beingLocationDao).findById(updatedKey);

	    Boolean result = beingLocationService.updateBeingLocation(updatedBeingLocation);

	    // Assert the response
	    Assertions.assertTrue(result, "The updated beingLocation should not be null");
	
	}


}
