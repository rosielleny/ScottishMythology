package com.myth.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.myth.dao.BeingDao;
import com.myth.entity.Being;
import com.myth.service.BeingService;

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
class BeingServiceTests {

	@Test
	void contextLoads() {
	}
	
	//@Autowired
	//private BeingService beingService;
	@MockBean
	private BeingDao beingDao;
	@Autowired
    private MockMvc mockMvc;
	@Autowired
	private BeingService beingService;
	
	
	// Service Tests
	@Test
	@DisplayName("Test getting being by ID")
	void testGetByID() {
		
		// Setting up mock repository
		Being being = new Being(1, "TestBeing", 1, "Test", 1, null, 1);
		doReturn(Optional.of(being)).when(beingDao).findById(1);
		
		// Execute the service call
		Optional<Being> returnedBeing = Optional.ofNullable(beingService.getBeingById(1));
		
		// Assert the response
		Assertions.assertTrue(returnedBeing.isPresent(), "Being was not found");
		Assertions.assertSame(returnedBeing.get(), being, "The being returned was not the same as the mock");
		
	}

	@Test
	@DisplayName("Test Not Found get being by ID")
	void testGetByIdNotFound() {
		// Setup our mock repository
		doReturn(Optional.empty()).when(beingDao).findById(1);

		// Execute the service call
		Optional<Being> returnedBeing = Optional.ofNullable(beingService.getBeingById(1));

		// Assert the response
		Assertions.assertFalse(returnedBeing.isPresent(), "Being should NOT be found, but was");
	}
	
	@Test
	@DisplayName("Test findAll")
	void testFindAll() {
		// Setup our mock repository
		Being being1 = new Being(1, "Being Name", 1, "Test", 1, null, 1);
		Being being2 = new Being(2, "Being 2 Name", 2, "Test 2", 2, null, 2);
		doReturn(Arrays.asList(being1, being2)).when(beingDao).findAll();
		

		// Execute the service call
		List<Being> beings = beingService.getAllBeing();

		// Assert the response
		Assertions.assertEquals(2, beings.size(), "findAll should return 2 abilities");
	}
	
	@Test
	@DisplayName("Test create being")
	void testCreate() {
	    // Setup our mock repository
	    Being being = new Being(1, "Being Name", 1, "Test", 1, null, 1);

	    // Mocking behaviour for beingDao.save
	    doAnswer(invocation -> {
	        Being argBeing = invocation.getArgument(0);
	        return new Being(argBeing.getBeingPK() + 1, argBeing.getBeingName(), argBeing.getBeingSpecies(), argBeing.getBeingDescription(), argBeing.getBeingGender(), argBeing.getBeingArt(), argBeing.getBeingFaction());
	    }).when(beingDao).save(any());

	    // Mocking behaviour for getBeingByName
	    when(beingDao.findByName(anyString())).thenReturn(new Being(2, "Being Name", 1, "Test", 1, null, 1));

	    // Execute the service call
	    Being returnedBeing = beingService.createBeing(being);

	    // Assert the response
	    Assertions.assertNotNull(returnedBeing, "The saved being should not be null");
	    Assertions.assertEquals(2, returnedBeing.getBeingPK(), "The ID should be incremented");
	}


	
	@Test
	@DisplayName("Test delete being")
	void testDelete() {
	    // Setup our mock repository
	    Being being = new Being(1, "Being Name", 1, "Test", 1, null, 1);
	    
	    doReturn(Optional.of(being)).when(beingDao).findById(1);

	    doNothing().when(beingDao).deleteById(1);

	    // Execute the service call
	    Boolean returnedBeing = beingService.deleteBeing(1);

	    // Assert the response
	    Assertions.assertNotNull(returnedBeing, "The deleted being should not be null, should be boolean.");
	    
	    // After deletion, the being should not be found.
	    doReturn(Optional.empty()).when(beingDao).findById(1);
	    Assertions.assertNull(beingService.getBeingById(1));

	    Assertions.assertEquals(true, returnedBeing, "Should return true if deleted.");
	}


	
	@Test
	@DisplayName("Test edit being")
	void testEdit() {
	    // Setup our mock repository
	    Being being = new Being(1, "Being Name", 1, "Test", 1, null, 1);
	    
	    doReturn(Optional.of(being)).when(beingDao).findById(1);

	    // Execute the service call
	    Being updatedBeing = new Being(1, "Updated Being Name", 1, "Test", 1, null, 1);
	    beingService.updateBeing(updatedBeing);
	    
	    doReturn(Optional.of(updatedBeing)).when(beingDao).findById(1);
	    
	    Being returnedBeing = beingService.getBeingById(1);

	    // Assert the response
	    Assertions.assertNotNull(returnedBeing, "The updated being should not be null");
	    Assertions.assertEquals(1, returnedBeing.getBeingPK(), "The ID should be same as the one updated");
	    Assertions.assertEquals("Updated Being Name", returnedBeing.getBeingName(), "The Name should be updated");
	}


}
