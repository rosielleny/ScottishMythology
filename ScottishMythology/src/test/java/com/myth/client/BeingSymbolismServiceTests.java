package com.myth.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.myth.dao.junction.BeingSymbolismDao;
import com.myth.entity.junction.BeingSymbolism;
import com.myth.entity.junction.KeyBeingSymbolism;
import com.myth.service.junction.BeingSymbolismService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
class BeingSymbolismServiceTests {

	@Test
	void contextLoads() {
	}
	
	KeyBeingSymbolism kBA = new KeyBeingSymbolism(1, 1);
	KeyBeingSymbolism kBA2 = new KeyBeingSymbolism(1, 2);
	BeingSymbolism beingSymbolism = new BeingSymbolism(kBA);
	BeingSymbolism beingSymbolism2 = new BeingSymbolism(kBA2);
	
	//@Autowired
	//private BeingSymbolismService beingSymbolismService;
	@MockBean
	private BeingSymbolismDao beingSymbolismDao;
	@Autowired
	private BeingSymbolismService beingSymbolismService;
	
	
	// Service Tests
	@Test
	@DisplayName("Test getting beingSymbolism by ID")
	void testGetByID() {
		
		// Setting up mock repository
		
		doReturn(Optional.of(beingSymbolism)).when(beingSymbolismDao).findById(kBA);
		
		// Execute the service call
		Optional<BeingSymbolism> returnedBeingSymbolism = Optional.ofNullable(beingSymbolismService.getBeingSymbolismById(kBA));
		
		// Assert the response
		Assertions.assertTrue(returnedBeingSymbolism.isPresent(), "BeingSymbolism was not found");
		Assertions.assertSame(returnedBeingSymbolism.get(), beingSymbolism, "The beingSymbolism returned was not the same as the mock");
		
	}

	@Test
	@DisplayName("Test Not Found get beingSymbolism by ID")
	void testGetByIdNotFound() {
		// Setup our mock repository
		doReturn(Optional.empty()).when(beingSymbolismDao).findById(kBA);

		// Execute the service call
		Optional<BeingSymbolism> returnedBeingSymbolism = Optional.ofNullable(beingSymbolismService.getBeingSymbolismById(kBA));

		// Assert the response
		Assertions.assertFalse(returnedBeingSymbolism.isPresent(), "BeingSymbolism should NOT be found, but was");
	}
	
	@Test
	@DisplayName("Test findAll")
	void testFindAll() {
		
		doReturn(Arrays.asList(beingSymbolism, beingSymbolism2)).when(beingSymbolismDao).findAll();
		

		// Execute the service call
		List<BeingSymbolism> beingSymbolisms = beingSymbolismService.getAllBeingSymbolism();

		// Assert the response
		Assertions.assertEquals(2, beingSymbolisms.size(), "findAll should return 2 abilities");
	}
	
	@Test
	@DisplayName("Test create beingSymbolism")
	void testCreate() {
	    // Setup our mock repository

	   when(beingSymbolismDao.save(beingSymbolism)).thenReturn(beingSymbolism);

	
	    // Execute the service call
	    BeingSymbolism returnedBeingSymbolism = beingSymbolismService.createBeingSymbolism(beingSymbolism);

	    // Assert the response
	    Assertions.assertNotNull(returnedBeingSymbolism, "The saved beingSymbolism should not be null");
	   
	}


	
	@Test
	@DisplayName("Test delete beingSymbolism")
	void testDelete() {
	    // Setup our mock repository
	    
	    doReturn(Optional.of(beingSymbolism)).when(beingSymbolismDao).findById(kBA);

	    doNothing().when(beingSymbolismDao).deleteById(kBA);

	    // Execute the service call
	    Boolean returnedBeingSymbolism = beingSymbolismService.deleteBeingSymbolism(kBA);

	    // Assert the response
	    Assertions.assertNotNull(returnedBeingSymbolism, "The deleted beingSymbolism should not be null, should be boolean.");
	    
	    // After deletion, the beingSymbolism should not be found.
	    doReturn(Optional.empty()).when(beingSymbolismDao).findById(kBA);
	    Assertions.assertNull(beingSymbolismService.getBeingSymbolismById(kBA));

	    Assertions.assertEquals(true, returnedBeingSymbolism, "Should return true if deleted.");
	}


	
	@Test
	@DisplayName("Test edit beingSymbolism")
	void testEdit() {
	   
		KeyBeingSymbolism updatedKey = new KeyBeingSymbolism(1, 3);
	    BeingSymbolism updatedBeingSymbolism = new BeingSymbolism(updatedKey);
	    
	    when(beingSymbolismDao.save(beingSymbolism)).thenReturn(beingSymbolism);
	    when(beingSymbolismDao.findById(kBA)).thenReturn(Optional.of(beingSymbolism));
	    doReturn(Optional.of(updatedBeingSymbolism)).when(beingSymbolismDao).findById(updatedKey);

	    Boolean result = beingSymbolismService.updateBeingSymbolism(updatedBeingSymbolism);

	    // Assert the response
	    Assertions.assertTrue(result, "The updated beingSymbolism should not be null");
	
	}


}
