package com.story.client;

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

import com.story.dao.StoryDao;
import com.story.entity.Story;
import com.story.service.StoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
class StoryControllerTests {

	@Test
	void contextLoads() {
	}
	
	//@Autowired
	//private StoryService storyService;
	@MockBean
	private StoryDao storyDao;
	@Autowired
    private MockMvc mockMvc;
	@MockBean
	private StoryService storyService;
	
	
	// Controller Tests
	
    @Test
    @DisplayName("GET /story/all-story success")
    void testGetStorysSuccess() throws Exception {
        // Setup our mocked service
        Story story1 = new Story(1, "Story Name", "Description");
        Story story2 = new Story(2, "Story 2 Name", "Description 2");
        doReturn(Lists.newArrayList(story1, story2)).when(storyService).getAllStory();

        // Execute the GET request
        mockMvc.perform(get("/story/all-story"))
        // Validate the response code and content type
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        // Validate the returned fields
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].storyPK", is(1)))
        .andExpect(jsonPath("$[0].storyName", is("Story Name")))
        .andExpect(jsonPath("$[0].storyDescription", is("Description")))
        .andExpect(jsonPath("$[1].storyPK", is(2)))
        .andExpect(jsonPath("$[1].storyName", is("Story 2 Name")))
        .andExpect(jsonPath("$[1].storyDescription", is("Description 2")));
    }
	
    
    @Test
    @DisplayName("GET /story/1")
    void testGetStoryById() throws Exception {
    	
    	// Setup our mocked service
        Story story = new Story(1, "Story Name", "Description");
        doReturn(story).when(storyService).getStoryById(1);

        // Execute the GET request
        mockMvc.perform(get("/story/{storyPK}", 1))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.storyPK", is(1)))
                .andExpect(jsonPath("$.storyName", is("Story Name")));
    }
    
    @Test
    @DisplayName("GET /story/1 - Not Found")
    void testGetStoryByIdNotFound() throws Exception {
        // Setup our mocked service
    	doReturn(null).when(storyService).getStoryById(1);

        // Execute the GET request
        mockMvc.perform(get("/story/{storyPK}", 1))
                // Validate the response code
                .andExpect(status().isNoContent());
    }
    
    @Test
    @DisplayName("/story/new-story")
    void testCreateStory() throws Exception {
        // Setup our mocked service
        Story storyToPost = new Story(0, "New Story", "Description");
        Story storyToReturn = new Story(1, "New Story", "Description");
        doReturn(storyToReturn).when(storyService).createStory(any());

        // Execute the POST request
        mockMvc.perform(post("/story/new-story")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(storyToPost)))

                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.storyPK", is(1)))
                .andExpect(jsonPath("$.storyName", is("New Story")));
    }
    
    @Test
    @DisplayName("PUT /story/update-story/{storyPK}")
    void testUpdateStory() throws Exception {
        // Setup our mocked service
        Story originalStory = new Story(1, "Original Story", "Description");
        Story updatedStory = new Story(1, "Updated Story", "Description");
        doReturn(originalStory).when(storyService).getStoryById(1);
        doReturn(true).when(storyService).updateStory(any());

        // Execute the PUT request
        mockMvc.perform(put("/story/update-story/{storyPK}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedStory)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.storyPK", is(1)))
                .andExpect(jsonPath("$.storyName", is("Updated Story")));
    }
    
    @Test
    @DisplayName("PUT /story/update-story/{storyPK} - Not Found")
    void testUpdateStoryNotFound() throws Exception {
        // Setup our mocked service
        Story storyToPut = new Story(1, "New Story", "Description");
        
        doReturn(null).when(storyService).getStoryById(1);
        doReturn(false).when(storyService).updateStory(storyToPut);

        // Execute the PUT request
        mockMvc.perform(put("/story/update-story/{storyPK}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(storyToPut)))
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
