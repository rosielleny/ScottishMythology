package com.species.client;

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

import com.species.dao.SpeciesDao;
import com.species.entity.Species;
import com.species.service.SpeciesService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
class SpeciesControllerTests {

	@Test
	void contextLoads() {
	}
	
	//@Autowired
	//private SpeciesService speciesService;
	@MockBean
	private SpeciesDao speciesDao;
	@Autowired
    private MockMvc mockMvc;
	@MockBean
	private SpeciesService speciesService;
	
	
	// Controller Tests
	
    @Test
    @DisplayName("GET /species/all-species success")
    void testGetSpeciessSuccess() throws Exception {
        // Setup our mocked service
        Species species1 = new Species(1, "Species Name", "Description");
        Species species2 = new Species(2, "Species 2 Name", "Description 2");
        doReturn(Lists.newArrayList(species1, species2)).when(speciesService).getAllSpecies();

        // Execute the GET request
        mockMvc.perform(get("/species/all-species"))
        // Validate the response code and content type
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        // Validate the returned fields
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].speciesPK", is(1)))
        .andExpect(jsonPath("$[0].speciesName", is("Species Name")))
        .andExpect(jsonPath("$[0].speciesDescription", is("Description")))
        .andExpect(jsonPath("$[1].speciesPK", is(2)))
        .andExpect(jsonPath("$[1].speciesName", is("Species 2 Name")))
        .andExpect(jsonPath("$[1].speciesDescription", is("Description 2")));
    }
	
    
    @Test
    @DisplayName("GET /species/1")
    void testGetSpeciesById() throws Exception {
    	
    	// Setup our mocked service
        Species species = new Species(1, "Species Name", "Description");
        doReturn(species).when(speciesService).getSpeciesById(1);

        // Execute the GET request
        mockMvc.perform(get("/species/{speciesPK}", 1))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.speciesPK", is(1)))
                .andExpect(jsonPath("$.speciesName", is("Species Name")));
    }
    
    @Test
    @DisplayName("GET /species/1 - Not Found")
    void testGetSpeciesByIdNotFound() throws Exception {
        // Setup our mocked service
    	doReturn(null).when(speciesService).getSpeciesById(1);

        // Execute the GET request
        mockMvc.perform(get("/species/{speciesPK}", 1))
                // Validate the response code
                .andExpect(status().isNoContent());
    }
    
    @Test
    @DisplayName("/species/new-species")
    void testCreateSpecies() throws Exception {
        // Setup our mocked service
        Species speciesToPost = new Species(0, "New Species", "Description");
        Species speciesToReturn = new Species(1, "New Species", "Description");
        doReturn(speciesToReturn).when(speciesService).createSpecies(any());

        // Execute the POST request
        mockMvc.perform(post("/species/new-species")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(speciesToPost)))

                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.speciesPK", is(1)))
                .andExpect(jsonPath("$.speciesName", is("New Species")));
    }
    
    @Test
    @DisplayName("PUT /species/update-species/{speciesPK}")
    void testUpdateSpecies() throws Exception {
        // Setup our mocked service
        Species originalSpecies = new Species(1, "Original Species", "Description");
        Species updatedSpecies = new Species(1, "Updated Species", "Description");
        doReturn(originalSpecies).when(speciesService).getSpeciesById(1);
        doReturn(true).when(speciesService).updateSpecies(any());

        // Execute the PUT request
        mockMvc.perform(put("/species/update-species/{speciesPK}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedSpecies)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.speciesPK", is(1)))
                .andExpect(jsonPath("$.speciesName", is("Updated Species")));
    }
    
    @Test
    @DisplayName("PUT /species/update-species/{speciesPK} - Not Found")
    void testUpdateSpeciesNotFound() throws Exception {
        // Setup our mocked service
        Species speciesToPut = new Species(1, "New Species", "Description");
        
        doReturn(null).when(speciesService).getSpeciesById(1);
        doReturn(false).when(speciesService).updateSpecies(speciesToPut);

        // Execute the PUT request
        mockMvc.perform(put("/species/update-species/{speciesPK}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(speciesToPut)))
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
