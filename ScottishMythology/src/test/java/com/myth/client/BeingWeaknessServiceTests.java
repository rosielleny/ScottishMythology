package com.myth.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.myth.dao.junction.BeingWeaknessDao;
import com.myth.entity.junction.BeingWeakness;
import com.myth.entity.junction.KeyBeingWeakness;
import com.myth.service.junction.BeingWeaknessService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
class BeingWeaknessServiceTests {

	@Test
	void contextLoads() {
	}
	
	KeyBeingWeakness kBA = new KeyBeingWeakness(1, 1);
	KeyBeingWeakness kBA2 = new KeyBeingWeakness(1, 2);
	BeingWeakness beingWeakness = new BeingWeakness(kBA);
	BeingWeakness beingWeakness2 = new BeingWeakness(kBA2);
	
	//@Autowired
	//private BeingWeaknessService beingWeaknessService;
	@MockBean
	private BeingWeaknessDao beingWeaknessDao;
	@Autowired
	private BeingWeaknessService beingWeaknessService;
	
	
	// Service Tests
	@Test
	@DisplayName("Test getting beingWeakness by ID")
	void testGetByID() {
		
		// Setting up mock repository
		
		doReturn(Optional.of(beingWeakness)).when(beingWeaknessDao).findById(kBA);
		
		// Execute the service call
		Optional<BeingWeakness> returnedBeingWeakness = Optional.ofNullable(beingWeaknessService.getBeingWeaknessById(kBA));
		
		// Assert the response
		Assertions.assertTrue(returnedBeingWeakness.isPresent(), "BeingWeakness was not found");
		Assertions.assertSame(returnedBeingWeakness.get(), beingWeakness, "The beingWeakness returned was not the same as the mock");
		
	}

	@Test
	@DisplayName("Test Not Found get beingWeakness by ID")
	void testGetByIdNotFound() {
		// Setup our mock repository
		doReturn(Optional.empty()).when(beingWeaknessDao).findById(kBA);

		// Execute the service call
		Optional<BeingWeakness> returnedBeingWeakness = Optional.ofNullable(beingWeaknessService.getBeingWeaknessById(kBA));

		// Assert the response
		Assertions.assertFalse(returnedBeingWeakness.isPresent(), "BeingWeakness should NOT be found, but was");
	}
	
	@Test
	@DisplayName("Test findAll")
	void testFindAll() {
		
		doReturn(Arrays.asList(beingWeakness, beingWeakness2)).when(beingWeaknessDao).findAll();
		

		// Execute the service call
		List<BeingWeakness> beingWeaknesss = beingWeaknessService.getAllBeingWeakness();

		// Assert the response
		Assertions.assertEquals(2, beingWeaknesss.size(), "findAll should return 2 abilities");
	}
	
	@Test
	@DisplayName("Test create beingWeakness")
	void testCreate() {
	    // Setup our mock repository

	   when(beingWeaknessDao.save(beingWeakness)).thenReturn(beingWeakness);

	
	    // Execute the service call
	    BeingWeakness returnedBeingWeakness = beingWeaknessService.createBeingWeakness(beingWeakness);

	    // Assert the response
	    Assertions.assertNotNull(returnedBeingWeakness, "The saved beingWeakness should not be null");
	   
	}


	
	@Test
	@DisplayName("Test delete beingWeakness")
	void testDelete() {
	    // Setup our mock repository
	    
	    doReturn(Optional.of(beingWeakness)).when(beingWeaknessDao).findById(kBA);

	    doNothing().when(beingWeaknessDao).deleteById(kBA);

	    // Execute the service call
	    Boolean returnedBeingWeakness = beingWeaknessService.deleteBeingWeakness(kBA);

	    // Assert the response
	    Assertions.assertNotNull(returnedBeingWeakness, "The deleted beingWeakness should not be null, should be boolean.");
	    
	    // After deletion, the beingWeakness should not be found.
	    doReturn(Optional.empty()).when(beingWeaknessDao).findById(kBA);
	    Assertions.assertNull(beingWeaknessService.getBeingWeaknessById(kBA));

	    Assertions.assertEquals(true, returnedBeingWeakness, "Should return true if deleted.");
	}


	
	@Test
	@DisplayName("Test edit beingWeakness")
	void testEdit() {
	   
		KeyBeingWeakness updatedKey = new KeyBeingWeakness(1, 3);
	    BeingWeakness updatedBeingWeakness = new BeingWeakness(updatedKey);
	    
	    when(beingWeaknessDao.save(beingWeakness)).thenReturn(beingWeakness);
	    when(beingWeaknessDao.findById(kBA)).thenReturn(Optional.of(beingWeakness));
	    doReturn(Optional.of(updatedBeingWeakness)).when(beingWeaknessDao).findById(updatedKey);

	    Boolean result = beingWeaknessService.updateBeingWeakness(updatedBeingWeakness);

	    // Assert the response
	    Assertions.assertTrue(result, "The updated beingWeakness should not be null");
	
	}


}
