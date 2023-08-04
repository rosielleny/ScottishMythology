package com.myth.service.junction;

import java.util.List;

import com.myth.entity.junction.BeingStory;
import com.myth.entity.junction.KeyBeingStory;
import com.myth.service.EntityService;

public interface BeingStoryService{

	// Create
	BeingStory createBeingStory(BeingStory beingStory);

	// Read
	List<BeingStory> getAllBeingStory();
	BeingStory getBeingStoryById(KeyBeingStory beingStoryPK);
	List<BeingStory> getBeingStoryByBeingId(int beingPK);
	List<BeingStory> getBeingStoryByStoryId(int StoryPK);

	// Update
	Boolean updateBeingStory(BeingStory beingStory);

	// Delete
	Boolean deleteBeingStory(KeyBeingStory beingStoryPK);

}
