package com.ability.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ability.dao.AbilityDao;
import com.ability.entity.Ability;

@Service
public class AbilityServiceImpl implements AbilityService {

	@Autowired
	private AbilityDao abilityDao;
	
	// Create
	
	@Override
	public Ability createAbility(Ability ability) {
		
		if(abilityDao.save(ability) != null) {
			return ability;
		} 
		else {
			return null;
		}
		
	}
	
	//Read

	@Override
	public List<Ability> getAllAbility() {
		
		return abilityDao.findAll();
	}

	@Override
	public Ability getAbilityById(int abilityPK) {
		
		Ability ability = abilityDao.findById(abilityPK).orElse(null);
		return ability;
	}

	@Override
	public Ability getAbilityByName(String abilityName) {
		
		return abilityDao.findByName(abilityName);
	}

	// Update
	
	@Override
	public Boolean updateAbility(Ability ability) {
		
		Ability abilityExists = getAbilityById(ability.getAbilityPK());
		
		if(abilityExists !=null) {
			
			abilityDao.save(ability);
			return true;
			
		}
		return false;
	}

	// Delete
	
	@Override
	public Boolean deleteAbility(int abilityPK) {
		
		Ability ability = getAbilityById(abilityPK);
		
		if(ability !=null) {
			
			abilityDao.deleteById(abilityPK);
			return true;
			
		}
		return false;
	}

}
