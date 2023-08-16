package com.myth.service.junction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.myth.dao.junction.BeingStoryDao;
import com.myth.entity.junction.BeingStory;
import com.myth.entity.junction.KeyBeingStory;

@Service
public class BeingStoryServiceImpl implements BeingStoryService {

	@Autowired
	BeingStoryDao beingStoryDao;
	
	// Create
	
	@Override
	public BeingStory createBeingStory(BeingStory beingStory) {
		
		if(beingStoryDao.save(beingStory) != null) {
			return beingStory;
		} 
		else {
			return null;
		}
	}

	@Override
	public List<BeingStory> getAllBeingStory() {
		
		return beingStoryDao.findAll();
	}

	@Override
	public BeingStory getBeingStoryById(KeyBeingStory beingStoryPK) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BeingStory> getBeingStoryByBeingId(int beingPK) {
		
		List<BeingStory> beingStories = beingStoryDao.findByBeing(beingPK);
		
		if(beingStories.size() >0) {
			
			return beingStories;
		}
		
		return null;
	}

	@Override
	public List<BeingStory> getBeingStoryByStoryId(int storyPK) {

		List<BeingStory> beingAbilities = beingStoryDao.findByBeing(storyPK);

		if(beingAbilities.size() >0) {

			return beingAbilities;
		}

		return null;
	}

	@Override
	public Boolean updateBeingStory(BeingStory beingStory) {

		BeingStory beingStoryExists = getBeingStoryById(beingStory.getId());

		if(beingStoryExists !=null) {

			beingStoryDao.save(beingStory);
			return true;

		}
		return false;
	}

	@Override
	public Boolean deleteBeingStory(KeyBeingStory beingStoryPK) {

		BeingStory beingStory = getBeingStoryById(beingStoryPK);

		if(beingStory !=null) {

			beingStoryDao.deleteById(beingStoryPK);
			return true;

		}
		return false;
	}

}
