package com.faction.resource;

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

import com.faction.entity.Faction;
import com.faction.service.FactionService;


@Controller
public class FactionResource {
	
	@Autowired
	private FactionService factionService;
	
	// Create
	@PostMapping(path = "/faction/new-faction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Faction> createFaction(@RequestBody Faction faction) {

		Faction newFaction = factionService.createFaction(faction);

		if (newFaction != null) {
			return new ResponseEntity<>(newFaction, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}


	// Read
	// Get by ID
	@CrossOrigin
	@GetMapping(path = "/faction/{factionPK}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Faction> getFactionById(@PathVariable int factionPK){

		Faction faction = factionService.getFactionById(factionPK);

		if (faction != null) {
			return new ResponseEntity<Faction>(faction, HttpStatus.OK);
		}else {
			return new ResponseEntity<Faction>(faction, HttpStatus.NO_CONTENT);
		}
	}
	
	// Read
	// Get by Name
	@CrossOrigin
	@GetMapping(path = "/faction/faction-by-name/{factionName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Faction> getFactionByName(@PathVariable String factionName){

		Faction faction = factionService.getFactionByName(factionName);

		if (faction != null) {
			return new ResponseEntity<Faction>(faction, HttpStatus.OK);
		}else {
			return new ResponseEntity<Faction>(faction, HttpStatus.NO_CONTENT);
		}
	}

	// Read
	// Get all
	@GetMapping(path = "/faction/all-faction", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Faction>> getAllFaction(){

		List<Faction> factionList = factionService.getAllFaction();

		if(factionList.size() > 0) {
			return new ResponseEntity<List<Faction>>(factionList, HttpStatus.OK);
		}else {
			return new ResponseEntity<List<Faction>>(factionList, HttpStatus.NO_CONTENT);
		}
	}
	
	// Update
	@PutMapping(path = "/faction/update-faction/{factionPK}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Faction> updateFaction(@PathVariable int factionPK, @RequestBody Faction faction){

		boolean updated = factionService.updateFaction(faction);
		Faction factionUpdated = factionService.getFactionById(factionPK);
		if(updated) {
			return new ResponseEntity<Faction>(faction, HttpStatus.OK);
		}else {
			return new ResponseEntity<Faction>(faction, HttpStatus.NO_CONTENT);
		}

	}

	// Delete
	@DeleteMapping(path = "/faction/delete-faction/{factionPK}")
	public ResponseEntity<Boolean> deleteFaction(@PathVariable int factionPK){

		boolean deleted = factionService.deleteFaction(factionPK);

		if(deleted) {
			return new ResponseEntity<Boolean>(deleted, HttpStatus.OK);
		}else {
			return new ResponseEntity<Boolean>(deleted, HttpStatus.NO_CONTENT);
		}
	}
}
