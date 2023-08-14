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

import com.myth.entity.Gender;
import com.myth.entity.Gender;
import com.myth.entity.composite.GenericEntity;
import com.myth.service.ScottishMythologyService;
import com.myth.service.micro.GenderService;

@Controller
public class GenderController {
	
	@Autowired
	private GenderService genderService;
	@Autowired
	private ScottishMythologyService scottishMythologyService;
	
	// Model Attributes
	
	@ModelAttribute("genders")
	public List<String> getGenders() {
		// Getting all genders
	    List<Gender> genders = genderService.getAllGender(); 
	    // Transforming the list of genders into a list of gender names
	    return genders.stream()
	            .map(gender -> gender.getGenderType())
	            .collect(Collectors.toList());
	}
	
	// A function to convert a list of Genders into a list of GenericEntities
	public List<GenericEntity> convertToGenericEntityList(List<Gender> genderList){
		
		List<GenericEntity> entityList = new ArrayList<GenericEntity>();
		// For each gender in the list, pass the values into the GenericEntity constructor and add to the entity list
		for(Gender gender: genderList) {
			GenericEntity entity = new GenericEntity(gender.getGenderPK(), gender.getGenderType());
			entityList.add(entity);
		}
		
		return entityList;
	}
	
	
	// CREATE
	
	@CrossOrigin
	@RequestMapping("/gender/create")
	public ModelAndView createAnGender() {
		
		ModelAndView modelAndView = new ModelAndView();
		// Creating GenericEntity object
		GenericEntity entity = new GenericEntity(0, "");
		// Adding it to the mav
		modelAndView.addObject("entity", entity);
		// Getting the list of gender names and adding to to the mav as "entities"
		modelAndView.addObject("entities", getGenders());
		// Calling a function to set up all possible links required for the template
		modelAndView = scottishMythologyService.setUpLinks("gender", "genders", modelAndView);
		
		modelAndView.setViewName("entity/create-entity");
		
		return modelAndView;
		
	}

	
	@CrossOrigin
	@RequestMapping("/gender/save-new")
	public ModelAndView saveGender(@Valid @ModelAttribute("entity")GenericEntity entity, BindingResult results) {
		
		
		ModelAndView modelAndView = new ModelAndView();
		// If the input has errors it wont be submitted
		if(results.hasErrors()) {
			return new ModelAndView("entity/create-entity", "entity", entity);
		}
		
		String message = null;
		
		if(entity != null) {
			// If we have Entity we convert it back into Gender
			Gender gender = new Gender();
			gender.setGenderType(entity.getEntityName());
			// Then call the service to create it
			genderService.createGender(gender);
			message = gender.getGenderType() + " successfully added to the database.";
		}
		else {
			message = "An error occurred. Gender not added to the database.";
		}
		
		// Adding the message
		modelAndView.addObject("message", message);
		// Adding all possible necessary links
		modelAndView = scottishMythologyService.setUpLinks("gender", "genders", modelAndView);
		
		modelAndView.setViewName("entity/entity-output");
		
		return modelAndView;
	}
	
	
	// READ
	// Functions as the main page, showing all entities and offering a search bar for individual entities at the top
	@CrossOrigin
	@RequestMapping("/gender/genders")
	public ModelAndView showAllGenders() {
		
		ModelAndView modelAndView = new ModelAndView();
		
		// Getting all genders and converting the list to a list of GenericEntities
		List<GenericEntity> entityList = convertToGenericEntityList(genderService.getAllGender());
		
		modelAndView.addObject("entityList", entityList);
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("gender", "genders", modelAndView);
	
		modelAndView.setViewName("entity/show-all-entities");
	
		return modelAndView;
	}
	
	@CrossOrigin
	@RequestMapping("/gender/search")
	public ModelAndView searchGendersByName(HttpServletRequest request) {
		
		ModelAndView modelAndView = new ModelAndView();
		// Getting gender by name
		Gender gender = genderService.getGenderByName(request.getParameter("name"));
		// Setting up all possible links
		modelAndView = scottishMythologyService.setUpLinks("gender", "genders", modelAndView);
		
		if(gender!=null) {
			// If gender was found, we convert to GenericEntity
			GenericEntity entity = new GenericEntity(gender.getGenderPK(), gender.getGenderType());
			modelAndView.addObject("entity", entity);
			modelAndView.setViewName("entity/show-entity");
		}
		else {
			
			modelAndView.addObject("message", "No genders were found for " + request.getParameter("name"));
			modelAndView.setViewName("entity/show-entity");
		}
		
		return modelAndView;
	}
	
	// UPDATE
	
	@CrossOrigin
	@RequestMapping("/gender/edit")
	public ModelAndView editGenders(@RequestParam int pk) {
		
		ModelAndView modelAndView = new ModelAndView();
		// Getting gender by Id
		Gender gender = genderService.getGenderById(pk);
		// Converting gender to GenericEntity
		GenericEntity ogEntity = new GenericEntity(gender.getGenderPK(), gender.getGenderType());
		// Creating a second store for the same entity
		GenericEntity entity = new GenericEntity(ogEntity.getEntityPK(), ogEntity.getEntityName());
		
		// Adding the list of gender names
		modelAndView.addObject("entities", getGenders());
		
		modelAndView.addObject("entity", entity);
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("gender", "genders", modelAndView);
		modelAndView.addObject("ogEntity", ogEntity);
		modelAndView.setViewName("entity/edit-entity");
		
		
		return modelAndView;
	}
	
	@CrossOrigin
	@RequestMapping("/gender/save-edit")
	public ModelAndView saveEdittedGender(@Valid @ModelAttribute("entity")GenericEntity entity, BindingResult results){
		
		
		ModelAndView modelAndView = new ModelAndView();
		
		if(results.hasErrors()) {
			return new ModelAndView("entity/edit-entity", "entity", entity);
		}
		
		String message = null;
		
		if(entity != null) {
			// Getting the original gender by PK
			Gender gender = genderService.getGenderById(entity.getEntityPK());
			// Setting the name to the new name
			gender.setGenderType(entity.getEntityName());
			
			if(genderService.updateGender(gender)) {
				
				message = gender.getGenderType() + " successfully updated";
				
			}else {
				message = "An error occurred. Gender not updated. Backend";
			}
		}
		else {
			message = "An error occurred. Gender not updated. Frontend";
		}
		
		// Getting the new gender by ID
		Gender newGender = genderService.getGenderById(entity.getEntityPK());
		// Converting to GenericEntity
		GenericEntity newEntity = new GenericEntity(newGender.getGenderPK(), newGender.getGenderType());
		modelAndView.addObject("entity", newEntity);
		modelAndView.addObject("message", message);
		// Adding all possible links to the mav
		modelAndView = scottishMythologyService.setUpLinks("gender", "genders", modelAndView);
		
		modelAndView.setViewName("entity/entity-output");
		
		return modelAndView;
	}
	
	// DELETE
	
	@CrossOrigin
	@RequestMapping("/gender/delete/{pk}")
	public ModelAndView deleteGenders(@PathVariable int pk) {
		
		ModelAndView modelAndView = new ModelAndView();
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("gender", "genders", modelAndView);
		// Getting the gender by PK
		Gender gender = genderService.getGenderById(pk);
		// Converting to GenericEntity
		GenericEntity entity = new GenericEntity(gender.getGenderPK(), gender.getGenderType());
		
		if(genderService.deleteGender(pk)) {
			modelAndView.addObject("message", gender.getGenderType() + " successfully deleted from Genders.");
		}else {
			modelAndView.addObject("message", "Failed to delete " + gender.getGenderType() + " from Genders.");
		}
		
		// Getting list of genders as GenericEntity list
		List<GenericEntity> entityList = convertToGenericEntityList(genderService.getAllGender());
		
		modelAndView.addObject("entity", entity);
		modelAndView.addObject("entityList", entityList);
		modelAndView.setViewName("entity/show-all-entities");
		
		return modelAndView;
	}
	
	@PostMapping("gender/add")
	public ResponseEntity<Map<String, Boolean>> sneakyAddGender(@RequestBody Gender gender) {
		Map<String, Boolean> response = new HashMap<>();
		try {
	        genderService.createGender(gender);
	        response.put("success", true);
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        response.put("success", false);
	        return ResponseEntity.badRequest().body(response);
	    }
	}

}
