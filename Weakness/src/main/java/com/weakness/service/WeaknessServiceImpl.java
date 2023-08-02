package com.weakness.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weakness.dao.WeaknessDao;
import com.weakness.entity.Weakness;

@Service
public class WeaknessServiceImpl implements WeaknessService {

	@Autowired
	private WeaknessDao weaknessDao;
	
	// Create
	
	@Override
	public Weakness createWeakness(Weakness weakness) {
		
		if(weaknessDao.save(weakness) != null) {
			return weakness;
		} 
		else {
			return null;
		}
		
	}
	
	//Read

	@Override
	public List<Weakness> getAllWeakness() {
		
		return weaknessDao.findAll();
	}

	@Override
	public Weakness getWeaknessById(int weaknessPK) {
		
		Weakness weakness = weaknessDao.findById(weaknessPK).orElse(null);
		return weakness;
	}

	@Override
	public Weakness getWeaknessByName(String weaknessName) {
		
		return weaknessDao.findByName(weaknessName);
	}

	// Update
	
	@Override
	public Boolean updateWeakness(Weakness weakness) {
		
		Weakness weaknessExists = getWeaknessById(weakness.getWeaknessPK());
		
		if(weaknessExists !=null) {
			
			weaknessDao.save(weakness);
			return true;
			
		}
		return false;
	}

	// Delete
	
	@Override
	public Boolean deleteWeakness(int weaknessPK) {
		
		Weakness weakness = getWeaknessById(weaknessPK);
		
		if(weakness !=null) {
			
			weaknessDao.deleteById(weaknessPK);
			return true;
			
		}
		return false;
	}

}
