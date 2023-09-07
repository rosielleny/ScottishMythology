package com.faction.client;

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

import com.faction.dao.FactionDao;
import com.faction.entity.Faction;
import com.faction.service.FactionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
class FactionControllerTests {

	@Test
	void contextLoads() {
	}
	
	//@Autowired
	//private FactionService factionService;
	@MockBean
	private FactionDao factionDao;
	@Autowired
    private MockMvc mockMvc;
	@MockBean
	private FactionService factionService;
	
	
	// Controller Tests
	
    @Test
    @DisplayName("GET /faction/all-faction success")
    void testGetFactionsSuccess() throws Exception {
        // Setup our mocked service
        Faction faction1 = new Faction(1, "Faction Name", "Description");
        Faction faction2 = new Faction(2, "Faction 2 Name", "Description 2");
        doReturn(Lists.newArrayList(faction1, faction2)).when(factionService).getAllFaction();

        // Execute the GET request
        mockMvc.perform(get("/faction/all-faction"))
        // Validate the response code and content type
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        // Validate the returned fields
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].factionPK", is(1)))
        .andExpect(jsonPath("$[0].factionName", is("Faction Name")))
        .andExpect(jsonPath("$[0].factionDescription", is("Description")))
        .andExpect(jsonPath("$[1].factionPK", is(2)))
        .andExpect(jsonPath("$[1].factionName", is("Faction 2 Name")))
        .andExpect(jsonPath("$[1].factionDescription", is("Description 2")))
;
    }
	
    
    @Test
    @DisplayName("GET /faction/1")
    void testGetFactionById() throws Exception {
    	
    	// Setup our mocked service
        Faction faction = new Faction(1, "Faction Name", "Description");
        doReturn(faction).when(factionService).getFactionById(1);

        // Execute the GET request
        mockMvc.perform(get("/faction/{factionPK}", 1))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.factionPK", is(1)))
                .andExpect(jsonPath("$.factionName", is("Faction Name")));
    }
    
    @Test
    @DisplayName("GET /faction/1 - Not Found")
    void testGetFactionByIdNotFound() throws Exception {
        // Setup our mocked service
    	doReturn(null).when(factionService).getFactionById(1);

        // Execute the GET request
        mockMvc.perform(get("/faction/{factionPK}", 1))
                // Validate the response code
                .andExpect(status().isNoContent());
    }
    
    @Test
    @DisplayName("/faction/new-faction")
    void testCreateFaction() throws Exception {
        // Setup our mocked service
        Faction factionToPost = new Faction(0, "New Faction", "Description");
        Faction factionToReturn = new Faction(1, "New Faction", "Description");
        doReturn(factionToReturn).when(factionService).createFaction(any());

        // Execute the POST request
        mockMvc.perform(post("/faction/new-faction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(factionToPost)))

                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.factionPK", is(1)))
                .andExpect(jsonPath("$.factionName", is("New Faction")));
    }
    
    @Test
    @DisplayName("PUT /faction/update-faction/{factionPK}")
    void testUpdateFaction() throws Exception {
        // Setup our mocked service
        Faction originalFaction = new Faction(1, "Original Faction", "Description");
        Faction updatedFaction = new Faction(1, "Updated Faction", "Description");
        doReturn(originalFaction).when(factionService).getFactionById(1);
        doReturn(true).when(factionService).updateFaction(any());

        // Execute the PUT request
        mockMvc.perform(put("/faction/update-faction/{factionPK}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedFaction)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.factionPK", is(1)))
                .andExpect(jsonPath("$.factionName", is("Updated Faction")));
    }
    
    @Test
    @DisplayName("PUT /faction/update-faction/{factionPK} - Not Found")
    void testUpdateFactionNotFound() throws Exception {
        // Setup our mocked service
        Faction factionToPut = new Faction(1, "New Faction", "Description");
        
        doReturn(null).when(factionService).getFactionById(1);
        doReturn(false).when(factionService).updateFaction(factionToPut);

        // Execute the PUT request
        mockMvc.perform(put("/faction/update-faction/{factionPK}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(factionToPut)))
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
