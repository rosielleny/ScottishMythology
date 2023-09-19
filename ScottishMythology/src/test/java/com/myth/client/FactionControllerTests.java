package com.myth.client;

import org.hamcrest.Matchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper; // Import ObjectMapper

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;

import org.springframework.web.servlet.ModelAndView;

import com.myth.controller.FactionController;
import com.myth.entity.Faction;
import com.myth.entity.composite.GenericEntity;

import com.myth.service.micro.FactionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest
@AutoConfigureMockMvc
class FactionControllerTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private MockMvc mockMvc;


	@MockBean
	private FactionService factionService;
	
	@Autowired
	private FactionController factionController;
	
	// CREATE
	@Test
	public void testCreateAnFaction() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/faction/create"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("entity/create-entity"))
				.andReturn();

		ModelAndView modelAndView = result.getModelAndView();

		Object entityAttribute = modelAndView.getModel().get("entity");
		Object entitiesAttribute = modelAndView.getModel().get("entities");

		Assertions.assertNotNull(entityAttribute, "The 'entity' attribute should not be null");
		Assertions.assertNotNull(entitiesAttribute, "The 'entities' attribute should not be null");
	}



	@Test
	@DisplayName("Test form validation failure")
	public void testFormValidationFailure() throws Exception {
		Faction createdFaction = new Faction();
		createdFaction.setFactionPK(1);
		createdFaction.setFactionName("New Faction");
		when(factionService.getFactionById(any(Integer.class))).thenReturn(createdFaction);
		when(factionService.createFaction(any(Faction.class))).thenReturn(createdFaction);
		when(factionService.getFactionByName(any(String.class))).thenReturn(createdFaction);

		mockMvc.perform(post("/faction/save-new")
				.param("entityName", "") // Invalid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attributeHasFieldErrors("entity", "entityName")) // Expect validation error
		.andExpect(view().name("entity/create-entity")); // Expect view name
	}

	@Test
	@DisplayName("Test successful faction creation")
	public void testSuccessfulFactionCreation() throws Exception {
		Faction createdFaction = new Faction();
		createdFaction.setFactionPK(1);
		createdFaction.setFactionName("New Faction");
		GenericEntity genEnt = new GenericEntity(createdFaction.getFactionPK(), createdFaction.getFactionName());
		when(factionService.getFactionById(any(Integer.class))).thenReturn(createdFaction);
		when(factionService.createFaction(any(Faction.class))).thenReturn(createdFaction);
		when(factionService.getFactionByName(any(String.class))).thenReturn(createdFaction);

		mockMvc.perform(post("/faction/save-new")
				.param("entityName", "New Faction") // Valid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attribute("message", "New Faction successfully added to the database.")) // Expect success message
		.andExpect(model().attribute("entity", samePropertyValuesAs(genEnt))) // Expect entity attribute to be updated
		.andExpect(view().name("entity/entity-output")); // Expect view name
	}

	@Test
	@DisplayName("Test error handling")
	public void testErrorHandling() throws Exception {
		// Mock an error during faction creation
		when(factionService.createFaction(any(Faction.class))).thenReturn(null);

		// Create a POST request with valid input
		mockMvc.perform(post("/faction/save-new")
				.param("entityName", "New Faction") // Valid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attribute("message", "An error occurred. Faction not added to the database.")) // Expect error message
		.andExpect(view().name("entity/entity-output")); // Expect view name
	}
	
	// READ
	
    @Test
    @DisplayName("Test show all factions")
    public void testShowAllFactions() throws Exception {
        // Mock your factionService to return a list of Faction objects
        List<Faction> factions = new ArrayList<>();
        Faction faction1 = new Faction(1, "Faction 1", "Description 1");
        Faction faction2 = new Faction(2, "Faction 2", "Description 2");
        factions.add(faction2);
        factions.add(faction1);
        
        when(factionService.getAllFaction()).thenReturn(factions);

        mockMvc.perform(get("/faction/factions"))
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-all-entities")) // Expect view name
            .andExpect(model().attributeExists("entityList")) // Expect model attribute
            .andExpect(model().attribute("entityList", Matchers.hasSize(2))); // Expect entityList size to match the number of factions
    }
    
    @Test
    @DisplayName("Test search factions by name")
    public void testSearchFactionsByName() throws Exception {
        Faction mockFaction = new Faction();
        mockFaction.setFactionPK(1);
        mockFaction.setFactionName("Mocked Faction");
        
        when(factionService.getFactionByName("ExistingFaction")).thenReturn(mockFaction);
        when(factionService.getFactionByName("NonExistingFaction")).thenReturn(null);
        
        mockMvc.perform(get("/faction/search")
                .param("name", "ExistingFaction")) // Valid input for an existing faction
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-entity")) // Expect view name
            .andExpect(model().attributeExists("entity")) // Expect model attribute
            .andExpect(model().attribute("entity", Matchers.hasProperty("entityName", is("Mocked Faction")))); // Expect entity attribute

        mockMvc.perform(get("/faction/search")
                .param("name", "NonExistingFaction")) // Valid input for a non-existing faction
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-entity")) // Expect view name
            .andExpect(model().attribute("message", is("No factions were found for NonExistingFaction"))); // Expect message attribute
    }
    
    // UPDATE
    
    @Test
    @DisplayName("Test edit factions by ID")
    public void testEditFactionsById() throws Exception {
        Faction mockFaction = new Faction();
        mockFaction.setFactionPK(1);
        mockFaction.setFactionName("Mocked Faction");
        
        when(factionService.getFactionById(1)).thenReturn(mockFaction);
        
        mockMvc.perform(get("/faction/edit")
                .param("pk", "1")) // Valid input for an existing faction
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/edit-entity")) // Expect view name
            .andExpect(model().attributeExists("entity")) // Expect model attribute
            .andExpect(model().attributeExists("ogEntity")) // Expect model attribute
            .andExpect(model().attribute("entity", Matchers.hasProperty("entityName", is("Mocked Faction")))); // Expect entity attribute
    }
    
    // DELETE
    
    @Test
    @DisplayName("Test successful faction deletion")
    public void testSuccessfulFactionDeletion() throws Exception {
        int factionIdToDelete = 1;
        Faction deletedFaction = new Faction();
        deletedFaction.setFactionPK(factionIdToDelete);
        deletedFaction.setFactionName("Deleted Faction");
        List<Faction> factions = new ArrayList<>();
        Faction faction1 = new Faction(3, "Faction 3", "Description 3");
        Faction faction2 = new Faction(2, "Faction 2", "Description 2");
        factions.add(faction2);
        factions.add(faction1);

        when(factionService.getFactionById(factionIdToDelete)).thenReturn(deletedFaction);
        when(factionService.deleteFaction(factionIdToDelete)).thenReturn(true);
        when(factionService.getAllFaction()).thenReturn(factions);


        mockMvc.perform(get("/faction/delete/{pk}", factionIdToDelete))
            .andExpect(status().isOk())
            .andExpect(model().attribute("message", "Deleted Faction successfully deleted from Factions."))
            .andExpect(view().name("entity/show-all-entities"));

        verify(factionService, times(1)).getFactionById(factionIdToDelete);
        verify(factionService, times(1)).deleteFaction(factionIdToDelete);
    }

    @Test
    @DisplayName("Test unsuccessful faction deletion")
    public void testUnsuccessfulFactionDeletion() throws Exception {
        int factionIdToDelete = 1;
        Faction factionToDelete = new Faction();
        factionToDelete.setFactionPK(factionIdToDelete);
        factionToDelete.setFactionName("Deletion Failure");
        List<Faction> factions = new ArrayList<>();
        Faction faction1 = new Faction(3, "Faction 3", "Description 3");
        Faction faction2 = new Faction(2, "Faction 2", "Description 2");
        factions.add(faction2);
        factions.add(faction1);
        factions.add(factionToDelete);
        

        when(factionService.getFactionById(factionIdToDelete)).thenReturn(factionToDelete);
        when(factionService.deleteFaction(factionIdToDelete)).thenReturn(false);
        when(factionService.getAllFaction()).thenReturn(factions);

        mockMvc.perform(get("/faction/delete/{pk}", factionIdToDelete))
            .andExpect(status().isOk())
            .andExpect(model().attribute("message", "Failed to delete Deletion Failure from Factions."))
            .andExpect(view().name("entity/show-all-entities"));

        // Verify that the service method was called with the correct parameter
        verify(factionService, times(1)).getFactionById(factionIdToDelete);
        verify(factionService, times(1)).deleteFaction(factionIdToDelete);
    }

    // EXTRA

    @Test
    public void testSneakyAddFaction_Success() {
        // Create a sample Faction object for testing
        Faction faction = new Faction();
        faction.setFactionName("Test Faction");

        when(factionService.createFaction(faction)).thenReturn(faction);

        // Make a POST request to the endpoint
        ResponseEntity<Map<String, Boolean>> responseEntity = factionController.sneakyAddFaction(faction);

        // Verify that the response is as expected
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().get("success"));
    }

    @Test
    public void testSneakyAddFaction_Failure() {
        // Create a sample Faction object for testing
        Faction faction = new Faction();
        faction.setFactionName("Test Faction");

        when(factionService.createFaction(faction)).thenReturn(null);

        // Make a POST request to the endpoint
        ResponseEntity<Map<String, Boolean>> responseEntity = factionController.sneakyAddFaction(faction);

        // Verify that the response is as expected
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertFalse(responseEntity.getBody().get("success"));
    }
    
    public static String asJsonString(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
