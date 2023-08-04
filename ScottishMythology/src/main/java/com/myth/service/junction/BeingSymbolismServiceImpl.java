package com.myth.service.junction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.myth.dao.junction.BeingSymbolismDao;
import com.myth.entity.junction.BeingSymbolism;
import com.myth.entity.junction.KeyBeingSymbolism;

@Service
public class BeingSymbolismServiceImpl implements BeingSymbolismService {

	@Autowired
	BeingSymbolismDao beingSymbolismDao;
	
	// Create
	
	@Override
	public BeingSymbolism createBeingSymbolism(BeingSymbolism beingSymbolism) {
		
		if(beingSymbolismDao.save(beingSymbolism) != null) {
			return beingSymbolism;
		} 
		else {
			return null;
		}
	}

	@Override
	public List<BeingSymbolism> getAllBeingSymbolism() {
		
		return beingSymbolismDao.findAll();
	}

	@Override
	public BeingSymbolism getBeingSymbolismById(KeyBeingSymbolism beingSymbolismPK) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BeingSymbolism> getBeingSymbolismByBeingId(int beingPK) {
		
		List<BeingSymbolism> beingAbilities = beingSymbolismDao.findByBeing(beingPK);
		
		if(beingAbilities.size() >0) {
			
			return beingAbilities;
		}
		
		return null;
	}

	@Override
	public List<BeingSymbolism> getBeingSymbolismBySymbolismId(int symbolPK) {

		List<BeingSymbolism> beingAbilities = beingSymbolismDao.findByBeing(symbolPK);

		if(beingAbilities.size() >0) {

			return beingAbilities;
		}

		return null;
	}

	@Override
	public Boolean updateBeingSymbolism(BeingSymbolism beingSymbolism) {

		BeingSymbolism beingSymbolismExists = getBeingSymbolismById(beingSymbolism.getId());

		if(beingSymbolismExists !=null) {

			beingSymbolismDao.save(beingSymbolism);
			return true;

		}
		return false;
	}

	@Override
	public Boolean deleteBeingSymbolism(KeyBeingSymbolism beingSymbolismPK) {

		BeingSymbolism beingSymbolism = getBeingSymbolismById(beingSymbolismPK);

		if(beingSymbolism !=null) {

			beingSymbolismDao.deleteById(beingSymbolismPK);
			return true;

		}
		return false;
	}


}
