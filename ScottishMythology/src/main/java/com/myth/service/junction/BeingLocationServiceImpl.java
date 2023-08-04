package com.myth.service.junction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.myth.dao.junction.BeingLocationDao;
import com.myth.entity.junction.BeingLocation;
import com.myth.entity.junction.KeyBeingLocation;


@Service
public class BeingLocationServiceImpl implements BeingLocationService {

	@Autowired
	BeingLocationDao beingLocationDao;
	
	// Create
	
	@Override
	public BeingLocation createBeingLocation(BeingLocation beingLocation) {
		
		if(beingLocationDao.save(beingLocation) != null) {
			return beingLocation;
		} 
		else {
			return null;
		}
	}

	@Override
	public List<BeingLocation> getAllBeingLocation() {
		
		return beingLocationDao.findAll();
	}

	@Override
	public BeingLocation getBeingLocationById(KeyBeingLocation beingLocationPK) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BeingLocation> getBeingLocationByBeingId(int beingPK) {
		
		List<BeingLocation> beingAbilities = beingLocationDao.findByBeing(beingPK);
		
		if(beingAbilities.size() >0) {
			
			return beingAbilities;
		}
		
		return null;
	}

	@Override
	public List<BeingLocation> getBeingLocationByLocationId(int locationPK) {

		List<BeingLocation> beingAbilities = beingLocationDao.findByBeing(locationPK);

		if(beingAbilities.size() >0) {

			return beingAbilities;
		}

		return null;
	}

	@Override
	public Boolean updateBeingLocation(BeingLocation beingLocation) {

		BeingLocation beingLocationExists = getBeingLocationById(beingLocation.getId());

		if(beingLocationExists !=null) {

			beingLocationDao.save(beingLocation);
			return true;

		}
		return false;
	}

	@Override
	public Boolean deleteBeingLocation(KeyBeingLocation beingLocationPK) {

		BeingLocation beingLocation = getBeingLocationById(beingLocationPK);

		if(beingLocation !=null) {

			beingLocationDao.deleteById(beingLocationPK);
			return true;

		}
		return false;
	}

}
