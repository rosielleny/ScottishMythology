package com.faction.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.faction.dao.FactionDao;
import com.faction.entity.Faction;
import com.faction.service.FactionService;

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
class FactionApplicationTests {

	@Test
	void contextLoads() {
	}
	
	//@Autowired
	//private FactionService factionService;
	@MockBean
	private FactionDao factionDao;
	@Autowired
    private MockMvc mockMvc;
	@Autowired
	private FactionService factionService;
	
	
	// Service Tests
	@Test
	@DisplayName("Test getting faction by ID")
	void testGetByID() {
		
		// Setting up mock repository
		Faction faction = new Faction(1, "TestFaction", "Description");
		doReturn(Optional.of(faction)).when(factionDao).findById(1);
		
		// Execute the service call
		Optional<Faction> returnedFaction = Optional.ofNullable(factionService.getFactionById(1));
		
		// Assert the response
		Assertions.assertTrue(returnedFaction.isPresent(), "Faction was not found");
		Assertions.assertSame(returnedFaction.get(), faction, "The faction returned was not the same as the mock");
		
	}

	@Test
	@DisplayName("Test Not Found get faction by ID")
	void testGetByIdNotFound() {
		// Setup our mock repository
		doReturn(Optional.empty()).when(factionDao).findById(1);

		// Execute the service call
		Optional<Faction> returnedFaction = Optional.ofNullable(factionService.getFactionById(1));

		// Assert the response
		Assertions.assertFalse(returnedFaction.isPresent(), "Faction should NOT be found, but was");
	}
	
	@Test
	@DisplayName("Test findAll")
	void testFindAll() {
		// Setup our mock repository
		Faction faction1 = new Faction(1, "Faction Name", "Description");
		Faction faction2 = new Faction(2, "Faction 2 Name", "Description");
		doReturn(Arrays.asList(faction1, faction2)).when(factionDao).findAll();
		when(factionDao.findAll()).thenReturn(Arrays.asList(faction1, faction2));

		// Execute the service call
		List<Faction> factions = factionService.getAllFaction();

		// Assert the response
		Assertions.assertEquals(2, factions.size(), "findAll should return 2 abilities");
	}
	
	@Test
	@DisplayName("Test create faction")
	void testCreate() {
	    // Setup our mock repository
	    Faction faction = new Faction(1, "Faction Name", "Description");

	    // Mocking behaviour for factionDao.save
	    doAnswer(invocation -> {
	        Faction argFaction = invocation.getArgument(0);
	        return new Faction(argFaction.getFactionPK() + 1, argFaction.getFactionName(), argFaction.getFactionDescription());
	    }).when(factionDao).save(any());

	    // Mocking behaviour for getFactionByName
	    when(factionDao.findByName(anyString())).thenReturn(new Faction(2, "Faction Name", "Description"));

	    // Execute the service call
	    Faction returnedFaction = factionService.createFaction(faction);

	    // Assert the response
	    Assertions.assertNotNull(returnedFaction, "The saved faction should not be null");
	    Assertions.assertEquals(2, returnedFaction.getFactionPK(), "The ID should be incremented");
	}


	
	@Test
	@DisplayName("Test delete faction")
	void testDelete() {
	    // Setup our mock repository
	    Faction faction = new Faction(1, "Faction Name", "Description");
	    
	    doReturn(Optional.of(faction)).when(factionDao).findById(1);

	    doNothing().when(factionDao).deleteById(1);

	    // Execute the service call
	    Boolean returnedFaction = factionService.deleteFaction(1);

	    // Assert the response
	    Assertions.assertNotNull(returnedFaction, "The deleted faction should not be null, should be boolean.");
	    
	    // After deletion, the faction should not be found.
	    doReturn(Optional.empty()).when(factionDao).findById(1);
	    Assertions.assertNull(factionService.getFactionById(1));

	    Assertions.assertEquals(true, returnedFaction, "Should return true if deleted.");
	}


	
	@Test
	@DisplayName("Test edit faction")
	void testEdit() {
	    // Setup our mock repository
	    Faction faction = new Faction(1, "Faction Name", "Description");
	    
	    doReturn(Optional.of(faction)).when(factionDao).findById(1);

	    // Execute the service call
	    Faction updatedFaction = new Faction(1, "Updated Faction Name", "Description");
	    factionService.updateFaction(updatedFaction);
	    
	    doReturn(Optional.of(updatedFaction)).when(factionDao).findById(1);
	    
	    Faction returnedFaction = factionService.getFactionById(1);

	    // Assert the response
	    Assertions.assertNotNull(returnedFaction, "The updated faction should not be null");
	    Assertions.assertEquals(1, returnedFaction.getFactionPK(), "The ID should be same as the one updated");
	    Assertions.assertEquals("Updated Faction Name", returnedFaction.getFactionName(), "The Name should be updated");
	}


}
