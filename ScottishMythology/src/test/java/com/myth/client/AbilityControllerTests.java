package com.myth.client;

import org.aspectj.lang.annotation.Before;
import org.hamcrest.Matchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper; // Import ObjectMapper

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.myth.controller.AbilityController;
import com.myth.dao.junction.BeingAbilityDao;
import com.myth.entity.Ability;
import com.myth.entity.composite.GenericEntity;
import com.myth.entity.junction.BeingAbility;
import com.myth.entity.junction.KeyBeingAbility;
import com.myth.service.junction.BeingAbilityService;
import com.myth.service.micro.AbilityService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.mockito.Mockito.*;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
class AbilityControllerTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private MockMvc mockMvc;


	@MockBean
	private AbilityService abilityService;
	
	@Autowired
	private AbilityController abilityController;
	
	// CREATE
	@Test
	public void testCreateAnAbility() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/ability/create"))
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
		Ability createdAbility = new Ability();
		createdAbility.setAbilityPK(1);
		createdAbility.setAbilityName("New Ability");
		when(abilityService.getAbilityById(any(Integer.class))).thenReturn(createdAbility);
		when(abilityService.createAbility(any(Ability.class))).thenReturn(createdAbility);
		when(abilityService.getAbilityByName(any(String.class))).thenReturn(createdAbility);

		mockMvc.perform(post("/ability/save-new")
				.param("entityName", "") // Invalid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attributeHasFieldErrors("entity", "entityName")) // Expect validation error
		.andExpect(view().name("entity/create-entity")); // Expect view name
	}

	@Test
	@DisplayName("Test successful ability creation")
	public void testSuccessfulAbilityCreation() throws Exception {
		Ability createdAbility = new Ability();
		createdAbility.setAbilityPK(1);
		createdAbility.setAbilityName("New Ability");
		GenericEntity genEnt = new GenericEntity(createdAbility.getAbilityPK(), createdAbility.getAbilityName());
		when(abilityService.getAbilityById(any(Integer.class))).thenReturn(createdAbility);
		when(abilityService.createAbility(any(Ability.class))).thenReturn(createdAbility);
		when(abilityService.getAbilityByName(any(String.class))).thenReturn(createdAbility);

		mockMvc.perform(post("/ability/save-new")
				.param("entityName", "New Ability") // Valid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attribute("message", "New Ability successfully added to the database.")) // Expect success message
		.andExpect(model().attribute("entity", samePropertyValuesAs(genEnt))) // Expect entity attribute to be updated
		.andExpect(view().name("entity/entity-output")); // Expect view name
	}

	@Test
	@DisplayName("Test error handling")
	public void testErrorHandling() throws Exception {
		// Mock an error during ability creation
		when(abilityService.createAbility(any(Ability.class))).thenReturn(null);

		// Create a POST request with valid input
		mockMvc.perform(post("/ability/save-new")
				.param("entityName", "New Ability") // Valid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attribute("message", "An error occurred. Ability not added to the database.")) // Expect error message
		.andExpect(view().name("entity/entity-output")); // Expect view name
	}
	
	// READ
	
    @Test
    @DisplayName("Test show all abilities")
    public void testShowAllAbilities() throws Exception {
        // Mock your abilityService to return a list of Ability objects
        List<Ability> abilities = new ArrayList<>();
        Ability ability1 = new Ability(1, "Ability 1");
        Ability ability2 = new Ability(2, "Ability 2");
        abilities.add(ability2);
        abilities.add(ability1);
        
        when(abilityService.getAllAbility()).thenReturn(abilities);

        mockMvc.perform(get("/ability/abilities"))
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-all-entities")) // Expect view name
            .andExpect(model().attributeExists("entityList")) // Expect model attribute
            .andExpect(model().attribute("entityList", Matchers.hasSize(2))); // Expect entityList size to match the number of abilities
    }
    
    @Test
    @DisplayName("Test search abilities by name")
    public void testSearchAbilitiesByName() throws Exception {
        Ability mockAbility = new Ability();
        mockAbility.setAbilityPK(1);
        mockAbility.setAbilityName("Mocked Ability");
        
        when(abilityService.getAbilityByName("ExistingAbility")).thenReturn(mockAbility);
        when(abilityService.getAbilityByName("NonExistingAbility")).thenReturn(null);
        
        mockMvc.perform(get("/ability/search")
                .param("name", "ExistingAbility")) // Valid input for an existing ability
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-entity")) // Expect view name
            .andExpect(model().attributeExists("entity")) // Expect model attribute
            .andExpect(model().attribute("entity", Matchers.hasProperty("entityName", is("Mocked Ability")))); // Expect entity attribute

        mockMvc.perform(get("/ability/search")
                .param("name", "NonExistingAbility")) // Valid input for a non-existing ability
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-entity")) // Expect view name
            .andExpect(model().attribute("message", is("No abilities were found for NonExistingAbility"))); // Expect message attribute
    }
    
    // UPDATE
    
    @Test
    @DisplayName("Test edit abilities by ID")
    public void testEditAbilitiesById() throws Exception {
        Ability mockAbility = new Ability();
        mockAbility.setAbilityPK(1);
        mockAbility.setAbilityName("Mocked Ability");
        
        when(abilityService.getAbilityById(1)).thenReturn(mockAbility);
        
        mockMvc.perform(get("/ability/edit")
                .param("pk", "1")) // Valid input for an existing ability
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/edit-entity")) // Expect view name
            .andExpect(model().attributeExists("entity")) // Expect model attribute
            .andExpect(model().attributeExists("ogEntity")) // Expect model attribute
            .andExpect(model().attribute("entity", Matchers.hasProperty("entityName", is("Mocked Ability")))); // Expect entity attribute
    }
    
    // DELETE
    
    @Test
    @DisplayName("Test successful ability deletion")
    public void testSuccessfulAbilityDeletion() throws Exception {
        int abilityIdToDelete = 1;
        Ability deletedAbility = new Ability();
        deletedAbility.setAbilityPK(abilityIdToDelete);
        deletedAbility.setAbilityName("Deleted Ability");
        List<Ability> abilities = new ArrayList<>();
        Ability ability1 = new Ability(3, "Ability 3");
        Ability ability2 = new Ability(2, "Ability 2");
        abilities.add(ability2);
        abilities.add(ability1);

        when(abilityService.getAbilityById(abilityIdToDelete)).thenReturn(deletedAbility);
        when(abilityService.deleteAbility(abilityIdToDelete)).thenReturn(true);
        when(abilityService.getAllAbility()).thenReturn(abilities);


        mockMvc.perform(get("/ability/delete/{pk}", abilityIdToDelete))
            .andExpect(status().isOk())
            .andExpect(model().attribute("message", "Deleted Ability successfully deleted from Abilities."))
            .andExpect(view().name("entity/show-all-entities"));

        verify(abilityService, times(1)).getAbilityById(abilityIdToDelete);
        verify(abilityService, times(1)).deleteAbility(abilityIdToDelete);
    }

    @Test
    @DisplayName("Test unsuccessful ability deletion")
    public void testUnsuccessfulAbilityDeletion() throws Exception {
        int abilityIdToDelete = 1;
        Ability abilityToDelete = new Ability();
        abilityToDelete.setAbilityPK(abilityIdToDelete);
        abilityToDelete.setAbilityName("Deletion Failure");
        List<Ability> abilities = new ArrayList<>();
        Ability ability1 = new Ability(3, "Ability 3");
        Ability ability2 = new Ability(2, "Ability 2");
        abilities.add(ability2);
        abilities.add(ability1);
        abilities.add(abilityToDelete);
        

        when(abilityService.getAbilityById(abilityIdToDelete)).thenReturn(abilityToDelete);
        when(abilityService.deleteAbility(abilityIdToDelete)).thenReturn(false);
        when(abilityService.getAllAbility()).thenReturn(abilities);

        mockMvc.perform(get("/ability/delete/{pk}", abilityIdToDelete))
            .andExpect(status().isOk())
            .andExpect(model().attribute("message", "Failed to delete Deletion Failure from Abilities."))
            .andExpect(view().name("entity/show-all-entities"));

        // Verify that the service method was called with the correct parameter
        verify(abilityService, times(1)).getAbilityById(abilityIdToDelete);
        verify(abilityService, times(1)).deleteAbility(abilityIdToDelete);
    }

    // EXTRA

    @Test
    public void testSneakyAddAbility_Success() {
        // Create a sample Ability object for testing
        Ability ability = new Ability();
        ability.setAbilityName("Test Ability");

        when(abilityService.createAbility(ability)).thenReturn(ability);

        // Make a POST request to the endpoint
        ResponseEntity<Map<String, Boolean>> responseEntity = abilityController.sneakyAddAbility(ability);

        // Verify that the response is as expected
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().get("success"));
    }

    @Test
    public void testSneakyAddAbility_Failure() {
        // Create a sample Ability object for testing
        Ability ability = new Ability();
        ability.setAbilityName("Test Ability");

        when(abilityService.createAbility(ability)).thenReturn(null);

        // Make a POST request to the endpoint
        ResponseEntity<Map<String, Boolean>> responseEntity = abilityController.sneakyAddAbility(ability);

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
