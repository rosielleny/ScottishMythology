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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.myth.entity.*;
import com.myth.entity.composite.GenericEntity;
import com.myth.service.micro.*;

@Controller
public class AbilityController {
	
	@Autowired
	private AbilityService abilityService;
	
	// Model Attributes
	
	@ModelAttribute("abilities")
	public List<String> getAbilities() {
		
	    List<Ability> abilities = abilityService.getAllAbility();
	    return abilities.stream()
	            .map(ability -> ability.getAbilityName())
	            .collect(Collectors.toList());
	}
	
	
	// CREATE
	
	@CrossOrigin
	@RequestMapping("/ability/create")
	public ModelAndView createAnAbility() {
		ModelAndView modelAndView = new ModelAndView();
		
		GenericEntity entity = new GenericEntity(0, "", "");
		
		modelAndView.addObject("entity", entity);
		modelAndView.addObject("entities", getAbilities());
		modelAndView.addObject("label", "Ability Name:");
		modelAndView.addObject("entityType", "Ability");
		modelAndView.addObject("checkEntityName", "ability/check-ability-name");
		modelAndView.setViewName("entity/create-entity");
		modelAndView.addObject("entityPath", "/ability/save-new");
		return modelAndView;
		
	}

	
	@CrossOrigin
	@RequestMapping("/ability/save-new")
	public ModelAndView saveAbility(@Valid @ModelAttribute("entity")GenericEntity entity, BindingResult results) {
		
		
		ModelAndView modelAndView = new ModelAndView();
		if(results.hasErrors()) {
			return new ModelAndView("entity/create-entity", "entity", entity);
		}
		
		String message = null;
		
		if(entity != null) {
			Ability ability = new Ability();
			ability.setAbilityName(entity.getEntityName());
			abilityService.createAbility(ability);
			message = ability.getAbilityName() + " successfully added to the database";
		}
		else {
			message = "An error occurred. Ability not added to the database";
		}
		
		modelAndView.addObject("message", message);
		modelAndView.addObject("showAllEntityPath", "/ability/abilities");
		modelAndView.addObject("editEntityPath", "/ability/edit");
		modelAndView.addObject("editEntityPath", "/ability/delete");
		modelAndView.addObject("createEntityPath", "/ability/create");
		modelAndView.setViewName("entity/entity-output");
		
		return modelAndView;
	}
	
	
	// READ
	// Functions as the main page, showing all entities and offering a search bar for individual entities at the top
	@CrossOrigin
	@RequestMapping("/ability/abilities")
	public ModelAndView showAllAbilities() {
		System.out.println("Found me!");
		ModelAndView modelAndView = new ModelAndView();
		
		List<Ability> abilityList = abilityService.getAllAbility();
		List<GenericEntity> entityList = new ArrayList<GenericEntity>();
		
		for(Ability ability: abilityList) {
			GenericEntity entity = new GenericEntity(ability.getAbilityPK(), ability.getAbilityName());
			entityList.add(entity);
		}
		modelAndView.addObject("entityList", entityList);
		modelAndView.addObject("entitySearchPath", "/ability/search");
		
		modelAndView.addObject("showAllEntityPath", "/ability/abilities");
		modelAndView.addObject("editEntityPath", "/ability/edit");
		modelAndView.addObject("deleteEntityPath", "/ability/delete");
		modelAndView.addObject("createEntityPath", "/ability/create");
	
		modelAndView.setViewName("entity/show-all-entities");
	
		return modelAndView;
	}
	
	@CrossOrigin
	@RequestMapping("/ability/search")
	public ModelAndView searchAbilitiesByName(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		Ability ability = abilityService.getAbilityByName(request.getParameter("name"));
		
		if(ability!=null) {
			GenericEntity entity = new GenericEntity(ability.getAbilityPK(), ability.getAbilityName());
			modelAndView.addObject("entity", entity);
			modelAndView.addObject("entitySearchPath", "/ability/search");
			modelAndView.addObject("showAllEntityPath", "/ability/abilities");
			modelAndView.addObject("editEntityPath", "/ability/edit");
			modelAndView.addObject("deleteEntityPath", "/ability/delete");
			modelAndView.addObject("createEntityPath", "/ability/create");
			modelAndView.setViewName("entity/show-entity");
		}
		else {
			modelAndView.addObject("message", "No abilities were found for " + request.getParameter("name"));
			modelAndView.addObject("entitySearchPath", "/ability/search");
			modelAndView.setViewName("entity/show-entity");
		}
		
		return modelAndView;
	}
	
	// UPDATE
	
//	@CrossOrigin
//	@RequestMapping("ability/edit")
//	public ModelAndView editAbilities(@RequestParam int pk) {
//		
//		ModelAndView modelAndView = new ModelAndView();
//		Ability ability = abilityService.getAbilityById(pk);
//		
//		modelAndView.addObject("pk-fill", ability.getAbilityPK());
//		modelAndView.addObject("name-fill", ability.getAbilityName());
//		modelAndView.setViewName("edit-entity");
//		
//		
//		return modelAndView;
//	}
	
	// DELETE
	
	@CrossOrigin
	@RequestMapping("/ability/delete")
	public void deleteAbilities(@RequestParam int pk) {
		
		ModelAndView modelAndView = new ModelAndView();
		
		Ability ability = abilityService.getAbilityById(pk);
		
		if(abilityService.deleteAbility(pk)) {
			modelAndView.addObject("message", ability.getAbilityName() + " successfully deleted from Abilities.");
		}else {
			modelAndView.addObject("message", "Failed to delete " + ability.getAbilityName() + " from Abilities.");
		}
	}
	
}
