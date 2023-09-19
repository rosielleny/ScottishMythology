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

import com.myth.controller.GenderController;
import com.myth.entity.Gender;
import com.myth.entity.composite.GenericEntity;

import com.myth.service.micro.GenderService;

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
class GenderControllerTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private MockMvc mockMvc;


	@MockBean
	private GenderService genderService;
	
	@Autowired
	private GenderController genderController;
	
	// CREATE
	@Test
	public void testCreateAnGender() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/gender/create"))
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
		Gender createdGender = new Gender();
		createdGender.setGenderPK(1);
		createdGender.setGenderType("New Gender");
		when(genderService.getGenderById(any(Integer.class))).thenReturn(createdGender);
		when(genderService.createGender(any(Gender.class))).thenReturn(createdGender);
		when(genderService.getGenderByName(any(String.class))).thenReturn(createdGender);

		mockMvc.perform(post("/gender/save-new")
				.param("entityName", "") // Invalid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attributeHasFieldErrors("entity", "entityName")) // Expect validation error
		.andExpect(view().name("entity/create-entity")); // Expect view name
	}

	@Test
	@DisplayName("Test successful gender creation")
	public void testSuccessfulGenderCreation() throws Exception {
		Gender createdGender = new Gender();
		createdGender.setGenderPK(1);
		createdGender.setGenderType("New Gender");
		GenericEntity genEnt = new GenericEntity(createdGender.getGenderPK(), createdGender.getGenderType());
		when(genderService.getGenderById(any(Integer.class))).thenReturn(createdGender);
		when(genderService.createGender(any(Gender.class))).thenReturn(createdGender);
		when(genderService.getGenderByName(any(String.class))).thenReturn(createdGender);

		mockMvc.perform(post("/gender/save-new")
				.param("entityName", "New Gender") // Valid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attribute("message", "New Gender successfully added to the database.")) // Expect success message
		.andExpect(model().attribute("entity", samePropertyValuesAs(genEnt))) // Expect entity attribute to be updated
		.andExpect(view().name("entity/entity-output")); // Expect view name
	}

	@Test
	@DisplayName("Test error handling")
	public void testErrorHandling() throws Exception {
		// Mock an error during gender creation
		when(genderService.createGender(any(Gender.class))).thenReturn(null);

		// Create a POST request with valid input
		mockMvc.perform(post("/gender/save-new")
				.param("entityName", "New Gender") // Valid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attribute("message", "An error occurred. Gender not added to the database.")) // Expect error message
		.andExpect(view().name("entity/entity-output")); // Expect view name
	}
	
	// READ
	
    @Test
    @DisplayName("Test show all genders")
    public void testShowAllGenders() throws Exception {
        // Mock your genderService to return a list of Gender objects
        List<Gender> genders = new ArrayList<>();
        Gender gender1 = new Gender(1, "Gender 1");
        Gender gender2 = new Gender(2, "Gender 2");
        genders.add(gender2);
        genders.add(gender1);
        
        when(genderService.getAllGender()).thenReturn(genders);

        mockMvc.perform(get("/gender/genders"))
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-all-entities")) // Expect view name
            .andExpect(model().attributeExists("entityList")) // Expect model attribute
            .andExpect(model().attribute("entityList", Matchers.hasSize(2))); // Expect entityList size to match the number of genders
    }
    
    @Test
    @DisplayName("Test search genders by name")
    public void testSearchGendersByName() throws Exception {
        Gender mockGender = new Gender();
        mockGender.setGenderPK(1);
        mockGender.setGenderType("Mocked Gender");
        
        when(genderService.getGenderByName("ExistingGender")).thenReturn(mockGender);
        when(genderService.getGenderByName("NonExistingGender")).thenReturn(null);
        
        mockMvc.perform(get("/gender/search")
                .param("name", "ExistingGender")) // Valid input for an existing gender
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-entity")) // Expect view name
            .andExpect(model().attributeExists("entity")) // Expect model attribute
            .andExpect(model().attribute("entity", Matchers.hasProperty("entityName", is("Mocked Gender")))); // Expect entity attribute

        mockMvc.perform(get("/gender/search")
                .param("name", "NonExistingGender")) // Valid input for a non-existing gender
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-entity")) // Expect view name
            .andExpect(model().attribute("message", is("No genders were found for NonExistingGender"))); // Expect message attribute
    }
    
    // UPDATE
    
    @Test
    @DisplayName("Test edit genders by ID")
    public void testEditGendersById() throws Exception {
        Gender mockGender = new Gender();
        mockGender.setGenderPK(1);
        mockGender.setGenderType("Mocked Gender");
        
        when(genderService.getGenderById(1)).thenReturn(mockGender);
        
        mockMvc.perform(get("/gender/edit")
                .param("pk", "1")) // Valid input for an existing gender
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/edit-entity")) // Expect view name
            .andExpect(model().attributeExists("entity")) // Expect model attribute
            .andExpect(model().attributeExists("ogEntity")) // Expect model attribute
            .andExpect(model().attribute("entity", Matchers.hasProperty("entityName", is("Mocked Gender")))); // Expect entity attribute
    }
    
    // DELETE
    
    @Test
    @DisplayName("Test successful gender deletion")
    public void testSuccessfulGenderDeletion() throws Exception {
        int genderIdToDelete = 1;
        Gender deletedGender = new Gender();
        deletedGender.setGenderPK(genderIdToDelete);
        deletedGender.setGenderType("Deleted Gender");
        List<Gender> genders = new ArrayList<>();
        Gender gender1 = new Gender(3, "Gender 3");
        Gender gender2 = new Gender(2, "Gender 2");
        genders.add(gender2);
        genders.add(gender1);

        when(genderService.getGenderById(genderIdToDelete)).thenReturn(deletedGender);
        when(genderService.deleteGender(genderIdToDelete)).thenReturn(true);
        when(genderService.getAllGender()).thenReturn(genders);


        mockMvc.perform(get("/gender/delete/{pk}", genderIdToDelete))
            .andExpect(status().isOk())
            .andExpect(model().attribute("message", "Deleted Gender successfully deleted from Genders."))
            .andExpect(view().name("entity/show-all-entities"));

        verify(genderService, times(1)).getGenderById(genderIdToDelete);
        verify(genderService, times(1)).deleteGender(genderIdToDelete);
    }

    @Test
    @DisplayName("Test unsuccessful gender deletion")
    public void testUnsuccessfulGenderDeletion() throws Exception {
        int genderIdToDelete = 1;
        Gender genderToDelete = new Gender();
        genderToDelete.setGenderPK(genderIdToDelete);
        genderToDelete.setGenderType("Deletion Failure");
        List<Gender> genders = new ArrayList<>();
        Gender gender1 = new Gender(3, "Gender 3");
        Gender gender2 = new Gender(2, "Gender 2");
        genders.add(gender2);
        genders.add(gender1);
        genders.add(genderToDelete);
        

        when(genderService.getGenderById(genderIdToDelete)).thenReturn(genderToDelete);
        when(genderService.deleteGender(genderIdToDelete)).thenReturn(false);
        when(genderService.getAllGender()).thenReturn(genders);

        mockMvc.perform(get("/gender/delete/{pk}", genderIdToDelete))
            .andExpect(status().isOk())
            .andExpect(model().attribute("message", "Failed to delete Deletion Failure from Genders."))
            .andExpect(view().name("entity/show-all-entities"));

        // Verify that the service method was called with the correct parameter
        verify(genderService, times(1)).getGenderById(genderIdToDelete);
        verify(genderService, times(1)).deleteGender(genderIdToDelete);
    }

    // EXTRA

    @Test
    public void testSneakyAddGender_Success() {
        // Create a sample Gender object for testing
        Gender gender = new Gender();
        gender.setGenderType("Test Gender");

        when(genderService.createGender(gender)).thenReturn(gender);

        // Make a POST request to the endpoint
        ResponseEntity<Map<String, Boolean>> responseEntity = genderController.sneakyAddGender(gender);

        // Verify that the response is as expected
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().get("success"));
    }

    @Test
    public void testSneakyAddGender_Failure() {
        // Create a sample Gender object for testing
        Gender gender = new Gender();
        gender.setGenderType("Test Gender");

        when(genderService.createGender(gender)).thenReturn(null);

        // Make a POST request to the endpoint
        ResponseEntity<Map<String, Boolean>> responseEntity = genderController.sneakyAddGender(gender);

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
