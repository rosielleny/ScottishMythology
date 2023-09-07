package com.location.client;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.http.MediaType;

import com.location.dao.LocationDao;
import com.location.entity.Location;
import com.location.service.LocationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
class LocationControllerTests {

	@Test
	void contextLoads() {
	}
	
	//@Autowired
	//private LocationService locationService;
	@MockBean
	private LocationDao locationDao;
	@Autowired
    private MockMvc mockMvc;
	@MockBean
	private LocationService locationService;
	
	
	// Controller Tests
	
    @Test
    @DisplayName("GET /location/all-location success")
    void testGetLocationsSuccess() throws Exception {
        // Setup our mocked service
        Location location1 = new Location(1, "Location Name", "Description");
        Location location2 = new Location(2, "Location 2 Name", "Description 2");
        doReturn(Lists.newArrayList(location1, location2)).when(locationService).getAllLocation();

        // Execute the GET request
        mockMvc.perform(get("/location/all-location"))
        // Validate the response code and content type
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        // Validate the returned fields
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].locationPK", is(1)))
        .andExpect(jsonPath("$[0].locationName", is("Location Name")))
        .andExpect(jsonPath("$[0].locationDescription", is("Description")))
        .andExpect(jsonPath("$[1].locationPK", is(2)))
        .andExpect(jsonPath("$[1].locationName", is("Location 2 Name")))
        .andExpect(jsonPath("$[1].locationDescription", is("Description 2")));
    }
	
    
    @Test
    @DisplayName("GET /location/1")
    void testGetLocationById() throws Exception {
    	
    	// Setup our mocked service
        Location location = new Location(1, "Location Name", "Description");
        doReturn(location).when(locationService).getLocationById(1);

        // Execute the GET request
        mockMvc.perform(get("/location/{locationPK}", 1))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.locationPK", is(1)))
                .andExpect(jsonPath("$.locationName", is("Location Name")));
    }
    
    @Test
    @DisplayName("GET /location/1 - Not Found")
    void testGetLocationByIdNotFound() throws Exception {
        // Setup our mocked service
    	doReturn(null).when(locationService).getLocationById(1);

        // Execute the GET request
        mockMvc.perform(get("/location/{locationPK}", 1))
                // Validate the response code
                .andExpect(status().isNoContent());
    }
    
    @Test
    @DisplayName("/location/new-location")
    void testCreateLocation() throws Exception {
        // Setup our mocked service
        Location locationToPost = new Location(0, "New Location", "Description");
        Location locationToReturn = new Location(1, "New Location", "Description");
        doReturn(locationToReturn).when(locationService).createLocation(any());

        // Execute the POST request
        mockMvc.perform(post("/location/new-location")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(locationToPost)))

                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.locationPK", is(1)))
                .andExpect(jsonPath("$.locationName", is("New Location")));
    }
    
    @Test
    @DisplayName("PUT /location/update-location/{locationPK}")
    void testUpdateLocation() throws Exception {
        // Setup our mocked service
        Location originalLocation = new Location(1, "Original Location", "Description");
        Location updatedLocation = new Location(1, "Updated Location", "Description");
        doReturn(originalLocation).when(locationService).getLocationById(1);
        doReturn(true).when(locationService).updateLocation(any());

        // Execute the PUT request
        mockMvc.perform(put("/location/update-location/{locationPK}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedLocation)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.locationPK", is(1)))
                .andExpect(jsonPath("$.locationName", is("Updated Location")));
    }
    
    @Test
    @DisplayName("PUT /location/update-location/{locationPK} - Not Found")
    void testUpdateLocationNotFound() throws Exception {
        // Setup our mocked service
        Location locationToPut = new Location(1, "New Location", "Description");
        
        doReturn(null).when(locationService).getLocationById(1);
        doReturn(false).when(locationService).updateLocation(locationToPut);

        // Execute the PUT request
        mockMvc.perform(put("/location/update-location/{locationPK}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(locationToPut)))
                // Validate the response code and content type
                .andExpect(status().isNoContent());
    }

    
    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
