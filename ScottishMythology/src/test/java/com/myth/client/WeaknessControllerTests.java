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

import com.myth.controller.WeaknessController;
import com.myth.dao.junction.BeingWeaknessDao;
import com.myth.entity.Weakness;
import com.myth.entity.composite.GenericEntity;
import com.myth.entity.junction.BeingWeakness;
import com.myth.entity.junction.KeyBeingWeakness;
import com.myth.service.junction.BeingWeaknessService;
import com.myth.service.micro.WeaknessService;

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
class WeaknessControllerTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private MockMvc mockMvc;


	@MockBean
	private WeaknessService weaknessService;
	
	@Autowired
	private WeaknessController weaknessController;
	
	// CREATE
	@Test
	public void testCreateAnWeakness() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/weakness/create"))
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
		Weakness createdWeakness = new Weakness();
		createdWeakness.setWeaknessPK(1);
		createdWeakness.setWeaknessName("New Weakness");
		when(weaknessService.getWeaknessById(any(Integer.class))).thenReturn(createdWeakness);
		when(weaknessService.createWeakness(any(Weakness.class))).thenReturn(createdWeakness);
		when(weaknessService.getWeaknessByName(any(String.class))).thenReturn(createdWeakness);

		mockMvc.perform(post("/weakness/save-new")
				.param("entityName", "") // Invalid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attributeHasFieldErrors("entity", "entityName")) // Expect validation error
		.andExpect(view().name("entity/create-entity")); // Expect view name
	}

	@Test
	@DisplayName("Test successful weakness creation")
	public void testSuccessfulWeaknessCreation() throws Exception {
		Weakness createdWeakness = new Weakness();
		createdWeakness.setWeaknessPK(1);
		createdWeakness.setWeaknessName("New Weakness");
		GenericEntity genEnt = new GenericEntity(createdWeakness.getWeaknessPK(), createdWeakness.getWeaknessName());
		when(weaknessService.getWeaknessById(any(Integer.class))).thenReturn(createdWeakness);
		when(weaknessService.createWeakness(any(Weakness.class))).thenReturn(createdWeakness);
		when(weaknessService.getWeaknessByName(any(String.class))).thenReturn(createdWeakness);

		mockMvc.perform(post("/weakness/save-new")
				.param("entityName", "New Weakness") // Valid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attribute("message", "New Weakness successfully added to the database.")) // Expect success message
		.andExpect(model().attribute("entity", samePropertyValuesAs(genEnt))) // Expect entity attribute to be updated
		.andExpect(view().name("entity/entity-output")); // Expect view name
	}

	@Test
	@DisplayName("Test error handling")
	public void testErrorHandling() throws Exception {
		// Mock an error during weakness creation
		when(weaknessService.createWeakness(any(Weakness.class))).thenReturn(null);

		// Create a POST request with valid input
		mockMvc.perform(post("/weakness/save-new")
				.param("entityName", "New Weakness") // Valid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attribute("message", "An error occurred. Weakness not added to the database.")) // Expect error message
		.andExpect(view().name("entity/entity-output")); // Expect view name
	}
	
	// READ
	
    @Test
    @DisplayName("Test show all weaknesses")
    public void testShowAllWeaknesses() throws Exception {
        // Mock your weaknessService to return a list of Weakness objects
        List<Weakness> weaknesses = new ArrayList<>();
        Weakness weakness1 = new Weakness(1, "Weakness 1");
        Weakness weakness2 = new Weakness(2, "Weakness 2");
        weaknesses.add(weakness2);
        weaknesses.add(weakness1);
        
        when(weaknessService.getAllWeakness()).thenReturn(weaknesses);

        mockMvc.perform(get("/weakness/weaknesses"))
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-all-entities")) // Expect view name
            .andExpect(model().attributeExists("entityList")) // Expect model attribute
            .andExpect(model().attribute("entityList", Matchers.hasSize(2))); // Expect entityList size to match the number of weaknesses
    }
    
    @Test
    @DisplayName("Test search weaknesses by name")
    public void testSearchWeaknessesByName() throws Exception {
        Weakness mockWeakness = new Weakness();
        mockWeakness.setWeaknessPK(1);
        mockWeakness.setWeaknessName("Mocked Weakness");
        
        when(weaknessService.getWeaknessByName("ExistingWeakness")).thenReturn(mockWeakness);
        when(weaknessService.getWeaknessByName("NonExistingWeakness")).thenReturn(null);
        
        mockMvc.perform(get("/weakness/search")
                .param("name", "ExistingWeakness")) // Valid input for an existing weakness
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-entity")) // Expect view name
            .andExpect(model().attributeExists("entity")) // Expect model attribute
            .andExpect(model().attribute("entity", Matchers.hasProperty("entityName", is("Mocked Weakness")))); // Expect entity attribute

        mockMvc.perform(get("/weakness/search")
                .param("name", "NonExistingWeakness")) // Valid input for a non-existing weakness
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-entity")) // Expect view name
            .andExpect(model().attribute("message", is("No weaknesses were found for NonExistingWeakness"))); // Expect message attribute
    }
    
    // UPDATE
    
    @Test
    @DisplayName("Test edit weaknesses by ID")
    public void testEditWeaknessesById() throws Exception {
        Weakness mockWeakness = new Weakness();
        mockWeakness.setWeaknessPK(1);
        mockWeakness.setWeaknessName("Mocked Weakness");
        
        when(weaknessService.getWeaknessById(1)).thenReturn(mockWeakness);
        
        mockMvc.perform(get("/weakness/edit")
                .param("pk", "1")) // Valid input for an existing weakness
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/edit-entity")) // Expect view name
            .andExpect(model().attributeExists("entity")) // Expect model attribute
            .andExpect(model().attributeExists("ogEntity")) // Expect model attribute
            .andExpect(model().attribute("entity", Matchers.hasProperty("entityName", is("Mocked Weakness")))); // Expect entity attribute
    }
    
    // DELETE
    
    @Test
    @DisplayName("Test successful weakness deletion")
    public void testSuccessfulWeaknessDeletion() throws Exception {
        int weaknessIdToDelete = 1;
        Weakness deletedWeakness = new Weakness();
        deletedWeakness.setWeaknessPK(weaknessIdToDelete);
        deletedWeakness.setWeaknessName("Deleted Weakness");
        List<Weakness> weaknesses = new ArrayList<>();
        Weakness weakness1 = new Weakness(3, "Weakness 3");
        Weakness weakness2 = new Weakness(2, "Weakness 2");
        weaknesses.add(weakness2);
        weaknesses.add(weakness1);

        when(weaknessService.getWeaknessById(weaknessIdToDelete)).thenReturn(deletedWeakness);
        when(weaknessService.deleteWeakness(weaknessIdToDelete)).thenReturn(true);
        when(weaknessService.getAllWeakness()).thenReturn(weaknesses);


        mockMvc.perform(get("/weakness/delete/{pk}", weaknessIdToDelete))
            .andExpect(status().isOk())
            .andExpect(model().attribute("message", "Deleted Weakness successfully deleted from Weaknesses."))
            .andExpect(view().name("entity/show-all-entities"));

        verify(weaknessService, times(1)).getWeaknessById(weaknessIdToDelete);
        verify(weaknessService, times(1)).deleteWeakness(weaknessIdToDelete);
    }

    @Test
    @DisplayName("Test unsuccessful weakness deletion")
    public void testUnsuccessfulWeaknessDeletion() throws Exception {
        int weaknessIdToDelete = 1;
        Weakness weaknessToDelete = new Weakness();
        weaknessToDelete.setWeaknessPK(weaknessIdToDelete);
        weaknessToDelete.setWeaknessName("Deletion Failure");
        List<Weakness> weaknesses = new ArrayList<>();
        Weakness weakness1 = new Weakness(3, "Weakness 3");
        Weakness weakness2 = new Weakness(2, "Weakness 2");
        weaknesses.add(weakness2);
        weaknesses.add(weakness1);
        weaknesses.add(weaknessToDelete);
        

        when(weaknessService.getWeaknessById(weaknessIdToDelete)).thenReturn(weaknessToDelete);
        when(weaknessService.deleteWeakness(weaknessIdToDelete)).thenReturn(false);
        when(weaknessService.getAllWeakness()).thenReturn(weaknesses);

        mockMvc.perform(get("/weakness/delete/{pk}", weaknessIdToDelete))
            .andExpect(status().isOk())
            .andExpect(model().attribute("message", "Failed to delete Deletion Failure from Weaknesses."))
            .andExpect(view().name("entity/show-all-entities"));

        // Verify that the service method was called with the correct parameter
        verify(weaknessService, times(1)).getWeaknessById(weaknessIdToDelete);
        verify(weaknessService, times(1)).deleteWeakness(weaknessIdToDelete);
    }

    // EXTRA

    @Test
    public void testSneakyAddWeakness_Success() {
        // Create a sample Weakness object for testing
        Weakness weakness = new Weakness();
        weakness.setWeaknessName("Test Weakness");

        when(weaknessService.createWeakness(weakness)).thenReturn(weakness);

        // Make a POST request to the endpoint
        ResponseEntity<Map<String, Boolean>> responseEntity = weaknessController.sneakyAddWeakness(weakness);

        // Verify that the response is as expected
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().get("success"));
    }

    @Test
    public void testSneakyAddWeakness_Failure() {
        // Create a sample Weakness object for testing
        Weakness weakness = new Weakness();
        weakness.setWeaknessName("Test Weakness");

        when(weaknessService.createWeakness(weakness)).thenReturn(null);

        // Make a POST request to the endpoint
        ResponseEntity<Map<String, Boolean>> responseEntity = weaknessController.sneakyAddWeakness(weakness);

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
