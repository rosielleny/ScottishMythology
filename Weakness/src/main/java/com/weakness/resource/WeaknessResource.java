package com.weakness.resource;

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

import com.weakness.entity.Weakness;
import com.weakness.service.WeaknessService;


@Controller
public class WeaknessResource {
	
	@Autowired
	private WeaknessService weaknessService;
	
	// Create
	@PostMapping(path = "/weakness/new-weakness", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Weakness> createWeakness(@RequestBody Weakness weakness) {

		Weakness newWeakness = weaknessService.createWeakness(weakness);

		if (newWeakness != null) {
			return new ResponseEntity<>(newWeakness, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}


	// Read
	// Get by ID
	@CrossOrigin
	@GetMapping(path = "/weakness/{weaknessPK}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Weakness> getWeaknessById(@PathVariable int weaknessPK){

		Weakness weakness = weaknessService.getWeaknessById(weaknessPK);

		if (weakness != null) {
			return new ResponseEntity<Weakness>(weakness, HttpStatus.OK);
		}else {
			return new ResponseEntity<Weakness>(weakness, HttpStatus.NO_CONTENT);
		}
	}
	
	// Read
	// Get by Name
	@CrossOrigin
	@GetMapping(path = "/weakness/weakness-by-name/{weaknessName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Weakness> getWeaknessByName(@PathVariable String weaknessName){

		Weakness weakness = weaknessService.getWeaknessByName(weaknessName);

		if (weakness != null) {
			return new ResponseEntity<Weakness>(weakness, HttpStatus.OK);
		}else {
			return new ResponseEntity<Weakness>(weakness, HttpStatus.NO_CONTENT);
		}
	}

	// Read
	// Get all
	@GetMapping(path = "/weakness/all-weakness", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Weakness>> getAllWeakness(){

		List<Weakness> weaknessList = weaknessService.getAllWeakness();

		if(weaknessList.size() > 0) {
			return new ResponseEntity<List<Weakness>>(weaknessList, HttpStatus.OK);
		}else {
			return new ResponseEntity<List<Weakness>>(weaknessList, HttpStatus.NO_CONTENT);
		}
	}
	
	// Update
	@PutMapping(path = "/weakness/update-weakness/{weaknessPK}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Weakness> updateWeakness(@PathVariable int weaknessPK, @RequestBody Weakness weakness){

		boolean updated = weaknessService.updateWeakness(weakness);
		Weakness weaknessUpdated = weaknessService.getWeaknessById(weaknessPK);
		if(updated) {
			return new ResponseEntity<Weakness>(weakness, HttpStatus.OK);
		}else {
			return new ResponseEntity<Weakness>(weakness, HttpStatus.NO_CONTENT);
		}

	}

	// Delete
	@DeleteMapping(path = "/weakness/delete-weakness/{weaknessPK}")
	public ResponseEntity<Boolean> deleteWeakness(@PathVariable int weaknessPK){

		boolean deleted = weaknessService.deleteWeakness(weaknessPK);

		if(deleted) {
			return new ResponseEntity<Boolean>(deleted, HttpStatus.OK);
		}else {
			return new ResponseEntity<Boolean>(deleted, HttpStatus.NO_CONTENT);
		}
	}
}
