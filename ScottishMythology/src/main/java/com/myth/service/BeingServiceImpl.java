package com.myth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myth.dao.BeingDao;
import com.myth.entity.Being;

@Service
public class BeingServiceImpl implements BeingService {

	@Autowired
	BeingDao beingDao;
	
	//CREATE 
	
	@Override
	public Being createBeing(Being being) {

		if(beingDao.save(being) != null) {
			return being;
		} 
		else {
			return null;
		}
	}

	// READ
	
	@Override
	public List<Being> getAllBeing() {

		return beingDao.findAll();
	}

	@Override
	public Being getBeingById(int beingPK) {

		Being being = beingDao.findById(beingPK).orElse(null);
		return being;
	}

	@Override
	public Being getBeingByName(String beingName) {

		return beingDao.findByName(beingName);
	}
	
	@Override
	public List<Being> getBeingByFaction(int factionPK){
		
		return beingDao.findBeingByFaction(factionPK);
	}
	
	@Override
	public List<Being> getBeingByGender(int genderPK){
		
		return beingDao.findBeingByGender(genderPK);
	}
	
	@Override
	public List<Being> getBeingBySpecies(int speciesPK){
		
		return beingDao.findBeingBySpecies(speciesPK);
	}
	
	
	//UPDATE

	@Override
	public Boolean updateBeing(Being being) {

		Being beingExists = getBeingById(being.getBeingPK());

		if(beingExists !=null) {

			beingDao.save(being);
			return true;

		}
		return false;
	}

	@Override
	public Boolean deleteBeing(int beingPK) {
		
		Being being = getBeingById(beingPK);

		if(being !=null) {

			beingDao.deleteById(beingPK);
			
			return true;

		}
		return false;
	}

}
