package com.myth.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.myth.entity.Faction;
import com.myth.entity.Faction;
import com.myth.entity.composite.GenericEntity;
import com.myth.service.ScottishMythologyService;
import com.myth.service.micro.FactionService;

@Controller
public class FactionController {

	@Autowired
	private FactionService factionService;
	@Autowired
	private ScottishMythologyService scottishMythologyService;
	
	// Model Attributes
	
	@ModelAttribute("factions")
	public List<String> getFactions() {
		// Getting all factions
	    List<Faction> factions = factionService.getAllFaction();
	    // Transforming the list of factions into a list of faction names
	    return factions.stream()
	            .map(faction -> faction.getFactionName())
	            .collect(Collectors.toList());
	}
	
	// A function to convert a list of Factions into a list of GenericEntities
	public List<GenericEntity> convertToGenericEntityList(List<Faction> factionList){
		
		List<GenericEntity> entityList = new ArrayList<GenericEntity>();
		// For each faction in the list, pass the values into the GenericEntity constructor and add to the entity list
		for(Faction faction: factionList) {
			GenericEntity entity = new GenericEntity(faction.getFactionPK(), faction.getFactionName(), faction.getFactionDescription());
			entityList.add(entity);
		}
		
		return entityList;
	}
	
	
	// CREATE
	
	@CrossOrigin
	@RequestMapping("/faction/create")
	public ModelAndView createAnFaction() {
		ModelAndView modelAndView = new ModelAndView();
		// Creating GenericEntity object
		GenericEntity entity = new GenericEntity(0, "", " ");
		// Adding it to the mav
		modelAndView.addObject("entity", entity);
		modelAndView.addObject("entities", getFactions());
		// Getting the list of faction names and adding to to the mav as "entities"
		modelAndView = scottishMythologyService.setUpLinks("faction", "factions", modelAndView);
		// Calling a function to set up all possible links required for the template
		modelAndView.setViewName("entity/create-entity");
		
		return modelAndView;
		
	}

	
	@CrossOrigin
	@RequestMapping("/faction/save-new")
	public ModelAndView saveFaction(@Valid @ModelAttribute("entity")GenericEntity entity, BindingResult results) {
		
		
		ModelAndView modelAndView = new ModelAndView();
		// If the input has errors it wont be submitted
		if(results.hasErrors()) {
			return new ModelAndView("entity/create-entity", "entity", entity);
		}
		
		String message = null;
		
		if(entity != null) {
			// If we have Entity we convert it back into Faction
			Faction faction = new Faction();
			faction.setFactionName(entity.getEntityName());
			faction.setFactionDescription(entity.getEntityDescription());
			// Then call the service to create it
			factionService.createFaction(faction);
			message = faction.getFactionName() + " successfully added to the database.";
		}
		else {
			message = "An error occurred. Faction not added to the database.";
		}
		// Adding the message
		modelAndView.addObject("message", message);
		// Adding all possible necessary links
		modelAndView = scottishMythologyService.setUpLinks("faction", "factions", modelAndView);
		
		modelAndView.setViewName("entity/entity-output");
		
		return modelAndView;
	}
	
	
	// READ
	// Functions as the main page, showing all entities and offering a search bar for individual entities at the top
	@CrossOrigin
	@RequestMapping("/faction/factions")
	public ModelAndView showAllFactions() {
		
		ModelAndView modelAndView = new ModelAndView();
		
		// Getting all factions and converting the list to a list of GenericEntities
		List<GenericEntity> entityList = convertToGenericEntityList(factionService.getAllFaction());
		
		modelAndView.addObject("entityList", entityList);
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("faction", "factions", modelAndView);
	
		modelAndView.setViewName("entity/show-all-entities");
	
		return modelAndView;
	}
	
	@CrossOrigin
	@RequestMapping("/faction/search")
	public ModelAndView searchFactionsByName(HttpServletRequest request) {
		
		ModelAndView modelAndView = new ModelAndView();
		// Getting faction by name
		Faction faction = factionService.getFactionByName(request.getParameter("name"));
		// Setting up all possible links
		modelAndView = scottishMythologyService.setUpLinks("faction", "factions", modelAndView);
		
		if(faction!=null) {
			// If faction was found, we convert to GenericEntity
			GenericEntity entity = new GenericEntity(faction.getFactionPK(), faction.getFactionName(), faction.getFactionDescription());
			modelAndView.addObject("entity", entity);
			modelAndView.setViewName("entity/show-entity");
		}
		else {
			
			modelAndView.addObject("message", "No factions were found for " + request.getParameter("name"));
			modelAndView.setViewName("entity/show-entity");
		}
		
		return modelAndView;
	}
	
	// UPDATE
	
	@CrossOrigin
	@RequestMapping("/faction/edit")
	public ModelAndView editFactions(@RequestParam int pk) {
		
		ModelAndView modelAndView = new ModelAndView();
		// Getting faction by Id
		Faction faction = factionService.getFactionById(pk);
		// Converting faction to GenericEntity
		GenericEntity ogEntity = new GenericEntity(faction.getFactionPK(), faction.getFactionName(), faction.getFactionDescription());
		// Creating a second store for the same entity
		GenericEntity entity = new GenericEntity(ogEntity.getEntityPK(), ogEntity.getEntityName(), ogEntity.getEntityDescription());
		
		// Adding the list of faction names
		modelAndView.addObject("entities", getFactions());
		// Adding all possible links
		modelAndView.addObject("entity", entity);
		modelAndView = scottishMythologyService.setUpLinks("faction", "factions", modelAndView);
		modelAndView.addObject("ogEntity", ogEntity);
		modelAndView.setViewName("entity/edit-entity");
		
		
		return modelAndView;
	}
	
	@CrossOrigin
	@RequestMapping("/faction/save-edit")
	public ModelAndView saveEdittedFaction(@Valid @ModelAttribute("entity")GenericEntity entity, BindingResult results){
		
		
		ModelAndView modelAndView = new ModelAndView();
		
		if(results.hasErrors()) {
			return new ModelAndView("entity/edit-entity", "entity", entity);
		}
		
		String message = null;
		
		if(entity != null) {
			// Getting the original faction by PK
			Faction faction = factionService.getFactionById(entity.getEntityPK());
			// Setting the name to the new name
			faction.setFactionName(entity.getEntityName());
			// Setting the description to the new description
			faction.setFactionDescription(entity.getEntityDescription());
			if(factionService.updateFaction(faction)) {
				
				message = faction.getFactionName() + " successfully updated";
			}else {
				message = "An error occurred. Faction not updated. Backend";
			}
		}
		else {
			message = "An error occurred. Faction not updated. Frontend";
		}
		// Getting the new faction by ID
		Faction newFaction = factionService.getFactionById(entity.getEntityPK());
		// Converting to GenericEntity
		GenericEntity newEntity = new GenericEntity(newFaction.getFactionPK(), newFaction.getFactionName(), newFaction.getFactionDescription());
		modelAndView.addObject("entity", newEntity);
		modelAndView.addObject("message", message);
		// Adding all possible links to the mav
		modelAndView = scottishMythologyService.setUpLinks("faction", "factions", modelAndView);
		
		modelAndView.setViewName("entity/entity-output");
		
		return modelAndView;
	}
	
	// DELETE
	
	@CrossOrigin
	@RequestMapping("/faction/delete/{pk}")
	public ModelAndView deleteFactions(@PathVariable int pk) {
		
		ModelAndView modelAndView = new ModelAndView();
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("faction", "factions", modelAndView);
		// Getting the faction by PK
		Faction faction = factionService.getFactionById(pk);
		// Converting to GenericEntity
		GenericEntity entity = new GenericEntity(faction.getFactionPK(), faction.getFactionName(), faction.getFactionDescription());
		
		if(factionService.deleteFaction(pk)) {
			modelAndView.addObject("message", faction.getFactionName() + " successfully deleted from Factions.");
		}else {
			modelAndView.addObject("message", "Failed to delete " + faction.getFactionName() + " from Factions.");
		}
		// Getting list of factions as GenericEntity list
		List<GenericEntity> entityList = convertToGenericEntityList(factionService.getAllFaction());
		
		modelAndView.addObject("entity", entity);
		modelAndView.addObject("entityList", entityList);
		modelAndView.setViewName("entity/show-all-entities");
		
		return modelAndView;
	}
	
	@PostMapping("faction/add")
	public ResponseEntity<Map<String, Boolean>> sneakyAddFaction(@RequestBody Faction faction) {
		Map<String, Boolean> response = new HashMap<>();
		try {
	        factionService.createFaction(faction);
	        response.put("success", true);
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        response.put("success", false);
	        return ResponseEntity.badRequest().body(response);
	    }
	}
}
