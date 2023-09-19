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

import com.myth.controller.LocationController;
import com.myth.entity.Location;
import com.myth.entity.composite.GenericEntity;

import com.myth.service.micro.LocationService;

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
class LocationControllerTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private MockMvc mockMvc;


	@MockBean
	private LocationService locationService;
	
	@Autowired
	private LocationController locationController;
	
	// CREATE
	@Test
	public void testCreateAnLocation() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/location/create"))
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
		Location createdLocation = new Location();
		createdLocation.setLocationPK(1);
		createdLocation.setLocationName("New Location");
		when(locationService.getLocationById(any(Integer.class))).thenReturn(createdLocation);
		when(locationService.createLocation(any(Location.class))).thenReturn(createdLocation);
		when(locationService.getLocationByName(any(String.class))).thenReturn(createdLocation);

		mockMvc.perform(post("/location/save-new")
				.param("entityName", "") // Invalid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attributeHasFieldErrors("entity", "entityName")) // Expect validation error
		.andExpect(view().name("entity/create-entity")); // Expect view name
	}

	@Test
	@DisplayName("Test successful location creation")
	public void testSuccessfulLocationCreation() throws Exception {
		Location createdLocation = new Location();
		createdLocation.setLocationPK(1);
		createdLocation.setLocationName("New Location");
		GenericEntity genEnt = new GenericEntity(createdLocation.getLocationPK(), createdLocation.getLocationName());
		when(locationService.getLocationById(any(Integer.class))).thenReturn(createdLocation);
		when(locationService.createLocation(any(Location.class))).thenReturn(createdLocation);
		when(locationService.getLocationByName(any(String.class))).thenReturn(createdLocation);

		mockMvc.perform(post("/location/save-new")
				.param("entityName", "New Location") // Valid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attribute("message", "New Location successfully added to the database.")) // Expect success message
		.andExpect(model().attribute("entity", samePropertyValuesAs(genEnt))) // Expect entity attribute to be updated
		.andExpect(view().name("entity/entity-output")); // Expect view name
	}

	@Test
	@DisplayName("Test error handling")
	public void testErrorHandling() throws Exception {
		// Mock an error during location creation
		when(locationService.createLocation(any(Location.class))).thenReturn(null);

		// Create a POST request with valid input
		mockMvc.perform(post("/location/save-new")
				.param("entityName", "New Location") // Valid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attribute("message", "An error occurred. Location not added to the database.")) // Expect error message
		.andExpect(view().name("entity/entity-output")); // Expect view name
	}
	
	// READ
	
    @Test
    @DisplayName("Test show all locations")
    public void testShowAllLocations() throws Exception {
        // Mock your locationService to return a list of Location objects
        List<Location> locations = new ArrayList<>();
        Location location1 = new Location(1, "Location 1", "Description 1");
        Location location2 = new Location(2, "Location 2", "Description 2");
        locations.add(location2);
        locations.add(location1);
        
        when(locationService.getAllLocation()).thenReturn(locations);

        mockMvc.perform(get("/location/locations"))
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-all-entities")) // Expect view name
            .andExpect(model().attributeExists("entityList")) // Expect model attribute
            .andExpect(model().attribute("entityList", Matchers.hasSize(2))); // Expect entityList size to match the number of locations
    }
    
    @Test
    @DisplayName("Test search locations by name")
    public void testSearchLocationsByName() throws Exception {
        Location mockLocation = new Location();
        mockLocation.setLocationPK(1);
        mockLocation.setLocationName("Mocked Location");
        
        when(locationService.getLocationByName("ExistingLocation")).thenReturn(mockLocation);
        when(locationService.getLocationByName("NonExistingLocation")).thenReturn(null);
        
        mockMvc.perform(get("/location/search")
                .param("name", "ExistingLocation")) // Valid input for an existing location
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-entity")) // Expect view name
            .andExpect(model().attributeExists("entity")) // Expect model attribute
            .andExpect(model().attribute("entity", Matchers.hasProperty("entityName", is("Mocked Location")))); // Expect entity attribute

        mockMvc.perform(get("/location/search")
                .param("name", "NonExistingLocation")) // Valid input for a non-existing location
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-entity")) // Expect view name
            .andExpect(model().attribute("message", is("No locations were found for NonExistingLocation"))); // Expect message attribute
    }
    
    // UPDATE
    
    @Test
    @DisplayName("Test edit locations by ID")
    public void testEditLocationsById() throws Exception {
        Location mockLocation = new Location();
        mockLocation.setLocationPK(1);
        mockLocation.setLocationName("Mocked Location");
        
        when(locationService.getLocationById(1)).thenReturn(mockLocation);
        
        mockMvc.perform(get("/location/edit")
                .param("pk", "1")) // Valid input for an existing location
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/edit-entity")) // Expect view name
            .andExpect(model().attributeExists("entity")) // Expect model attribute
            .andExpect(model().attributeExists("ogEntity")) // Expect model attribute
            .andExpect(model().attribute("entity", Matchers.hasProperty("entityName", is("Mocked Location")))); // Expect entity attribute
    }
    
    // DELETE
    
    @Test
    @DisplayName("Test successful location deletion")
    public void testSuccessfulLocationDeletion() throws Exception {
        int locationIdToDelete = 1;
        Location deletedLocation = new Location();
        deletedLocation.setLocationPK(locationIdToDelete);
        deletedLocation.setLocationName("Deleted Location");
        List<Location> locations = new ArrayList<>();
        Location location1 = new Location(3, "Location 3", "Description 3");
        Location location2 = new Location(2, "Location 2", "Description 2");
        locations.add(location2);
        locations.add(location1);

        when(locationService.getLocationById(locationIdToDelete)).thenReturn(deletedLocation);
        when(locationService.deleteLocation(locationIdToDelete)).thenReturn(true);
        when(locationService.getAllLocation()).thenReturn(locations);


        mockMvc.perform(get("/location/delete/{pk}", locationIdToDelete))
            .andExpect(status().isOk())
            .andExpect(model().attribute("message", "Deleted Location successfully deleted from Locations."))
            .andExpect(view().name("entity/show-all-entities"));

        verify(locationService, times(1)).getLocationById(locationIdToDelete);
        verify(locationService, times(1)).deleteLocation(locationIdToDelete);
    }

    @Test
    @DisplayName("Test unsuccessful location deletion")
    public void testUnsuccessfulLocationDeletion() throws Exception {
        int locationIdToDelete = 1;
        Location locationToDelete = new Location();
        locationToDelete.setLocationPK(locationIdToDelete);
        locationToDelete.setLocationName("Deletion Failure");
        List<Location> locations = new ArrayList<>();
        Location location1 = new Location(3, "Location 3", "Description 3");
        Location location2 = new Location(2, "Location 2", "Description 2");
        locations.add(location2);
        locations.add(location1);
        locations.add(locationToDelete);
        

        when(locationService.getLocationById(locationIdToDelete)).thenReturn(locationToDelete);
        when(locationService.deleteLocation(locationIdToDelete)).thenReturn(false);
        when(locationService.getAllLocation()).thenReturn(locations);

        mockMvc.perform(get("/location/delete/{pk}", locationIdToDelete))
            .andExpect(status().isOk())
            .andExpect(model().attribute("message", "Failed to delete Deletion Failure from Locations."))
            .andExpect(view().name("entity/show-all-entities"));

        // Verify that the service method was called with the correct parameter
        verify(locationService, times(1)).getLocationById(locationIdToDelete);
        verify(locationService, times(1)).deleteLocation(locationIdToDelete);
    }

    // EXTRA

    @Test
    public void testSneakyAddLocation_Success() {
        // Create a sample Location object for testing
        Location location = new Location();
        location.setLocationName("Test Location");

        when(locationService.createLocation(location)).thenReturn(location);

        // Make a POST request to the endpoint
        ResponseEntity<Map<String, Boolean>> responseEntity = locationController.sneakyAddLocation(location);

        // Verify that the response is as expected
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().get("success"));
    }

    @Test
    public void testSneakyAddLocation_Failure() {
        // Create a sample Location object for testing
        Location location = new Location();
        location.setLocationName("Test Location");

        when(locationService.createLocation(location)).thenReturn(null);

        // Make a POST request to the endpoint
        ResponseEntity<Map<String, Boolean>> responseEntity = locationController.sneakyAddLocation(location);

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
