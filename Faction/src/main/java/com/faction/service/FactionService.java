package com.faction.service;

import java.util.List;

import com.faction.entity.Faction;

public interface FactionService {
	
	// Create
	Faction createFaction(Faction species);
	
	// Read
	List<Faction> getAllFaction();
	Faction getFactionById(int speciesPK);
	Faction getFactionByName(String speciesName);
	
	// Update
	Boolean updateFaction(Faction species);
	
	// Delete
	Boolean deleteFaction(int speciesPK);
	

}
