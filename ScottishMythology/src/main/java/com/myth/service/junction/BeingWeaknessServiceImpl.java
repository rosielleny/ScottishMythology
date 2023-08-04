package com.myth.service.junction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.myth.dao.junction.BeingWeaknessDao;
import com.myth.entity.junction.BeingWeakness;
import com.myth.entity.junction.KeyBeingWeakness;

@Service
public class BeingWeaknessServiceImpl implements BeingWeaknessService {

	@Autowired
	BeingWeaknessDao beingWeaknessDao;
	
	// Create
	
	@Override
	public BeingWeakness createBeingWeakness(BeingWeakness beingWeakness) {
		
		if(beingWeaknessDao.save(beingWeakness) != null) {
			return beingWeakness;
		} 
		else {
			return null;
		}
	}

	@Override
	public List<BeingWeakness> getAllBeingWeakness() {
		
		return beingWeaknessDao.findAll();
	}

	@Override
	public BeingWeakness getBeingWeaknessById(KeyBeingWeakness beingWeaknessPK) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BeingWeakness> getBeingWeaknessByBeingId(int beingPK) {
		
		List<BeingWeakness> beingAbilities = beingWeaknessDao.findByBeing(beingPK);
		
		if(beingAbilities.size() >0) {
			
			return beingAbilities;
		}
		
		return null;
	}

	@Override
	public List<BeingWeakness> getBeingWeaknessByWeaknessId(int weaknessPK) {

		List<BeingWeakness> beingAbilities = beingWeaknessDao.findByBeing(weaknessPK);

		if(beingAbilities.size() >0) {

			return beingAbilities;
		}

		return null;
	}

	@Override
	public Boolean updateBeingWeakness(BeingWeakness beingWeakness) {

		BeingWeakness beingWeaknessExists = getBeingWeaknessById(beingWeakness.getId());

		if(beingWeaknessExists !=null) {

			beingWeaknessDao.save(beingWeakness);
			return true;

		}
		return false;
	}

	@Override
	public Boolean deleteBeingWeakness(KeyBeingWeakness beingWeaknessPK) {

		BeingWeakness beingWeakness = getBeingWeaknessById(beingWeaknessPK);

		if(beingWeakness !=null) {

			beingWeaknessDao.deleteById(beingWeaknessPK);
			return true;

		}
		return false;
	}

}
