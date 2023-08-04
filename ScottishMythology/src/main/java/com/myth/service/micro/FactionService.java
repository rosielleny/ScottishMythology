package com.myth.service.micro;

import java.util.List;

import com.myth.entity.Faction;

public interface FactionService {
	
	// Create
	Faction createFaction(Faction faction);
	
	// Read
	List<Faction> getAllFaction();
	Faction getFactionById(int factionPK);
	Faction getFactionByName(String factionName);
	
	// Update
	Boolean updateFaction(Faction faction);
	
	// Delete
	Boolean deleteFaction(int factionPK);
	

}
