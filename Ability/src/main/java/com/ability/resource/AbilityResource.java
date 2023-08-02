package com.ability.resource;

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

import com.ability.entity.Ability;
import com.ability.service.AbilityService;


@Controller
public class AbilityResource {
	
	@Autowired
	private AbilityService abilityService;
	
	// Create
	@PostMapping(path = "/ability/new-ability", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Ability> createAbility(@RequestBody Ability ability) {

		Ability newAbility = abilityService.createAbility(ability);

		if (newAbility != null) {
			return new ResponseEntity<>(newAbility, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}


	// Read
	// Get by ID
	@CrossOrigin
	@GetMapping(path = "/ability/{abilityPK}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Ability> getAbilityById(@PathVariable int abilityPK){

		Ability ability = abilityService.getAbilityById(abilityPK);

		if (ability != null) {
			return new ResponseEntity<Ability>(ability, HttpStatus.OK);
		}else {
			return new ResponseEntity<Ability>(ability, HttpStatus.NO_CONTENT);
		}
	}
	
	// Read
	// Get by Name
	@CrossOrigin
	@GetMapping(path = "/ability/ability-by-name/{abilityName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Ability> getAbilityByName(@PathVariable String abilityName){

		Ability ability = abilityService.getAbilityByName(abilityName);

		if (ability != null) {
			return new ResponseEntity<Ability>(ability, HttpStatus.OK);
		}else {
			return new ResponseEntity<Ability>(ability, HttpStatus.NO_CONTENT);
		}
	}

	// Read
	// Get all
	@GetMapping(path = "/ability/all-ability", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Ability>> getAllAbility(){

		List<Ability> abilityList = abilityService.getAllAbility();

		if(abilityList.size() > 0) {
			return new ResponseEntity<List<Ability>>(abilityList, HttpStatus.OK);
		}else {
			return new ResponseEntity<List<Ability>>(abilityList, HttpStatus.NO_CONTENT);
		}
	}
	
	// Update
	@PutMapping(path = "/ability/update-ability/{abilityPK}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Ability> updateAbility(@PathVariable int abilityPK, @RequestBody Ability ability){

		boolean updated = abilityService.updateAbility(ability);
		Ability abilityUpdated = abilityService.getAbilityById(abilityPK);
		if(updated) {
			return new ResponseEntity<Ability>(ability, HttpStatus.OK);
		}else {
			return new ResponseEntity<Ability>(ability, HttpStatus.NO_CONTENT);
		}

	}

	// Delete
	@DeleteMapping(path = "/ability/delete-ability/{abilityPK}")
	public ResponseEntity<Boolean> deleteAbility(@PathVariable int abilityPK){

		boolean deleted = abilityService.deleteAbility(abilityPK);

		if(deleted) {
			return new ResponseEntity<Boolean>(deleted, HttpStatus.OK);
		}else {
			return new ResponseEntity<Boolean>(deleted, HttpStatus.NO_CONTENT);
		}
	}
}
