package com.myth.service.micro;

import java.util.List;

import com.myth.entity.Species;

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
