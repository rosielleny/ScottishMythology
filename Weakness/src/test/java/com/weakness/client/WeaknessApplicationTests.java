package com.weakness.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.weakness.dao.WeaknessDao;
import com.weakness.entity.Weakness;
import com.weakness.service.WeaknessService;

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
class WeaknessApplicationTests {

	@Test
	void contextLoads() {
	}
	
	//@Autowired
	//private WeaknessService weaknessService;
	@MockBean
	private WeaknessDao weaknessDao;
	
	@Autowired
	private WeaknessService weaknessService;
	
	
	// Service Tests
	@Test
	@DisplayName("Test getting weakness by ID")
	void testGetByID() {
		
		// Setting up mock repository
		Weakness weakness = new Weakness(1, "TestWeakness");
		doReturn(Optional.of(weakness)).when(weaknessDao).findById(1);
		
		// Execute the service call
		Optional<Weakness> returnedWeakness = Optional.ofNullable(weaknessService.getWeaknessById(1));
		
		// Assert the response
		Assertions.assertTrue(returnedWeakness.isPresent(), "Weakness was not found");
		Assertions.assertSame(returnedWeakness.get(), weakness, "The weakness returned was not the same as the mock");
		
	}

	@Test
	@DisplayName("Test Not Found get weakness by ID")
	void testGetByIdNotFound() {
		// Setup our mock repository
		doReturn(Optional.empty()).when(weaknessDao).findById(1);

		// Execute the service call
		Optional<Weakness> returnedWeakness = Optional.ofNullable(weaknessService.getWeaknessById(1));

		// Assert the response
		Assertions.assertFalse(returnedWeakness.isPresent(), "Weakness should NOT be found, but was");
	}
	
	@Test
	@DisplayName("Test findAll")
	void testFindAll() {
		// Setup our mock repository
		Weakness weakness1 = new Weakness(1, "Weakness Name");
		Weakness weakness2 = new Weakness(2, "Weakness 2 Name");
		doReturn(Arrays.asList(weakness1, weakness2)).when(weaknessDao).findAll();
		when(weaknessDao.findAll()).thenReturn(Arrays.asList(weakness1, weakness2));

		// Execute the service call
		List<Weakness> weaknesss = weaknessService.getAllWeakness();

		// Assert the response
		Assertions.assertEquals(2, weaknesss.size(), "findAll should return 2 abilities");
	}
	
	@Test
	@DisplayName("Test create weakness")
	void testCreate() {
	    // Setup our mock repository
	    Weakness weakness = new Weakness(1, "Weakness Name");

	    // Mocking behaviour for weaknessDao.save
	    doAnswer(invocation -> {
	        Weakness argWeakness = invocation.getArgument(0);
	        return new Weakness(argWeakness.getWeaknessPK() + 1, argWeakness.getWeaknessName());
	    }).when(weaknessDao).save(any());

	    // Mocking behaviour for getWeaknessByName
	    when(weaknessDao.findByName(anyString())).thenReturn(new Weakness(2, "Weakness Name"));

	    // Execute the service call
	    Weakness returnedWeakness = weaknessService.createWeakness(weakness);

	    // Assert the response
	    Assertions.assertNotNull(returnedWeakness, "The saved weakness should not be null");
	    Assertions.assertEquals(2, returnedWeakness.getWeaknessPK(), "The ID should be incremented");
	}


	
	@Test
	@DisplayName("Test delete weakness")
	void testDelete() {
	    // Setup our mock repository
	    Weakness weakness = new Weakness(1, "Weakness Name");
	    
	    doReturn(Optional.of(weakness)).when(weaknessDao).findById(1);

	    doNothing().when(weaknessDao).deleteById(1);

	    // Execute the service call
	    Boolean returnedWeakness = weaknessService.deleteWeakness(1);

	    // Assert the response
	    Assertions.assertNotNull(returnedWeakness, "The deleted weakness should not be null, should be boolean.");
	    
	    // After deletion, the weakness should not be found.
	    doReturn(Optional.empty()).when(weaknessDao).findById(1);
	    Assertions.assertNull(weaknessService.getWeaknessById(1));

	    Assertions.assertEquals(true, returnedWeakness, "Should return true if deleted.");
	}


	
	@Test
	@DisplayName("Test edit weakness")
	void testEdit() {
	    // Setup our mock repository
	    Weakness weakness = new Weakness(1, "Weakness Name");
	    
	    doReturn(Optional.of(weakness)).when(weaknessDao).findById(1);

	    // Execute the service call
	    Weakness updatedWeakness = new Weakness(1, "Updated Weakness Name");
	    weaknessService.updateWeakness(updatedWeakness);
	    
	    doReturn(Optional.of(updatedWeakness)).when(weaknessDao).findById(1);
	    
	    Weakness returnedWeakness = weaknessService.getWeaknessById(1);

	    // Assert the response
	    Assertions.assertNotNull(returnedWeakness, "The updated weakness should not be null");
	    Assertions.assertEquals(1, returnedWeakness.getWeaknessPK(), "The ID should be same as the one updated");
	    Assertions.assertEquals("Updated Weakness Name", returnedWeakness.getWeaknessName(), "The Name should be updated");
	}


}
