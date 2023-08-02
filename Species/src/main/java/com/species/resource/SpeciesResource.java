package com.species.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.species.entity.Species;
import com.species.service.SpeciesService;


@Controller
public class SpeciesResource {
	
	@Autowired
	private SpeciesService speciesService;
	
	// Create
	@PostMapping(path = "/species/new-species", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Species> createSpecies(@RequestBody Species species) {

		Species newSpecies = speciesService.createSpecies(species);

		if (newSpecies != null) {
			return new ResponseEntity<>(newSpecies, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}


	// Read
	// Get by ID
	@CrossOrigin
	@GetMapping(path = "/species/{speciesPK}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Species> getSpeciesById(@PathVariable int speciesPK){

		Species species = speciesService.getSpeciesById(speciesPK);

		if (species != null) {
			return new ResponseEntity<Species>(species, HttpStatus.OK);
		}else {
			return new ResponseEntity<Species>(species, HttpStatus.NO_CONTENT);
		}
	}
	
	// Read
	// Get by Name
	@CrossOrigin
	@GetMapping(path = "/species/species-by-name/{speciesName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Species> getSpeciesByName(@PathVariable String speciesName){

		Species species = speciesService.getSpeciesByName(speciesName);

		if (species != null) {
			return new ResponseEntity<Species>(species, HttpStatus.OK);
		}else {
			return new ResponseEntity<Species>(species, HttpStatus.NO_CONTENT);
		}
	}

	// Read
	// Get all
	@GetMapping(path = "/species/all-species", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Species>> getAllSpecies(){

		List<Species> speciesList = speciesService.getAllSpecies();

		if(speciesList.size() > 0) {
			return new ResponseEntity<List<Species>>(speciesList, HttpStatus.OK);
		}else {
			return new ResponseEntity<List<Species>>(speciesList, HttpStatus.NO_CONTENT);
		}
	}
	
	// Update
	@PutMapping(path = "/species/update-species/{speciesPK}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Species> updateSpecies(@PathVariable int speciesPK, @RequestBody Species species){

		boolean updated = speciesService.updateSpecies(species);
		Species speciesUpdated = speciesService.getSpeciesById(speciesPK);
		if(updated) {
			return new ResponseEntity<Species>(species, HttpStatus.OK);
		}else {
			return new ResponseEntity<Species>(species, HttpStatus.NO_CONTENT);
		}

	}

	// Delete
	@DeleteMapping(path = "/species/delete-species/{speciesPK}")
	public ResponseEntity<Boolean> deleteSpecies(@PathVariable int speciesPK){

		boolean deleted = speciesService.deleteSpecies(speciesPK);

		if(deleted) {
			return new ResponseEntity<Boolean>(deleted, HttpStatus.OK);
		}else {
			return new ResponseEntity<Boolean>(deleted, HttpStatus.NO_CONTENT);
		}
	}
}
