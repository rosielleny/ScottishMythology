package com.species.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.species.dao.SpeciesDao;
import com.species.entity.Species;

@Service
public class SpeciesServiceImpl implements SpeciesService {

	@Autowired
	private SpeciesDao speciesDao;
	
	// Create
	
	@Override
	public Species createSpecies(Species species) {
		
		if(speciesDao.save(species) != null) {
			return species;
		} 
		else {
			return null;
		}
		
	}
	
	//Read

	@Override
	public List<Species> getAllSpecies() {
		
		return speciesDao.findAll();
	}

	@Override
	public Species getSpeciesById(int speciesPK) {
		
		Species species = speciesDao.findById(speciesPK).orElse(null);
		return species;
	}

	@Override
	public Species getSpeciesByName(String speciesName) {
		
		return speciesDao.findByName(speciesName);
	}

	// Update
	
	@Override
	public Boolean updateSpecies(Species species) {
		
		Species speciesExists = getSpeciesById(species.getSpeciesPK());
		
		if(speciesExists !=null) {
			
			speciesDao.save(species);
			return true;
			
		}
		return false;
	}

	// Delete
	
	@Override
	public Boolean deleteSpecies(int speciesPK) {
		
		Species species = getSpeciesById(speciesPK);
		
		if(species !=null) {
			
			speciesDao.deleteById(speciesPK);
			return true;
			
		}
		return false;
	}

}
