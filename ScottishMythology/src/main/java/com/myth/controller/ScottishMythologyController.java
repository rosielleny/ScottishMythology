package com.myth.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.myth.entity.Being;
import com.myth.entity.composite.BeingComposite;
import com.myth.service.ScottishMythologyService;

@Controller
public class ScottishMythologyController {
	
	@Autowired
	private ScottishMythologyService scottishMythologyService;
	
	// HOME
	
	@CrossOrigin
	@RequestMapping("/")
	public ModelAndView createAnWeakness() {
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("home");
		
		return modelAndView;
		
	}
	
	// Model Attributes
	
	@ModelAttribute("beings")
	public List<String> getBeings() {
		// Getting all beings
	    List<BeingComposite> beings = scottishMythologyService.getAllBeingRecords(); 
	    // Transforming the list of beings into a list of being names
	    return beings.stream()
	            .map(being -> being.getBeingName())
	            .collect(Collectors.toList());
	}
	
//	
//	
//	// CREATE
//	
//	@CrossOrigin
//	@RequestMapping("/being/create")
//	public ModelAndView createAnBeing() {
//		
//		ModelAndView modelAndView = new ModelAndView();
//		// Creating GenericEntity object
//		GenericEntity entity = new GenericEntity(0, "");
//		// Adding it to the mav
//		modelAndView.addObject("entity", entity);
//		// Getting the list of being names and adding to to the mav as "entities"
//		modelAndView.addObject("entities", getBeings());
//		// Calling a function to set up all possible links required for the template
//		modelAndView = scottishMythologyService.setUpLinks("being", "beings", modelAndView);
//		
//		modelAndView.setViewName("entity/create-entity");
//		
//		return modelAndView;
//		
//	}
//
//	
//	@CrossOrigin
//	@RequestMapping("/being/save-new")
//	public ModelAndView saveBeing(@Valid @ModelAttribute("entity")GenericEntity entity, BindingResult results) {
//		
//		
//		ModelAndView modelAndView = new ModelAndView();
//		// If the input has errors it wont be submitted
//		if(results.hasErrors()) {
//			return new ModelAndView("entity/create-entity", "entity", entity);
//		}
//		
//		String message = null;
//		
//		if(entity != null) {
//			// If we have Entity we convert it back into Being
//			Being being = new Being();
//			being.setBeingName(entity.getEntityName());
//			// Then call the service to create it
//			scottishMythologyService.createBeing(being);
//			message = being.getBeingName() + " successfully added to the database.";
//		}
//		else {
//			message = "An error occurred. Being not added to the database.";
//		}
//		
//		// Adding the message
//		modelAndView.addObject("message", message);
//		// Adding all possible necessary links
//		modelAndView = scottishMythologyService.setUpLinks("being", "beings", modelAndView);
//		
//		modelAndView.setViewName("entity/entity-output");
//		
//		return modelAndView;
//	}
	
	
	// READ
	// Functions as the main page, showing all entities and offering a search bar for individual entities at the top
	@CrossOrigin
	@RequestMapping("/being/beings")
	public ModelAndView showAllBeings() {
		
		ModelAndView modelAndView = new ModelAndView();
		
		// Getting a list of BeingComposite objects
		List<BeingComposite> beingList = scottishMythologyService.getAllBeingRecords();

		modelAndView.addObject("beingList", beingList);
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("being", "beings", modelAndView);
	
		modelAndView.setViewName("being/show-all-beings");
	
		return modelAndView;
	}
	
	@CrossOrigin
	@RequestMapping("/being/search")
	public ModelAndView searchBeingsByName(HttpServletRequest request) {
		
		ModelAndView modelAndView = new ModelAndView();
		// Getting being by name
		BeingComposite being = scottishMythologyService.getBeingRecordByName(request.getParameter("name"));
		// Setting up all possible links
		modelAndView = scottishMythologyService.setUpLinks("being", "beings", modelAndView);
		
		if(being!=null) {
			// If being was found
			modelAndView.addObject("being", being);
			modelAndView.setViewName("being/show-being");
		}
		else {
			
			modelAndView.addObject("message", "No beings were found for " + request.getParameter("name"));
			modelAndView.setViewName("being/show-being");
		}
		
		return modelAndView;
	}
//	
//	// UPDATE
//	
//	@CrossOrigin
//	@RequestMapping("/being/edit")
//	public ModelAndView editBeings(@RequestParam int pk) {
//		
//		ModelAndView modelAndView = new ModelAndView();
//		// Getting being by Id
//		Being being = scottishMythologyService.getBeingById(pk);
//		// Converting being to GenericEntity
//		GenericEntity ogEntity = new GenericEntity(being.getBeingPK(), being.getBeingName());
//		// Creating a second store for the same entity
//		GenericEntity entity = new GenericEntity(ogEntity.getEntityPK(), ogEntity.getEntityName());
//		
//		// Adding the list of being names
//		modelAndView.addObject("entities", getBeings());
//		
//		modelAndView.addObject("entity", entity);
//		// Adding all possible links
//		modelAndView = scottishMythologyService.setUpLinks("being", "beings", modelAndView);
//		modelAndView.addObject("ogEntity", ogEntity);
//		modelAndView.setViewName("entity/edit-entity");
//		
//		
//		return modelAndView;
//	}
//	
//	@CrossOrigin
//	@RequestMapping("/being/save-edit")
//	public ModelAndView saveEdittedBeing(@Valid @ModelAttribute("entity")GenericEntity entity, BindingResult results){
//		
//		
//		ModelAndView modelAndView = new ModelAndView();
//		
//		if(results.hasErrors()) {
//			return new ModelAndView("entity/edit-entity", "entity", entity);
//		}
//		
//		String message = null;
//		
//		if(entity != null) {
//			// Getting the original being by PK
//			Being being = scottishMythologyService.getBeingById(entity.getEntityPK());
//			// Setting the name to the new name
//			being.setBeingName(entity.getEntityName());
//			
//			if(scottishMythologyService.updateBeing(being)) {
//				
//				message = being.getBeingName() + " successfully updated";
//				
//			}else {
//				message = "An error occurred. Being not updated. Backend";
//			}
//		}
//		else {
//			message = "An error occurred. Being not updated. Frontend";
//		}
//		
//		// Getting the new being by ID
//		Being newBeing = scottishMythologyService.getBeingById(entity.getEntityPK());
//		// Converting to GenericEntity
//		GenericEntity newEntity = new GenericEntity(newBeing.getBeingPK(), newBeing.getBeingName());
//		modelAndView.addObject("entity", newEntity);
//		modelAndView.addObject("message", message);
//		// Adding all possible links to the mav
//		modelAndView = scottishMythologyService.setUpLinks("being", "beings", modelAndView);
//		
//		modelAndView.setViewName("entity/entity-output");
//		
//		return modelAndView;
//	}
//	
//	// DELETE
//	
//	@CrossOrigin
//	@RequestMapping("/being/delete/{pk}")
//	public ModelAndView deleteBeings(@PathVariable int pk) {
//		
//		ModelAndView modelAndView = new ModelAndView();
//		// Adding all possible links
//		modelAndView = scottishMythologyService.setUpLinks("being", "beings", modelAndView);
//		// Getting the being by PK
//		Being being = scottishMythologyService.getBeingById(pk);
//		// Converting to GenericEntity
//		GenericEntity entity = new GenericEntity(being.getBeingPK(), being.getBeingName());
//		
//		if(scottishMythologyService.deleteBeing(pk)) {
//			modelAndView.addObject("message", being.getBeingName() + " successfully deleted from Beings.");
//		}else {
//			modelAndView.addObject("message", "Failed to delete " + being.getBeingName() + " from Beings.");
//		}
//		
//		// Getting list of beings as GenericEntity list
//		List<GenericEntity> entityList = convertToGenericEntityList(scottishMythologyService.getAllBeing());
//		
//		modelAndView.addObject("entity", entity);
//		modelAndView.addObject("entityList", entityList);
//		modelAndView.setViewName("entity/show-all-entities");
//		
//		return modelAndView;
//	}
//
}
