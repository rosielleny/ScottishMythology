package com.story.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.story.entity.Story;
import com.story.dao.StoryDao;

@Service
public class StoryServiceImpl implements StoryService {

	@Autowired
	private StoryDao storyDao;
	
	// Create
	
	@Override
	public Story createStory(Story story) {
		
		if(storyDao.save(story) != null) {
			Story createdStory = getStoryByName(story.getStoryName());
			return createdStory;
		} 
		else {
			return null;
		}
		
	}
	
	//Read

	@Override
	public List<Story> getAllStory() {
		
		return storyDao.findAll();
	}

	@Override
	public Story getStoryById(int storyPK) {
		
		Story story = storyDao.findById(storyPK).orElse(null);
		return story;
	}

	@Override
	public Story getStoryByName(String storyName) {
		
		return storyDao.findByName(storyName);
	}

	// Update
	
	@Override
	public Boolean updateStory(Story story) {
		
		Story storyExists = getStoryById(story.getStoryPK());
		
		if(storyExists !=null) {
			
			storyDao.save(story);
			return true;
			
		}
		return false;
	}

	// Delete
	
	@Override
	public Boolean deleteStory(int storyPK) {
		
		Story story = getStoryById(storyPK);
		
		if(story !=null) {
			
			storyDao.deleteById(storyPK);
			return true;
			
		}
		return false;
	}

}
