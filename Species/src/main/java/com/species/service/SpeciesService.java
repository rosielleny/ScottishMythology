package com.species.service;

import java.util.List;

import com.species.entity.Species;

public interface SpeciesService {
	
	// Create
	Species createSpecies(Species species);
	
	// Read
	List<Species> getAllSpecies();
	Species getSpeciesById(int speciesPK);
	Species getSpeciesByName(String speciesName);
	
	// Update
	Boolean updateSpecies(Species species);
	
	// Delete
	Boolean deleteSpecies(int speciesPK);
	

}
