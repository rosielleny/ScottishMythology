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

import com.myth.controller.StoryController;
import com.myth.entity.Story;
import com.myth.entity.composite.GenericEntity;

import com.myth.service.micro.StoryService;

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
class StoryControllerTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private MockMvc mockMvc;


	@MockBean
	private StoryService storyService;
	
	@Autowired
	private StoryController storyController;
	
	// CREATE
	@Test
	public void testCreateAnStory() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/story/create"))
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
		Story createdStory = new Story();
		createdStory.setStoryPK(1);
		createdStory.setStoryName("New Story");
		when(storyService.getStoryById(any(Integer.class))).thenReturn(createdStory);
		when(storyService.createStory(any(Story.class))).thenReturn(createdStory);
		when(storyService.getStoryByName(any(String.class))).thenReturn(createdStory);

		mockMvc.perform(post("/story/save-new")
				.param("entityName", "") // Invalid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attributeHasFieldErrors("entity", "entityName")) // Expect validation error
		.andExpect(view().name("entity/create-entity")); // Expect view name
	}

	@Test
	@DisplayName("Test successful story creation")
	public void testSuccessfulStoryCreation() throws Exception {
		Story createdStory = new Story();
		createdStory.setStoryPK(1);
		createdStory.setStoryName("New Story");
		GenericEntity genEnt = new GenericEntity(createdStory.getStoryPK(), createdStory.getStoryName());
		when(storyService.getStoryById(any(Integer.class))).thenReturn(createdStory);
		when(storyService.createStory(any(Story.class))).thenReturn(createdStory);
		when(storyService.getStoryByName(any(String.class))).thenReturn(createdStory);

		mockMvc.perform(post("/story/save-new")
				.param("entityName", "New Story") // Valid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attribute("message", "New Story successfully added to the database.")) // Expect success message
		.andExpect(model().attribute("entity", samePropertyValuesAs(genEnt))) // Expect entity attribute to be updated
		.andExpect(view().name("entity/entity-output")); // Expect view name
	}

	@Test
	@DisplayName("Test error handling")
	public void testErrorHandling() throws Exception {
		// Mock an error during story creation
		when(storyService.createStory(any(Story.class))).thenReturn(null);

		// Create a POST request with valid input
		mockMvc.perform(post("/story/save-new")
				.param("entityName", "New Story") // Valid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attribute("message", "An error occurred. Story not added to the database.")) // Expect error message
		.andExpect(view().name("entity/entity-output")); // Expect view name
	}
	
	// READ
	
    @Test
    @DisplayName("Test show all stories")
    public void testShowAllStories() throws Exception {
        // Mock your storyService to return a list of Story objects
        List<Story> stories = new ArrayList<>();
        Story story1 = new Story(1, "Story 1", "Description 1");
        Story story2 = new Story(2, "Story 2", "Description 2");
        stories.add(story2);
        stories.add(story1);
        
        when(storyService.getAllStory()).thenReturn(stories);

        mockMvc.perform(get("/story/stories"))
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-all-entities")) // Expect view name
            .andExpect(model().attributeExists("entityList")) // Expect model attribute
            .andExpect(model().attribute("entityList", Matchers.hasSize(2))); // Expect entityList size to match the number of stories
    }
    
    @Test
    @DisplayName("Test search stories by name")
    public void testSearchStoriesByName() throws Exception {
        Story mockStory = new Story();
        mockStory.setStoryPK(1);
        mockStory.setStoryName("Mocked Story");
        
        when(storyService.getStoryByName("ExistingStory")).thenReturn(mockStory);
        when(storyService.getStoryByName("NonExistingStory")).thenReturn(null);
        
        mockMvc.perform(get("/story/search")
                .param("name", "ExistingStory")) // Valid input for an existing story
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-entity")) // Expect view name
            .andExpect(model().attributeExists("entity")) // Expect model attribute
            .andExpect(model().attribute("entity", Matchers.hasProperty("entityName", is("Mocked Story")))); // Expect entity attribute

        mockMvc.perform(get("/story/search")
                .param("name", "NonExistingStory")) // Valid input for a non-existing story
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-entity")) // Expect view name
            .andExpect(model().attribute("message", is("No stories were found for NonExistingStory"))); // Expect message attribute
    }
    
    // UPDATE
    
    @Test
    @DisplayName("Test edit stories by ID")
    public void testEditStoriesById() throws Exception {
        Story mockStory = new Story();
        mockStory.setStoryPK(1);
        mockStory.setStoryName("Mocked Story");
        
        when(storyService.getStoryById(1)).thenReturn(mockStory);
        
        mockMvc.perform(get("/story/edit")
                .param("pk", "1")) // Valid input for an existing story
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/edit-entity")) // Expect view name
            .andExpect(model().attributeExists("entity")) // Expect model attribute
            .andExpect(model().attributeExists("ogEntity")) // Expect model attribute
            .andExpect(model().attribute("entity", Matchers.hasProperty("entityName", is("Mocked Story")))); // Expect entity attribute
    }
    
    // DELETE
    
    @Test
    @DisplayName("Test successful story deletion")
    public void testSuccessfulStoryDeletion() throws Exception {
        int storyIdToDelete = 1;
        Story deletedStory = new Story();
        deletedStory.setStoryPK(storyIdToDelete);
        deletedStory.setStoryName("Deleted Story");
        List<Story> stories = new ArrayList<>();
        Story story1 = new Story(3, "Story 3", "Description 3");
        Story story2 = new Story(2, "Story 2", "Description 2");
        stories.add(story2);
        stories.add(story1);

        when(storyService.getStoryById(storyIdToDelete)).thenReturn(deletedStory);
        when(storyService.deleteStory(storyIdToDelete)).thenReturn(true);
        when(storyService.getAllStory()).thenReturn(stories);


        mockMvc.perform(get("/story/delete/{pk}", storyIdToDelete))
            .andExpect(status().isOk())
            .andExpect(model().attribute("message", "Deleted Story successfully deleted from Stories."))
            .andExpect(view().name("entity/show-all-entities"));

        verify(storyService, times(1)).getStoryById(storyIdToDelete);
        verify(storyService, times(1)).deleteStory(storyIdToDelete);
    }

    @Test
    @DisplayName("Test unsuccessful story deletion")
    public void testUnsuccessfulStoryDeletion() throws Exception {
        int storyIdToDelete = 1;
        Story storyToDelete = new Story();
        storyToDelete.setStoryPK(storyIdToDelete);
        storyToDelete.setStoryName("Deletion Failure");
        List<Story> stories = new ArrayList<>();
        Story story1 = new Story(3, "Story 3", "Description 3");
        Story story2 = new Story(2, "Story 2", "Description 2");
        stories.add(story2);
        stories.add(story1);
        stories.add(storyToDelete);
        

        when(storyService.getStoryById(storyIdToDelete)).thenReturn(storyToDelete);
        when(storyService.deleteStory(storyIdToDelete)).thenReturn(false);
        when(storyService.getAllStory()).thenReturn(stories);

        mockMvc.perform(get("/story/delete/{pk}", storyIdToDelete))
            .andExpect(status().isOk())
            .andExpect(model().attribute("message", "Failed to delete Deletion Failure from Stories."))
            .andExpect(view().name("entity/show-all-entities"));

        // Verify that the service method was called with the correct parameter
        verify(storyService, times(1)).getStoryById(storyIdToDelete);
        verify(storyService, times(1)).deleteStory(storyIdToDelete);
    }

    // EXTRA

    @Test
    public void testSneakyAddStory_Success() {
        // Create a sample Story object for testing
        Story story = new Story();
        story.setStoryName("Test Story");

        when(storyService.createStory(story)).thenReturn(story);

        // Make a POST request to the endpoint
        ResponseEntity<Map<String, Boolean>> responseEntity = storyController.sneakyAddStory(story);

        // Verify that the response is as expected
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().get("success"));
    }

    @Test
    public void testSneakyAddStory_Failure() {
        // Create a sample Story object for testing
        Story story = new Story();
        story.setStoryName("Test Story");

        when(storyService.createStory(story)).thenReturn(null);

        // Make a POST request to the endpoint
        ResponseEntity<Map<String, Boolean>> responseEntity = storyController.sneakyAddStory(story);

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
