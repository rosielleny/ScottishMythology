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

import com.myth.controller.SpeciesController;
import com.myth.entity.Species;
import com.myth.entity.composite.GenericEntity;

import com.myth.service.micro.SpeciesService;

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
class SpeciesControllerTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private MockMvc mockMvc;


	@MockBean
	private SpeciesService speciesService;
	
	@Autowired
	private SpeciesController speciesController;
	
	// CREATE
	@Test
	public void testCreateAnSpecies() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/species/create"))
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
		Species createdSpecies = new Species();
		createdSpecies.setSpeciesPK(1);
		createdSpecies.setSpeciesName("New Species");
		when(speciesService.getSpeciesById(any(Integer.class))).thenReturn(createdSpecies);
		when(speciesService.createSpecies(any(Species.class))).thenReturn(createdSpecies);
		when(speciesService.getSpeciesByName(any(String.class))).thenReturn(createdSpecies);

		mockMvc.perform(post("/species/save-new")
				.param("entityName", "") // Invalid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attributeHasFieldErrors("entity", "entityName")) // Expect validation error
		.andExpect(view().name("entity/create-entity")); // Expect view name
	}

	@Test
	@DisplayName("Test successful species creation")
	public void testSuccessfulSpeciesCreation() throws Exception {
		Species createdSpecies = new Species();
		createdSpecies.setSpeciesPK(1);
		createdSpecies.setSpeciesName("New Species");
		GenericEntity genEnt = new GenericEntity(createdSpecies.getSpeciesPK(), createdSpecies.getSpeciesName());
		when(speciesService.getSpeciesById(any(Integer.class))).thenReturn(createdSpecies);
		when(speciesService.createSpecies(any(Species.class))).thenReturn(createdSpecies);
		when(speciesService.getSpeciesByName(any(String.class))).thenReturn(createdSpecies);

		mockMvc.perform(post("/species/save-new")
				.param("entityName", "New Species") // Valid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attribute("message", "New Species successfully added to the database.")) // Expect success message
		.andExpect(model().attribute("entity", samePropertyValuesAs(genEnt))) // Expect entity attribute to be updated
		.andExpect(view().name("entity/entity-output")); // Expect view name
	}

	@Test
	@DisplayName("Test error handling")
	public void testErrorHandling() throws Exception {
		// Mock an error during species creation
		when(speciesService.createSpecies(any(Species.class))).thenReturn(null);

		// Create a POST request with valid input
		mockMvc.perform(post("/species/save-new")
				.param("entityName", "New Species") // Valid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attribute("message", "An error occurred. Species not added to the database.")) // Expect error message
		.andExpect(view().name("entity/entity-output")); // Expect view name
	}
	
	// READ
	
    @Test
    @DisplayName("Test show all species")
    public void testShowAllSpecies() throws Exception {
        // Mock your speciesService to return a list of Species objects
        List<Species> species = new ArrayList<>();
        Species species1 = new Species(1, "Species 1", "Description 1");
        Species species2 = new Species(2, "Species 2", "Description 2");
        species.add(species2);
        species.add(species1);
        
        when(speciesService.getAllSpecies()).thenReturn(species);

        mockMvc.perform(get("/species/species"))
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-all-entities")) // Expect view name
            .andExpect(model().attributeExists("entityList")) // Expect model attribute
            .andExpect(model().attribute("entityList", Matchers.hasSize(2))); // Expect entityList size to match the number of species
    }
    
    @Test
    @DisplayName("Test search species by name")
    public void testSearchSpeciesByName() throws Exception {
        Species mockSpecies = new Species();
        mockSpecies.setSpeciesPK(1);
        mockSpecies.setSpeciesName("Mocked Species");
        
        when(speciesService.getSpeciesByName("ExistingSpecies")).thenReturn(mockSpecies);
        when(speciesService.getSpeciesByName("NonExistingSpecies")).thenReturn(null);
        
        mockMvc.perform(get("/species/search")
                .param("name", "ExistingSpecies")) // Valid input for an existing species
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-entity")) // Expect view name
            .andExpect(model().attributeExists("entity")) // Expect model attribute
            .andExpect(model().attribute("entity", Matchers.hasProperty("entityName", is("Mocked Species")))); // Expect entity attribute

        mockMvc.perform(get("/species/search")
                .param("name", "NonExistingSpecies")) // Valid input for a non-existing species
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-entity")) // Expect view name
            .andExpect(model().attribute("message", is("No species were found for NonExistingSpecies"))); // Expect message attribute
    }
    
    // UPDATE
    
    @Test
    @DisplayName("Test edit species by ID")
    public void testEditSpeciesById() throws Exception {
        Species mockSpecies = new Species();
        mockSpecies.setSpeciesPK(1);
        mockSpecies.setSpeciesName("Mocked Species");
        
        when(speciesService.getSpeciesById(1)).thenReturn(mockSpecies);
        
        mockMvc.perform(get("/species/edit")
                .param("pk", "1")) // Valid input for an existing species
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/edit-entity")) // Expect view name
            .andExpect(model().attributeExists("entity")) // Expect model attribute
            .andExpect(model().attributeExists("ogEntity")) // Expect model attribute
            .andExpect(model().attribute("entity", Matchers.hasProperty("entityName", is("Mocked Species")))); // Expect entity attribute
    }
    
    // DELETE
    
    @Test
    @DisplayName("Test successful species deletion")
    public void testSuccessfulSpeciesDeletion() throws Exception {
        int speciesIdToDelete = 1;
        Species deletedSpecies = new Species();
        deletedSpecies.setSpeciesPK(speciesIdToDelete);
        deletedSpecies.setSpeciesName("Deleted Species");
        List<Species> species = new ArrayList<>();
        Species species1 = new Species(3, "Species 3", "Description 3");
        Species species2 = new Species(2, "Species 2", "Description 2");
        species.add(species2);
        species.add(species1);

        when(speciesService.getSpeciesById(speciesIdToDelete)).thenReturn(deletedSpecies);
        when(speciesService.deleteSpecies(speciesIdToDelete)).thenReturn(true);
        when(speciesService.getAllSpecies()).thenReturn(species);


        mockMvc.perform(get("/species/delete/{pk}", speciesIdToDelete))
            .andExpect(status().isOk())
            .andExpect(model().attribute("message", "Deleted Species successfully deleted from Species."))
            .andExpect(view().name("entity/show-all-entities"));

        verify(speciesService, times(1)).getSpeciesById(speciesIdToDelete);
        verify(speciesService, times(1)).deleteSpecies(speciesIdToDelete);
    }

    @Test
    @DisplayName("Test unsuccessful species deletion")
    public void testUnsuccessfulSpeciesDeletion() throws Exception {
        int speciesIdToDelete = 1;
        Species speciesToDelete = new Species();
        speciesToDelete.setSpeciesPK(speciesIdToDelete);
        speciesToDelete.setSpeciesName("Deletion Failure");
        List<Species> species = new ArrayList<>();
        Species species1 = new Species(3, "Species 3", "Description 3");
        Species species2 = new Species(2, "Species 2", "Description 2");
        species.add(species2);
        species.add(species1);
        species.add(speciesToDelete);
        

        when(speciesService.getSpeciesById(speciesIdToDelete)).thenReturn(speciesToDelete);
        when(speciesService.deleteSpecies(speciesIdToDelete)).thenReturn(false);
        when(speciesService.getAllSpecies()).thenReturn(species);

        mockMvc.perform(get("/species/delete/{pk}", speciesIdToDelete))
            .andExpect(status().isOk())
            .andExpect(model().attribute("message", "Failed to delete Deletion Failure from Species."))
            .andExpect(view().name("entity/show-all-entities"));

        // Verify that the service method was called with the correct parameter
        verify(speciesService, times(1)).getSpeciesById(speciesIdToDelete);
        verify(speciesService, times(1)).deleteSpecies(speciesIdToDelete);
    }

    // EXTRA

    @Test
    public void testSneakyAddSpecies_Success() {
        // Create a sample Species object for testing
        Species species = new Species();
        species.setSpeciesName("Test Species");

        when(speciesService.createSpecies(species)).thenReturn(species);

        // Make a POST request to the endpoint
        ResponseEntity<Map<String, Boolean>> responseEntity = speciesController.sneakyAddSpecies(species);

        // Verify that the response is as expected
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().get("success"));
    }

    @Test
    public void testSneakyAddSpecies_Failure() {
        // Create a sample Species object for testing
        Species species = new Species();
        species.setSpeciesName("Test Species");

        when(speciesService.createSpecies(species)).thenReturn(null);

        // Make a POST request to the endpoint
        ResponseEntity<Map<String, Boolean>> responseEntity = speciesController.sneakyAddSpecies(species);

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
