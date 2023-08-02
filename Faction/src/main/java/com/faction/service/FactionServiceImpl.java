package com.faction.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faction.dao.FactionDao;
import com.faction.entity.Faction;

@Service
public class FactionServiceImpl implements FactionService {

	@Autowired
	private FactionDao factionDao;
	
	// Create
	
	@Override
	public Faction createFaction(Faction faction) {
		
		if(factionDao.save(faction) != null) {
			return faction;
		} 
		else {
			return null;
		}
		
	}
	
	//Read

	@Override
	public List<Faction> getAllFaction() {
		
		return factionDao.findAll();
	}

	@Override
	public Faction getFactionById(int factionPK) {
		
		Faction faction = factionDao.findById(factionPK).orElse(null);
		return faction;
	}

	@Override
	public Faction getFactionByName(String factionName) {
		
		return factionDao.findByName(factionName);
	}

	// Update
	
	@Override
	public Boolean updateFaction(Faction faction) {
		
		Faction factionExists = getFactionById(faction.getFactionPK());
		
		if(factionExists !=null) {
			
			factionDao.save(faction);
			return true;
			
		}
		return false;
	}

	// Delete
	
	@Override
	public Boolean deleteFaction(int factionPK) {
		
		Faction faction = getFactionById(factionPK);
		
		if(faction !=null) {
			
			factionDao.deleteById(factionPK);
			return true;
			
		}
		return false;
	}

}
