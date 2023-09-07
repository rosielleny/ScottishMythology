package com.weakness.client;

import org.assertj.core.util.Lists;
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

import com.weakness.dao.WeaknessDao;
import com.weakness.entity.Weakness;
import com.weakness.service.WeaknessService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
class WeaknessControllerTests {

	@Test
	void contextLoads() {
	}
	
	//@Autowired
	//private WeaknessService weaknessService;
	@MockBean
	private WeaknessDao weaknessDao;
	@Autowired
    private MockMvc mockMvc;
	@MockBean
	private WeaknessService weaknessService;
	
	
	// Controller Tests
	
    @Test
    @DisplayName("GET /weakness/all-weakness success")
    void testGetWeaknesssSuccess() throws Exception {
        // Setup our mocked service
        Weakness weakness1 = new Weakness(1, "Weakness Name");
        Weakness weakness2 = new Weakness(2, "Weakness 2 Name");
        doReturn(Lists.newArrayList(weakness1, weakness2)).when(weaknessService).getAllWeakness();

        // Execute the GET request
        mockMvc.perform(get("/weakness/all-weakness"))
        // Validate the response code and content type
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        // Validate the returned fields
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].weaknessPK", is(1)))
        .andExpect(jsonPath("$[0].weaknessName", is("Weakness Name")))
        .andExpect(jsonPath("$[1].weaknessPK", is(2)))
        .andExpect(jsonPath("$[1].weaknessName", is("Weakness 2 Name")));
    }
	
    
    @Test
    @DisplayName("GET /weakness/1")
    void testGetWeaknessById() throws Exception {
    	
    	// Setup our mocked service
        Weakness weakness = new Weakness(1, "Weakness Name");
        doReturn(weakness).when(weaknessService).getWeaknessById(1);

        // Execute the GET request
        mockMvc.perform(get("/weakness/{weaknessPK}", 1))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.weaknessPK", is(1)))
                .andExpect(jsonPath("$.weaknessName", is("Weakness Name")));
    }
    
    @Test
    @DisplayName("GET /weakness/1 - Not Found")
    void testGetWeaknessByIdNotFound() throws Exception {
        // Setup our mocked service
    	doReturn(null).when(weaknessService).getWeaknessById(1);

        // Execute the GET request
        mockMvc.perform(get("/weakness/{weaknessPK}", 1))
                // Validate the response code
                .andExpect(status().isNoContent());
    }
    
    @Test
    @DisplayName("/weakness/new-weakness")
    void testCreateWeakness() throws Exception {
        // Setup our mocked service
        Weakness weaknessToPost = new Weakness(0, "New Weakness");
        Weakness weaknessToReturn = new Weakness(1, "New Weakness");
        doReturn(weaknessToReturn).when(weaknessService).createWeakness(any());

        // Execute the POST request
        mockMvc.perform(post("/weakness/new-weakness")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(weaknessToPost)))

                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.weaknessPK", is(1)))
                .andExpect(jsonPath("$.weaknessName", is("New Weakness")));
    }
    
    @Test
    @DisplayName("PUT /weakness/update-weakness/{weaknessPK}")
    void testUpdateWeakness() throws Exception {
        // Setup our mocked service
        Weakness originalWeakness = new Weakness(1, "Original Weakness");
        Weakness updatedWeakness = new Weakness(1, "Updated Weakness");
        doReturn(originalWeakness).when(weaknessService).getWeaknessById(1);
        doReturn(true).when(weaknessService).updateWeakness(any());

        // Execute the PUT request
        mockMvc.perform(put("/weakness/update-weakness/{weaknessPK}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedWeakness)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.weaknessPK", is(1)))
                .andExpect(jsonPath("$.weaknessName", is("Updated Weakness")));
    }
    
    @Test
    @DisplayName("PUT /weakness/update-weakness/{weaknessPK} - Not Found")
    void testUpdateWeaknessNotFound() throws Exception {
        // Setup our mocked service
        Weakness weaknessToPut = new Weakness(1, "New Weakness");
        
        doReturn(null).when(weaknessService).getWeaknessById(1);
        doReturn(false).when(weaknessService).updateWeakness(weaknessToPut);

        // Execute the PUT request
        mockMvc.perform(put("/weakness/update-weakness/{weaknessPK}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(weaknessToPut)))
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
