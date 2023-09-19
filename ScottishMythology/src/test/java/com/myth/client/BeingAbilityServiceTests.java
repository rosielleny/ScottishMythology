package com.myth.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.myth.dao.junction.BeingAbilityDao;
import com.myth.entity.junction.BeingAbility;
import com.myth.entity.junction.KeyBeingAbility;
import com.myth.service.junction.BeingAbilityService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
class BeingAbilityServiceTests {

	@Test
	void contextLoads() {
	}
	
	KeyBeingAbility kBA = new KeyBeingAbility(1, 1);
	KeyBeingAbility kBA2 = new KeyBeingAbility(1, 2);
	BeingAbility beingAbility = new BeingAbility(kBA);
	BeingAbility beingAbility2 = new BeingAbility(kBA2);
	
	//@Autowired
	//private BeingAbilityService beingAbilityService;
	@MockBean
	private BeingAbilityDao beingAbilityDao;
	@Autowired
	private BeingAbilityService beingAbilityService;
	
	
	// Service Tests
	@Test
	@DisplayName("Test getting beingAbility by ID")
	void testGetByID() {
		
		// Setting up mock repository
		
		doReturn(Optional.of(beingAbility)).when(beingAbilityDao).findById(kBA);
		
		// Execute the service call
		Optional<BeingAbility> returnedBeingAbility = Optional.ofNullable(beingAbilityService.getBeingAbilityById(kBA));
		
		// Assert the response
		Assertions.assertTrue(returnedBeingAbility.isPresent(), "BeingAbility was not found");
		Assertions.assertSame(returnedBeingAbility.get(), beingAbility, "The beingAbility returned was not the same as the mock");
		
	}

	@Test
	@DisplayName("Test Not Found get beingAbility by ID")
	void testGetByIdNotFound() {
		// Setup our mock repository
		doReturn(Optional.empty()).when(beingAbilityDao).findById(kBA);

		// Execute the service call
		Optional<BeingAbility> returnedBeingAbility = Optional.ofNullable(beingAbilityService.getBeingAbilityById(kBA));

		// Assert the response
		Assertions.assertFalse(returnedBeingAbility.isPresent(), "BeingAbility should NOT be found, but was");
	}
	
	@Test
	@DisplayName("Test findAll")
	void testFindAll() {
		
		doReturn(Arrays.asList(beingAbility, beingAbility2)).when(beingAbilityDao).findAll();
		

		// Execute the service call
		List<BeingAbility> beingAbilitys = beingAbilityService.getAllBeingAbility();

		// Assert the response
		Assertions.assertEquals(2, beingAbilitys.size(), "findAll should return 2 abilities");
	}
	
	@Test
	@DisplayName("Test create beingAbility")
	void testCreate() {
	    // Setup our mock repository

	   when(beingAbilityDao.save(beingAbility)).thenReturn(beingAbility);

	
	    // Execute the service call
	    BeingAbility returnedBeingAbility = beingAbilityService.createBeingAbility(beingAbility);

	    // Assert the response
	    Assertions.assertNotNull(returnedBeingAbility, "The saved beingAbility should not be null");
	   
	}


	
	@Test
	@DisplayName("Test delete beingAbility")
	void testDelete() {
	    // Setup our mock repository
	    
	    doReturn(Optional.of(beingAbility)).when(beingAbilityDao).findById(kBA);

	    doNothing().when(beingAbilityDao).deleteById(kBA);

	    // Execute the service call
	    Boolean returnedBeingAbility = beingAbilityService.deleteBeingAbility(kBA);

	    // Assert the response
	    Assertions.assertNotNull(returnedBeingAbility, "The deleted beingAbility should not be null, should be boolean.");
	    
	    // After deletion, the beingAbility should not be found.
	    doReturn(Optional.empty()).when(beingAbilityDao).findById(kBA);
	    Assertions.assertNull(beingAbilityService.getBeingAbilityById(kBA));

	    Assertions.assertEquals(true, returnedBeingAbility, "Should return true if deleted.");
	}


	
	@Test
	@DisplayName("Test edit beingAbility")
	void testEdit() {
	   
		KeyBeingAbility updatedKey = new KeyBeingAbility(1, 3);
	    BeingAbility updatedBeingAbility = new BeingAbility(updatedKey);
	    
	    when(beingAbilityDao.save(beingAbility)).thenReturn(beingAbility);
	    when(beingAbilityDao.findById(kBA)).thenReturn(Optional.of(beingAbility));
	    doReturn(Optional.of(updatedBeingAbility)).when(beingAbilityDao).findById(updatedKey);

	    Boolean result = beingAbilityService.updateBeingAbility(updatedBeingAbility);

	    // Assert the response
	    Assertions.assertTrue(result, "The updated beingAbility should not be null");
	
	}


}
