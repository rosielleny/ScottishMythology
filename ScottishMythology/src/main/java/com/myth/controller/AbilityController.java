package com.myth.controller;

import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.myth.entity.*;
import com.myth.entity.composite.GenericEntity;
import com.myth.service.ScottishMythologyService;
import com.myth.service.micro.*;

@Controller
public class AbilityController {
	
	@Autowired
	private AbilityService abilityService;
	@Autowired
	private ScottishMythologyService scottishMythologyService;
	
	// Model Attributes
	
	@ModelAttribute("abilities")
	public List<String> getAbilities() {
		// Getting all abilities
	    List<Ability> abilities = abilityService.getAllAbility(); 
	    // Transforming the list of abilities into a list of ability names
	    return abilities.stream()
	            .map(ability -> ability.getAbilityName())
	            .collect(Collectors.toList());
	}
	
	// A function to convert a list of Abilities into a list of GenericEntities
	public List<GenericEntity> convertToGenericEntityList(List<Ability> abilityList){
		
		List<GenericEntity> entityList = new ArrayList<GenericEntity>();
		// For each ability in the list, pass the values into the GenericEntity constructor and add to the entity list
		for(Ability ability: abilityList) {
			GenericEntity entity = new GenericEntity(ability.getAbilityPK(), ability.getAbilityName());
			entityList.add(entity);
		}
		
		return entityList;
	}
	
	
	// CREATE
	
	@CrossOrigin
	@RequestMapping("/ability/create")
	public ModelAndView createAnAbility() {
		
		ModelAndView modelAndView = new ModelAndView();
		// Creating GenericEntity object
		GenericEntity entity = new GenericEntity(0, "");
		// Adding it to the mav
		modelAndView.addObject("entity", entity);
		// Getting the list of ability names and adding to to the mav as "entities"
		modelAndView.addObject("entities", getAbilities());
		// Calling a function to set up all possible links required for the template
		modelAndView = scottishMythologyService.setUpLinks("ability", "abilities", modelAndView);
		
		modelAndView.setViewName("entity/create-entity");
		
		return modelAndView;
		
	}

	
	@CrossOrigin
	@RequestMapping("/ability/save-new")
	public ModelAndView saveAbility(@Valid @ModelAttribute("entity")GenericEntity entity, BindingResult results) {
		
		
		ModelAndView modelAndView = new ModelAndView();
		// If the input has errors it wont be submitted
		if(results.hasErrors()) {
			return new ModelAndView("entity/create-entity", "entity", entity);
		}
		
		String message = null;
		
		if(entity != null) {
			// If we have Entity we convert it back into Ability
			Ability ability = new Ability();
			ability.setAbilityName(entity.getEntityName());
			// Then call the service to create it
			abilityService.createAbility(ability);
			message = ability.getAbilityName() + " successfully added to the database.";
		}
		else {
			message = "An error occurred. Ability not added to the database.";
		}
		
		// Adding the message
		modelAndView.addObject("message", message);
		// Adding all possible necessary links
		modelAndView = scottishMythologyService.setUpLinks("ability", "abilities", modelAndView);
		
		modelAndView.setViewName("entity/entity-output");
		
		return modelAndView;
	}
	
	
	// READ
	// Functions as the main page, showing all entities and offering a search bar for individual entities at the top
	@CrossOrigin
	@RequestMapping("/ability/abilities")
	public ModelAndView showAllAbilities() {
		
		ModelAndView modelAndView = new ModelAndView();
		
		// Getting all abilities and converting the list to a list of GenericEntities
		List<GenericEntity> entityList = convertToGenericEntityList(abilityService.getAllAbility());
		
		modelAndView.addObject("entityList", entityList);
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("ability", "abilities", modelAndView);
	
		modelAndView.setViewName("entity/show-all-entities");
	
		return modelAndView;
	}
	
	@CrossOrigin
	@RequestMapping("/ability/search")
	public ModelAndView searchAbilitiesByName(HttpServletRequest request) {
		
		ModelAndView modelAndView = new ModelAndView();
		// Getting ability by name
		Ability ability = abilityService.getAbilityByName(request.getParameter("name"));
		// Setting up all possible links
		modelAndView = scottishMythologyService.setUpLinks("ability", "abilities", modelAndView);
		
		if(ability!=null) {
			// If ability was found, we convert to GenericEntity
			GenericEntity entity = new GenericEntity(ability.getAbilityPK(), ability.getAbilityName());
			modelAndView.addObject("entity", entity);
			modelAndView.setViewName("entity/show-entity");
		}
		else {
			
			modelAndView.addObject("message", "No abilities were found for " + request.getParameter("name"));
			modelAndView.setViewName("entity/show-entity");
		}
		
		return modelAndView;
	}
	
	// UPDATE
	
	@CrossOrigin
	@RequestMapping("/ability/edit")
	public ModelAndView editAbilities(@RequestParam int pk) {
		
		ModelAndView modelAndView = new ModelAndView();
		// Getting ability by Id
		Ability ability = abilityService.getAbilityById(pk);
		// Converting ability to GenericEntity
		GenericEntity ogEntity = new GenericEntity(ability.getAbilityPK(), ability.getAbilityName());
		// Creating a second store for the same entity
		GenericEntity entity = new GenericEntity(ogEntity.getEntityPK(), ogEntity.getEntityName());
		
		// Adding the list of ability names
		modelAndView.addObject("entities", getAbilities());
		
		modelAndView.addObject("entity", entity);
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("ability", "abilities", modelAndView);
		modelAndView.addObject("ogEntity", ogEntity);
		modelAndView.setViewName("entity/edit-entity");
		
		
		return modelAndView;
	}
	
	@CrossOrigin
	@RequestMapping("/ability/save-edit")
	public ModelAndView saveEdittedAbility(@Valid @ModelAttribute("entity")GenericEntity entity, BindingResult results){
		
		
		ModelAndView modelAndView = new ModelAndView();
		
		if(results.hasErrors()) {
			return new ModelAndView("entity/edit-entity", "entity", entity);
		}
		
		String message = null;
		
		if(entity != null) {
			// Getting the original ability by PK
			Ability ability = abilityService.getAbilityById(entity.getEntityPK());
			// Setting the name to the new name
			ability.setAbilityName(entity.getEntityName());
			
			if(abilityService.updateAbility(ability)) {
				
				message = ability.getAbilityName() + " successfully updated";
				
			}else {
				message = "An error occurred. Ability not updated. Backend";
			}
		}
		else {
			message = "An error occurred. Ability not updated. Frontend";
		}
		
		// Getting the new ability by ID
		Ability newAbility = abilityService.getAbilityById(entity.getEntityPK());
		// Converting to GenericEntity
		GenericEntity newEntity = new GenericEntity(newAbility.getAbilityPK(), newAbility.getAbilityName());
		modelAndView.addObject("entity", newEntity);
		modelAndView.addObject("message", message);
		// Adding all possible links to the mav
		modelAndView = scottishMythologyService.setUpLinks("ability", "abilities", modelAndView);
		
		modelAndView.setViewName("entity/entity-output");
		
		return modelAndView;
	}
	
	// DELETE
	
	@CrossOrigin
	@RequestMapping("/ability/delete/{pk}")
	public ModelAndView deleteAbilities(@PathVariable int pk) {
		
		ModelAndView modelAndView = new ModelAndView();
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("ability", "abilities", modelAndView);
		// Getting the ability by PK
		Ability ability = abilityService.getAbilityById(pk);
		// Converting to GenericEntity
		GenericEntity entity = new GenericEntity(ability.getAbilityPK(), ability.getAbilityName());
		
		if(abilityService.deleteAbility(pk)) {
			modelAndView.addObject("message", ability.getAbilityName() + " successfully deleted from Abilities.");
		}else {
			modelAndView.addObject("message", "Failed to delete " + ability.getAbilityName() + " from Abilities.");
		}
		
		// Getting list of abilities as GenericEntity list
		List<GenericEntity> entityList = convertToGenericEntityList(abilityService.getAllAbility());
		
		modelAndView.addObject("entity", entity);
		modelAndView.addObject("entityList", entityList);
		modelAndView.setViewName("entity/show-all-entities");
		
		return modelAndView;
	}
	
}
