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

import com.myth.entity.Species;
import com.myth.entity.Species;
import com.myth.entity.composite.GenericEntity;
import com.myth.service.ScottishMythologyService;
import com.myth.service.micro.SpeciesService;

@Controller
public class SpeciesController {

	@Autowired
	private SpeciesService speciesService;
	@Autowired
	private ScottishMythologyService scottishMythologyService;
	
	// Model Attributes
	
	@ModelAttribute("species")
	public List<String> getSpecies() {
		// Getting all species
	    List<Species> speciesList = speciesService.getAllSpecies();
	    // Transforming the list of species into a list of species names
	    return speciesList.stream()
	            .map(species -> species.getSpeciesName())
	            .collect(Collectors.toList());
	}
	
	// A function to convert a list of Species into a list of GenericEntities
	public List<GenericEntity> convertToGenericEntityList(List<Species> speciesList){
		
		List<GenericEntity> entityList = new ArrayList<GenericEntity>();
		// For each species in the list, pass the values into the GenericEntity constructor and add to the entity list
		for(Species species: speciesList) {
			GenericEntity entity = new GenericEntity(species.getSpeciesPK(), species.getSpeciesName(), species.getSpeciesDescription());
			entityList.add(entity);
		}
		
		return entityList;
	}
	
	
	// CREATE
	
	@CrossOrigin
	@RequestMapping("/species/create")
	public ModelAndView createAnSpecies() {
		ModelAndView modelAndView = new ModelAndView();
		// Creating GenericEntity object
		GenericEntity entity = new GenericEntity(0, "", " ");
		// Adding it to the mav
		modelAndView.addObject("entity", entity);
		modelAndView.addObject("entities", getSpecies());
		// Getting the list of species names and adding to to the mav as "entities"
		modelAndView = scottishMythologyService.setUpLinks("species", "species", modelAndView);
		// Calling a function to set up all possible links required for the template
		modelAndView.setViewName("entity/create-entity");
		
		return modelAndView;
		
	}

	
	@CrossOrigin
	@RequestMapping("/species/save-new")
	public ModelAndView saveSpecies(@Valid @ModelAttribute("entity")GenericEntity entity, BindingResult results) {
		
		
		ModelAndView modelAndView = new ModelAndView();
		// If the input has errors it wont be submitted
		if(results.hasErrors()) {
			return new ModelAndView("entity/create-entity", "entity", entity);
		}
		
		String message = null;
		
		if(entity != null) {
			// If we have Entity we convert it back into Species
			Species species = new Species();
			species.setSpeciesName(entity.getEntityName());
			species.setSpeciesDescription(entity.getEntityDescription());
			// Then call the service to create it
			speciesService.createSpecies(species);
			message = species.getSpeciesName() + " successfully added to the database.";
		}
		else {
			message = "An error occurred. Species not added to the database.";
		}
		// Adding the message
		modelAndView.addObject("message", message);
		// Adding all possible necessary links
		modelAndView = scottishMythologyService.setUpLinks("species", "species", modelAndView);
		
		modelAndView.setViewName("entity/entity-output");
		
		return modelAndView;
	}
	
	
	// READ
	// Functions as the main page, showing all entities and offering a search bar for individual entities at the top
	@CrossOrigin
	@RequestMapping("/species/species")
	public ModelAndView showAllSpecies() {
		
		ModelAndView modelAndView = new ModelAndView();
		
		// Getting all species and converting the list to a list of GenericEntities
		List<GenericEntity> entityList = convertToGenericEntityList(speciesService.getAllSpecies());
		
		modelAndView.addObject("entityList", entityList);
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("species", "species", modelAndView);
	
		modelAndView.setViewName("entity/show-all-entities");
	
		return modelAndView;
	}
	
	@CrossOrigin
	@RequestMapping("/species/search")
	public ModelAndView searchSpeciesByName(HttpServletRequest request) {
		
		ModelAndView modelAndView = new ModelAndView();
		// Getting species by name
		Species species = speciesService.getSpeciesByName(request.getParameter("name"));
		// Setting up all possible links
		modelAndView = scottishMythologyService.setUpLinks("species", "species", modelAndView);
		
		if(species!=null) {
			// If species was found, we convert to GenericEntity
			GenericEntity entity = new GenericEntity(species.getSpeciesPK(), species.getSpeciesName(), species.getSpeciesDescription());
			modelAndView.addObject("entity", entity);
			modelAndView.setViewName("entity/show-entity");
		}
		else {
			
			modelAndView.addObject("message", "No species were found for " + request.getParameter("name"));
			modelAndView.setViewName("entity/show-entity");
		}
		
		return modelAndView;
	}
	
	// UPDATE
	
	@CrossOrigin
	@RequestMapping("/species/edit")
	public ModelAndView editSpecies(@RequestParam int pk) {
		
		ModelAndView modelAndView = new ModelAndView();
		// Getting species by Id
		Species species = speciesService.getSpeciesById(pk);
		// Converting species to GenericEntity
		GenericEntity ogEntity = new GenericEntity(species.getSpeciesPK(), species.getSpeciesName(), species.getSpeciesDescription());
		// Creating a second store for the same entity
		GenericEntity entity = new GenericEntity(ogEntity.getEntityPK(), ogEntity.getEntityName(), ogEntity.getEntityDescription());
		
		// Adding the list of species names
		modelAndView.addObject("entities", getSpecies());
		// Adding all possible links
		modelAndView.addObject("entity", entity);
		modelAndView = scottishMythologyService.setUpLinks("species", "species", modelAndView);
		modelAndView.addObject("ogEntity", ogEntity);
		modelAndView.setViewName("entity/edit-entity");
		
		
		return modelAndView;
	}
	
	@CrossOrigin
	@RequestMapping("/species/save-edit")
	public ModelAndView saveEdittedSpecies(@Valid @ModelAttribute("entity")GenericEntity entity, BindingResult results){
		
		
		ModelAndView modelAndView = new ModelAndView();
		
		if(results.hasErrors()) {
			return new ModelAndView("entity/edit-entity", "entity", entity);
		}
		
		String message = null;
		
		if(entity != null) {
			// Getting the original species by PK
			Species species = speciesService.getSpeciesById(entity.getEntityPK());
			// Setting the name to the new name
			species.setSpeciesName(entity.getEntityName());
			// Setting the desc to the new desc
			species.setSpeciesDescription(entity.getEntityDescription());
			if(speciesService.updateSpecies(species)) {
				
				message = species.getSpeciesName() + " successfully updated";
			}else {
				message = "An error occurred. Species not updated. Backend";
			}
		}
		else {
			message = "An error occurred. Species not updated. Frontend";
		}
		// Getting the new species by ID
		Species newSpecies = speciesService.getSpeciesById(entity.getEntityPK());
		// Converting to GenericEntity
		GenericEntity newEntity = new GenericEntity(newSpecies.getSpeciesPK(), newSpecies.getSpeciesName(), newSpecies.getSpeciesDescription());
		modelAndView.addObject("entity", newEntity);
		modelAndView.addObject("message", message);
		// Adding all possible links to the mav
		modelAndView = scottishMythologyService.setUpLinks("species", "species", modelAndView);
		
		modelAndView.setViewName("entity/entity-output");
		
		return modelAndView;
	}
	
	// DELETE
	
	@CrossOrigin
	@RequestMapping("/species/delete/{pk}")
	public ModelAndView deleteSpecies(@PathVariable int pk) {
		
		ModelAndView modelAndView = new ModelAndView();
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("species", "species", modelAndView);
		// Getting the species by PK
		Species species = speciesService.getSpeciesById(pk);
		// Converting to GenericEntity
		GenericEntity entity = new GenericEntity(species.getSpeciesPK(), species.getSpeciesName(), species.getSpeciesDescription());
		
		if(speciesService.deleteSpecies(pk)) {
			modelAndView.addObject("message", species.getSpeciesName() + " successfully deleted from Species.");
		}else {
			modelAndView.addObject("message", "Failed to delete " + species.getSpeciesName() + " from Species.");
		}
		// Getting list of species as GenericEntity list
		List<GenericEntity> entityList = convertToGenericEntityList(speciesService.getAllSpecies());
		
		modelAndView.addObject("entity", entity);
		modelAndView.addObject("entityList", entityList);
		modelAndView.setViewName("entity/show-all-entities");
		
		return modelAndView;
	}
	
	@PostMapping("species/add")
	public ResponseEntity<Map<String, Boolean>> sneakyAddSpecies(@RequestBody Species species) {
		Map<String, Boolean> response = new HashMap<>();
		try {
	        speciesService.createSpecies(species);
	        response.put("success", true);
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        response.put("success", false);
	        return ResponseEntity.badRequest().body(response);
	    }
	}
}
