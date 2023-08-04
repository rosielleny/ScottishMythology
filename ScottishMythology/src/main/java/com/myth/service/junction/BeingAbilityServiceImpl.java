package com.myth.service.junction;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.myth.dao.junction.BeingAbilityDao;
import com.myth.entity.junction.BeingAbility;
import com.myth.entity.junction.KeyBeingAbility;

@Service
public class BeingAbilityServiceImpl implements BeingAbilityService {

	@Autowired
	BeingAbilityDao beingAbilityDao;
	
	// Create
	
	@Override
	public BeingAbility createBeingAbility(BeingAbility beingAbility) {
		
		if(beingAbilityDao.save(beingAbility) != null) {
			return beingAbility;
		} 
		else {
			return null;
		}
	}

	@Override
	public List<BeingAbility> getAllBeingAbility() {
		
		return beingAbilityDao.findAll();
	}

	@Override
	public BeingAbility getBeingAbilityById(KeyBeingAbility beingAbilityPK) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BeingAbility> getBeingAbilityByBeingId(int beingPK) {
		
		List<BeingAbility> beingAbilities = beingAbilityDao.findByBeing(beingPK);
		
		if(beingAbilities.size() >0) {
			
			return beingAbilities;
		}
		
		return null;
	}

	@Override
	public List<BeingAbility> getBeingAbilityByAbilityId(int abilityPK) {

		List<BeingAbility> beingAbilities = beingAbilityDao.findByBeing(abilityPK);

		if(beingAbilities.size() >0) {

			return beingAbilities;
		}

		return null;
	}

	@Override
	public Boolean updateBeingAbility(BeingAbility beingAbility) {

		BeingAbility beingAbilityExists = getBeingAbilityById(beingAbility.getId());

		if(beingAbilityExists !=null) {

			beingAbilityDao.save(beingAbility);
			return true;

		}
		return false;
	}

	@Override
	public Boolean deleteBeingAbility(KeyBeingAbility beingAbilityPK) {

		BeingAbility beingAbility = getBeingAbilityById(beingAbilityPK);

		if(beingAbility !=null) {

			beingAbilityDao.deleteById(beingAbilityPK);
			return true;

		}
		return false;
	}

}
