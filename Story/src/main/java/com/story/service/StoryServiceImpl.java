package com.story.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.story.dao.StoryDao;
import com.story.entity.Story;

@Service
public class StoryServiceImpl implements StoryService {

	@Autowired
	private StoryDao abilityDao;
	
	// Create
	
	@Override
	public Story createStory(Story ability) {
		
		if(abilityDao.save(ability) != null) {
			return ability;
		} 
		else {
			return null;
		}
		
	}
	
	//Read

	@Override
	public List<Story> getAllStory() {
		
		return abilityDao.findAll();
	}

	@Override
	public Story getStoryById(int abilityPK) {
		
		Story ability = abilityDao.findById(abilityPK).orElse(null);
		return ability;
	}

	@Override
	public Story getStoryByName(String abilityName) {
		
		return abilityDao.findByName(abilityName);
	}

	// Update
	
	@Override
	public Boolean updateStory(Story ability) {
		
		Story abilityExists = getStoryById(ability.getStoryPK());
		
		if(abilityExists !=null) {
			
			abilityDao.save(ability);
			return true;
			
		}
		return false;
	}

	// Delete
	
	@Override
	public Boolean deleteStory(int abilityPK) {
		
		Story ability = getStoryById(abilityPK);
		
		if(ability !=null) {
			
			abilityDao.deleteById(abilityPK);
			return true;
			
		}
		return false;
	}

}
