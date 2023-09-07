package com.story.client;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import com.story.dao.StoryDao;
import com.story.entity.Story;
import com.story.service.StoryService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@AutoConfigureMockMvc
class StoryApplicationTests {

	@Test
	void contextLoads() {
	}
	
	//@Autowired
	//private StoryService storyService;
	@MockBean
	private StoryDao storyDao;
	@Autowired
    private MockMvc mockMvc;
	@Autowired
	private StoryService storyService;
	
	
	// Service Tests
	@Test
	@DisplayName("Test getting story by ID")
	void testGetByID() {
		
		// Setting up mock repository
		Story story = new Story(1, "TestStory", "Description");
		doReturn(Optional.of(story)).when(storyDao).findById(1);
		
		// Execute the service call
		Optional<Story> returnedStory = Optional.ofNullable(storyService.getStoryById(1));
		
		// Assert the response
		Assertions.assertTrue(returnedStory.isPresent(), "Story was not found");
		Assertions.assertSame(returnedStory.get(), story, "The story returned was not the same as the mock");
		
	}

	@Test
	@DisplayName("Test Not Found get story by ID")
	void testGetByIdNotFound() {
		// Setup our mock repository
		doReturn(Optional.empty()).when(storyDao).findById(1);

		// Execute the service call
		Optional<Story> returnedStory = Optional.ofNullable(storyService.getStoryById(1));

		// Assert the response
		Assertions.assertFalse(returnedStory.isPresent(), "Story should NOT be found, but was");
	}
	
	@Test
	@DisplayName("Test findAll")
	void testFindAll() {
		// Setup our mock repository
		Story story1 = new Story(1, "Story Name", "Description");
		Story story2 = new Story(2, "Story 2 Name", "Description");
		doReturn(Arrays.asList(story1, story2)).when(storyDao).findAll();
		when(storyDao.findAll()).thenReturn(Arrays.asList(story1, story2));

		// Execute the service call
		List<Story> storys = storyService.getAllStory();

		// Assert the response
		Assertions.assertEquals(2, storys.size(), "findAll should return 2 abilities");
	}
	
	@Test
	@DisplayName("Test create story")
	void testCreate() {
	    // Setup our mock repository
	    Story story = new Story(1, "Story Name", "Description");

	    // Mocking behaviour for storyDao.save
	    doAnswer(invocation -> {
	        Story argStory = invocation.getArgument(0);
	        return new Story(argStory.getStoryPK() + 1, argStory.getStoryName(), argStory.getStoryDescription());
	    }).when(storyDao).save(any());

	    // Mocking behaviour for getStoryByName
	    when(storyDao.findByName(anyString())).thenReturn(new Story(2, "Story Name", "Description"));

	    // Execute the service call
	    Story returnedStory = storyService.createStory(story);

	    // Assert the response
	    Assertions.assertNotNull(returnedStory, "The saved story should not be null");
	    Assertions.assertEquals(2, returnedStory.getStoryPK(), "The ID should be incremented");
	}


	
	@Test
	@DisplayName("Test delete story")
	void testDelete() {
	    // Setup our mock repository
	    Story story = new Story(1, "Story Name", "Description");
	    
	    doReturn(Optional.of(story)).when(storyDao).findById(1);

	    doNothing().when(storyDao).deleteById(1);

	    // Execute the service call
	    Boolean returnedStory = storyService.deleteStory(1);

	    // Assert the response
	    Assertions.assertNotNull(returnedStory, "The deleted story should not be null, should be boolean.");
	    
	    // After deletion, the story should not be found.
	    doReturn(Optional.empty()).when(storyDao).findById(1);
	    Assertions.assertNull(storyService.getStoryById(1));

	    Assertions.assertEquals(true, returnedStory, "Should return true if deleted.");
	}


	
	@Test
	@DisplayName("Test edit story")
	void testEdit() {
	    // Setup our mock repository
	    Story story = new Story(1, "Story Name", "Description");
	    
	    doReturn(Optional.of(story)).when(storyDao).findById(1);

	    // Execute the service call
	    Story updatedStory = new Story(1, "Updated Story Name", "Description");
	    storyService.updateStory(updatedStory);
	    
	    doReturn(Optional.of(updatedStory)).when(storyDao).findById(1);
	    
	    Story returnedStory = storyService.getStoryById(1);

	    // Assert the response
	    Assertions.assertNotNull(returnedStory, "The updated story should not be null");
	    Assertions.assertEquals(1, returnedStory.getStoryPK(), "The ID should be same as the one updated");
	    Assertions.assertEquals("Updated Story Name", returnedStory.getStoryName(), "The Name should be updated");
	}


}
