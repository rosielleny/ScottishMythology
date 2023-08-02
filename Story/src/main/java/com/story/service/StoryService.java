package com.story.service;

import java.util.List;

import com.story.entity.Story;

public interface StoryService {
	
	// Create
	Story createStory(Story story);
	
	// Read
	List<Story> getAllStory();
	Story getStoryById(int storyPK);
	Story getStoryByName(String storyName);
	
	// Update
	Boolean updateStory(Story story);
	
	// Delete
	Boolean deleteStory(int storyPK);
	

}
