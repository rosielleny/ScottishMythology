package com.myth.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.myth.dao.junction.BeingStoryDao;
import com.myth.entity.junction.BeingStory;
import com.myth.entity.junction.KeyBeingStory;
import com.myth.service.junction.BeingStoryService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
class BeingStoryServiceTests {

	@Test
	void contextLoads() {
	}
	
	KeyBeingStory kBA = new KeyBeingStory(1, 1);
	KeyBeingStory kBA2 = new KeyBeingStory(1, 2);
	BeingStory beingStory = new BeingStory(kBA);
	BeingStory beingStory2 = new BeingStory(kBA2);
	
	//@Autowired
	//private BeingStoryService beingStoryService;
	@MockBean
	private BeingStoryDao beingStoryDao;
	@Autowired
	private BeingStoryService beingStoryService;
	
	
	// Service Tests
	@Test
	@DisplayName("Test getting beingStory by ID")
	void testGetByID() {
		
		// Setting up mock repository
		
		doReturn(Optional.of(beingStory)).when(beingStoryDao).findById(kBA);
		
		// Execute the service call
		Optional<BeingStory> returnedBeingStory = Optional.ofNullable(beingStoryService.getBeingStoryById(kBA));
		
		// Assert the response
		Assertions.assertTrue(returnedBeingStory.isPresent(), "BeingStory was not found");
		Assertions.assertSame(returnedBeingStory.get(), beingStory, "The beingStory returned was not the same as the mock");
		
	}

	@Test
	@DisplayName("Test Not Found get beingStory by ID")
	void testGetByIdNotFound() {
		// Setup our mock repository
		doReturn(Optional.empty()).when(beingStoryDao).findById(kBA);

		// Execute the service call
		Optional<BeingStory> returnedBeingStory = Optional.ofNullable(beingStoryService.getBeingStoryById(kBA));

		// Assert the response
		Assertions.assertFalse(returnedBeingStory.isPresent(), "BeingStory should NOT be found, but was");
	}
	
	@Test
	@DisplayName("Test findAll")
	void testFindAll() {
		
		doReturn(Arrays.asList(beingStory, beingStory2)).when(beingStoryDao).findAll();
		

		// Execute the service call
		List<BeingStory> beingStorys = beingStoryService.getAllBeingStory();

		// Assert the response
		Assertions.assertEquals(2, beingStorys.size(), "findAll should return 2 abilities");
	}
	
	@Test
	@DisplayName("Test create beingStory")
	void testCreate() {
	    // Setup our mock repository

	   when(beingStoryDao.save(beingStory)).thenReturn(beingStory);

	
	    // Execute the service call
	    BeingStory returnedBeingStory = beingStoryService.createBeingStory(beingStory);

	    // Assert the response
	    Assertions.assertNotNull(returnedBeingStory, "The saved beingStory should not be null");
	   
	}


	
	@Test
	@DisplayName("Test delete beingStory")
	void testDelete() {
	    // Setup our mock repository
	    
	    doReturn(Optional.of(beingStory)).when(beingStoryDao).findById(kBA);

	    doNothing().when(beingStoryDao).deleteById(kBA);

	    // Execute the service call
	    Boolean returnedBeingStory = beingStoryService.deleteBeingStory(kBA);

	    // Assert the response
	    Assertions.assertNotNull(returnedBeingStory, "The deleted beingStory should not be null, should be boolean.");
	    
	    // After deletion, the beingStory should not be found.
	    doReturn(Optional.empty()).when(beingStoryDao).findById(kBA);
	    Assertions.assertNull(beingStoryService.getBeingStoryById(kBA));

	    Assertions.assertEquals(true, returnedBeingStory, "Should return true if deleted.");
	}


	
	@Test
	@DisplayName("Test edit beingStory")
	void testEdit() {
	   
		KeyBeingStory updatedKey = new KeyBeingStory(1, 3);
	    BeingStory updatedBeingStory = new BeingStory(updatedKey);
	    
	    when(beingStoryDao.save(beingStory)).thenReturn(beingStory);
	    when(beingStoryDao.findById(kBA)).thenReturn(Optional.of(beingStory));
	    doReturn(Optional.of(updatedBeingStory)).when(beingStoryDao).findById(updatedKey);

	    Boolean result = beingStoryService.updateBeingStory(updatedBeingStory);

	    // Assert the response
	    Assertions.assertTrue(result, "The updated beingStory should not be null");
	
	}


}
