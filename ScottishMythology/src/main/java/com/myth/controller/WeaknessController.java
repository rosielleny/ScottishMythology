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

import com.myth.entity.Weakness;
import com.myth.entity.Weakness;
import com.myth.entity.composite.GenericEntity;
import com.myth.service.ScottishMythologyService;
import com.myth.service.micro.WeaknessService;

@Controller
public class WeaknessController {

	@Autowired
	private WeaknessService weaknessService;
	@Autowired
	private ScottishMythologyService scottishMythologyService;
	
	// Model Attributes
	
	@ModelAttribute("weaknesses")
	public List<String> getWeaknesses() {
		// Getting all weaknesses
	    List<Weakness> weaknesses = weaknessService.getAllWeakness(); 
	    // Transforming the list of weaknesses into a list of weakness names
	    return weaknesses.stream()
	            .map(weakness -> weakness.getWeaknessName())
	            .collect(Collectors.toList());
	}
	
	// A function to convert a list of Weaknesses into a list of GenericEntities
	public List<GenericEntity> convertToGenericEntityList(List<Weakness> weaknessList){
		
		List<GenericEntity> entityList = new ArrayList<GenericEntity>();
		// For each weakness in the list, pass the values into the GenericEntity constructor and add to the entity list
		for(Weakness weakness: weaknessList) {
			GenericEntity entity = new GenericEntity(weakness.getWeaknessPK(), weakness.getWeaknessName());
			entityList.add(entity);
		}
		
		return entityList;
	}
	
	
	// CREATE
	
	@CrossOrigin
	@RequestMapping("/weakness/create")
	public ModelAndView createAnWeakness() {
		
		ModelAndView modelAndView = new ModelAndView();
		// Creating GenericEntity object
		GenericEntity entity = new GenericEntity(0, "");
		// Adding it to the mav
		modelAndView.addObject("entity", entity);
		// Getting the list of weakness names and adding to to the mav as "entities"
		modelAndView.addObject("entities", getWeaknesses());
		// Calling a function to set up all possible links required for the template
		modelAndView = scottishMythologyService.setUpLinks("weakness", "weaknesses", modelAndView);
		
		modelAndView.setViewName("entity/create-entity");
		
		return modelAndView;
		
	}

	
	@CrossOrigin
	@RequestMapping("/weakness/save-new")
	public ModelAndView saveWeakness(@Valid @ModelAttribute("entity")GenericEntity entity, BindingResult results) {
		
		
		ModelAndView modelAndView = new ModelAndView();
		// If the input has errors it wont be submitted
		if(results.hasErrors()) {
			return new ModelAndView("entity/create-entity", "entity", entity);
		}
		
		String message = null;
		
		if(entity != null) {
			// If we have Entity we convert it back into Weakness
			Weakness weakness = new Weakness();
			weakness.setWeaknessName(entity.getEntityName());
			// Then call the service to create it
			weaknessService.createWeakness(weakness);
			message = weakness.getWeaknessName() + " successfully added to the database.";
		}
		else {
			message = "An error occurred. Weakness not added to the database.";
		}
		
		// Adding the message
		modelAndView.addObject("message", message);
		// Adding all possible necessary links
		modelAndView = scottishMythologyService.setUpLinks("weakness", "weaknesses", modelAndView);
		
		modelAndView.setViewName("entity/entity-output");
		
		return modelAndView;
	}
	
	
	// READ
	// Functions as the main page, showing all entities and offering a search bar for individual entities at the top
	@CrossOrigin
	@RequestMapping("/weakness/weaknesses")
	public ModelAndView showAllWeaknesses() {
		
		ModelAndView modelAndView = new ModelAndView();
		
		// Getting all weaknesses and converting the list to a list of GenericEntities
		List<GenericEntity> entityList = convertToGenericEntityList(weaknessService.getAllWeakness());
		
		modelAndView.addObject("entityList", entityList);
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("weakness", "weaknesses", modelAndView);
	
		modelAndView.setViewName("entity/show-all-entities");
	
		return modelAndView;
	}
	
	@CrossOrigin
	@RequestMapping("/weakness/search")
	public ModelAndView searchWeaknessesByName(HttpServletRequest request) {
		
		ModelAndView modelAndView = new ModelAndView();
		// Getting weakness by name
		Weakness weakness = weaknessService.getWeaknessByName(request.getParameter("name"));
		// Setting up all possible links
		modelAndView = scottishMythologyService.setUpLinks("weakness", "weaknesses", modelAndView);
		
		if(weakness!=null) {
			// If weakness was found, we convert to GenericEntity
			GenericEntity entity = new GenericEntity(weakness.getWeaknessPK(), weakness.getWeaknessName());
			modelAndView.addObject("entity", entity);
			modelAndView.setViewName("entity/show-entity");
		}
		else {
			
			modelAndView.addObject("message", "No weaknesses were found for " + request.getParameter("name"));
			modelAndView.setViewName("entity/show-entity");
		}
		
		return modelAndView;
	}
	
	// UPDATE
	
	@CrossOrigin
	@RequestMapping("/weakness/edit")
	public ModelAndView editWeaknesses(@RequestParam int pk) {
		
		ModelAndView modelAndView = new ModelAndView();
		// Getting weakness by Id
		Weakness weakness = weaknessService.getWeaknessById(pk);
		// Converting weakness to GenericEntity
		GenericEntity ogEntity = new GenericEntity(weakness.getWeaknessPK(), weakness.getWeaknessName());
		// Creating a second store for the same entity
		GenericEntity entity = new GenericEntity(ogEntity.getEntityPK(), ogEntity.getEntityName());
		
		// Adding the list of weakness names
		modelAndView.addObject("entities", getWeaknesses());
		
		modelAndView.addObject("entity", entity);
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("weakness", "weaknesses", modelAndView);
		modelAndView.addObject("ogEntity", ogEntity);
		modelAndView.setViewName("entity/edit-entity");
		
		
		return modelAndView;
	}
	
	@CrossOrigin
	@RequestMapping("/weakness/save-edit")
	public ModelAndView saveEdittedWeakness(@Valid @ModelAttribute("entity")GenericEntity entity, BindingResult results){
		
		
		ModelAndView modelAndView = new ModelAndView();
		
		if(results.hasErrors()) {
			return new ModelAndView("entity/edit-entity", "entity", entity);
		}
		
		String message = null;
		
		if(entity != null) {
			// Getting the original weakness by PK
			Weakness weakness = weaknessService.getWeaknessById(entity.getEntityPK());
			// Setting the name to the new name
			weakness.setWeaknessName(entity.getEntityName());
			
			if(weaknessService.updateWeakness(weakness)) {
				
				message = weakness.getWeaknessName() + " successfully updated";
				
			}else {
				message = "An error occurred. Weakness not updated. Backend";
			}
		}
		else {
			message = "An error occurred. Weakness not updated. Frontend";
		}
		
		// Getting the new weakness by ID
		Weakness newWeakness = weaknessService.getWeaknessById(entity.getEntityPK());
		// Converting to GenericEntity
		GenericEntity newEntity = new GenericEntity(newWeakness.getWeaknessPK(), newWeakness.getWeaknessName());
		modelAndView.addObject("entity", newEntity);
		modelAndView.addObject("message", message);
		// Adding all possible links to the mav
		modelAndView = scottishMythologyService.setUpLinks("weakness", "weaknesses", modelAndView);
		
		modelAndView.setViewName("entity/entity-output");
		
		return modelAndView;
	}
	
	// DELETE
	
	@CrossOrigin
	@RequestMapping("/weakness/delete/{pk}")
	public ModelAndView deleteWeaknesses(@PathVariable int pk) {
		
		ModelAndView modelAndView = new ModelAndView();
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("weakness", "weaknesses", modelAndView);
		// Getting the weakness by PK
		Weakness weakness = weaknessService.getWeaknessById(pk);
		// Converting to GenericEntity
		GenericEntity entity = new GenericEntity(weakness.getWeaknessPK(), weakness.getWeaknessName());
		
		if(weaknessService.deleteWeakness(pk)) {
			modelAndView.addObject("message", weakness.getWeaknessName() + " successfully deleted from Weaknesses.");
		}else {
			modelAndView.addObject("message", "Failed to delete " + weakness.getWeaknessName() + " from Weaknesses.");
		}
		
		// Getting list of weaknesses as GenericEntity list
		List<GenericEntity> entityList = convertToGenericEntityList(weaknessService.getAllWeakness());
		
		modelAndView.addObject("entity", entity);
		modelAndView.addObject("entityList", entityList);
		modelAndView.setViewName("entity/show-all-entities");
		
		return modelAndView;
	}
	
	@PostMapping("weakness/add")
	public ResponseEntity<Map<String, Boolean>> sneakyAddWeakness(@RequestBody Weakness weakness) {
		Map<String, Boolean> response = new HashMap<>();
		try {
	        weaknessService.createWeakness(weakness);
	        response.put("success", true);
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        response.put("success", false);
	        return ResponseEntity.badRequest().body(response);
	    }
	}
	
}
