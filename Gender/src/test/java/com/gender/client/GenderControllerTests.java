package com.gender.client;

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

import com.gender.dao.GenderDao;
import com.gender.entity.Gender;
import com.gender.service.GenderService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
class GenderControllerTests {

	@Test
	void contextLoads() {
	}
	
	//@Autowired
	//private GenderService genderService;
	@MockBean
	private GenderDao genderDao;
	@Autowired
    private MockMvc mockMvc;
	@MockBean
	private GenderService genderService;
	
	
	// Controller Tests
	
    @Test
    @DisplayName("GET /gender/all-gender success")
    void testGetGendersSuccess() throws Exception {
        // Setup our mocked service
        Gender gender1 = new Gender(1, "Gender Name");
        Gender gender2 = new Gender(2, "Gender 2 Name");
        doReturn(Lists.newArrayList(gender1, gender2)).when(genderService).getAllGender();

        // Execute the GET request
        mockMvc.perform(get("/gender/all-gender"))
        // Validate the response code and content type
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        // Validate the returned fields
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].genderPK", is(1)))
        .andExpect(jsonPath("$[0].genderName", is("Gender Name")))
        .andExpect(jsonPath("$[1].genderPK", is(2)))
        .andExpect(jsonPath("$[1].genderName", is("Gender 2 Name")));
    }
	
    
    @Test
    @DisplayName("GET /gender/1")
    void testGetGenderById() throws Exception {
    	
    	// Setup our mocked service
        Gender gender = new Gender(1, "Gender Name");
        doReturn(gender).when(genderService).getGenderById(1);

        // Execute the GET request
        mockMvc.perform(get("/gender/{genderPK}", 1))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.genderPK", is(1)))
                .andExpect(jsonPath("$.genderName", is("Gender Name")));
    }
    
    @Test
    @DisplayName("GET /gender/1 - Not Found")
    void testGetGenderByIdNotFound() throws Exception {
        // Setup our mocked service
    	doReturn(null).when(genderService).getGenderById(1);

        // Execute the GET request
        mockMvc.perform(get("/gender/{genderPK}", 1))
                // Validate the response code
                .andExpect(status().isNoContent());
    }
    
    @Test
    @DisplayName("/gender/new-gender")
    void testCreateGender() throws Exception {
        // Setup our mocked service
        Gender genderToPost = new Gender(0, "New Gender");
        Gender genderToReturn = new Gender(1, "New Gender");
        doReturn(genderToReturn).when(genderService).createGender(any());

        // Execute the POST request
        mockMvc.perform(post("/gender/new-gender")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(genderToPost)))

                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.genderPK", is(1)))
                .andExpect(jsonPath("$.genderName", is("New Gender")));
    }
    
    @Test
    @DisplayName("PUT /gender/update-gender/{genderPK}")
    void testUpdateGender() throws Exception {
        // Setup our mocked service
        Gender originalGender = new Gender(1, "Original Gender");
        Gender updatedGender = new Gender(1, "Updated Gender");
        doReturn(originalGender).when(genderService).getGenderById(1);
        doReturn(true).when(genderService).updateGender(any());

        // Execute the PUT request
        mockMvc.perform(put("/gender/update-gender/{genderPK}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedGender)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.genderPK", is(1)))
                .andExpect(jsonPath("$.genderName", is("Updated Gender")));
    }
    
    @Test
    @DisplayName("PUT /gender/update-gender/{genderPK} - Not Found")
    void testUpdateGenderNotFound() throws Exception {
        // Setup our mocked service
        Gender genderToPut = new Gender(1, "New Gender");
        
        doReturn(null).when(genderService).getGenderById(1);
        doReturn(false).when(genderService).updateGender(genderToPut);

        // Execute the PUT request
        mockMvc.perform(put("/gender/update-gender/{genderPK}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(genderToPut)))
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
