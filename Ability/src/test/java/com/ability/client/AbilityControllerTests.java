package com.ability.client;

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

import com.ability.dao.AbilityDao;
import com.ability.entity.Ability;
import com.ability.service.AbilityService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
class AbilityControllerTests {

	@Test
	void contextLoads() {
	}
	
	//@Autowired
	//private AbilityService abilityService;
	@MockBean
	private AbilityDao abilityDao;
	@Autowired
    private MockMvc mockMvc;
	@MockBean
	private AbilityService abilityService;
	
	
	// Controller Tests
	
    @Test
    @DisplayName("GET /ability/all-ability success")
    void testGetAbilitysSuccess() throws Exception {
        // Setup our mocked service
        Ability ability1 = new Ability(1, "Ability Name");
        Ability ability2 = new Ability(2, "Ability 2 Name");
        doReturn(Lists.newArrayList(ability1, ability2)).when(abilityService).getAllAbility();

        // Execute the GET request
        mockMvc.perform(get("/ability/all-ability"))
        // Validate the response code and content type
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        // Validate the returned fields
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].abilityPK", is(1)))
        .andExpect(jsonPath("$[0].abilityName", is("Ability Name")))
        .andExpect(jsonPath("$[1].abilityPK", is(2)))
        .andExpect(jsonPath("$[1].abilityName", is("Ability 2 Name")));
    }
	
    
    @Test
    @DisplayName("GET /ability/1")
    void testGetAbilityById() throws Exception {
    	
    	// Setup our mocked service
        Ability ability = new Ability(1, "Ability Name");
        doReturn(ability).when(abilityService).getAbilityById(1);

        // Execute the GET request
        mockMvc.perform(get("/ability/{abilityPK}", 1))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.abilityPK", is(1)))
                .andExpect(jsonPath("$.abilityName", is("Ability Name")));
    }
    
    @Test
    @DisplayName("GET /ability/1 - Not Found")
    void testGetAbilityByIdNotFound() throws Exception {
        // Setup our mocked service
    	doReturn(null).when(abilityService).getAbilityById(1);

        // Execute the GET request
        mockMvc.perform(get("/ability/{abilityPK}", 1))
                // Validate the response code
                .andExpect(status().isNoContent());
    }
    
    @Test
    @DisplayName("/ability/new-ability")
    void testCreateAbility() throws Exception {
        // Setup our mocked service
        Ability abilityToPost = new Ability(0, "New Ability");
        Ability abilityToReturn = new Ability(1, "New Ability");
        doReturn(abilityToReturn).when(abilityService).createAbility(any());

        // Execute the POST request
        mockMvc.perform(post("/ability/new-ability")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(abilityToPost)))

                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.abilityPK", is(1)))
                .andExpect(jsonPath("$.abilityName", is("New Ability")));
    }
    
    @Test
    @DisplayName("PUT /ability/update-ability/{abilityPK}")
    void testUpdateAbility() throws Exception {
        // Setup our mocked service
        Ability originalAbility = new Ability(1, "Original Ability");
        Ability updatedAbility = new Ability(1, "Updated Ability");
        doReturn(originalAbility).when(abilityService).getAbilityById(1);
        doReturn(true).when(abilityService).updateAbility(any());

        // Execute the PUT request
        mockMvc.perform(put("/ability/update-ability/{abilityPK}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedAbility)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.abilityPK", is(1)))
                .andExpect(jsonPath("$.abilityName", is("Updated Ability")));
    }
    
    @Test
    @DisplayName("PUT /ability/update-ability/{abilityPK} - Not Found")
    void testUpdateAbilityNotFound() throws Exception {
        // Setup our mocked service
        Ability abilityToPut = new Ability(1, "New Ability");
        
        doReturn(null).when(abilityService).getAbilityById(1);
        doReturn(false).when(abilityService).updateAbility(abilityToPut);

        // Execute the PUT request
        mockMvc.perform(put("/ability/update-ability/{abilityPK}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(abilityToPut)))
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
