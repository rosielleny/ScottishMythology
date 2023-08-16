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

import com.myth.entity.*;
import com.myth.entity.composite.BeingComposite;
import com.myth.service.*;
import com.myth.service.micro.*;

@Controller
public class ScottishMythologyController {
	
	@Autowired
	private ScottishMythologyService scottishMythologyService;
	@Autowired
	private AbilityService abilityService;
	@Autowired
	private FactionService factionService;
	@Autowired
	private GenderService genderService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private SpeciesService speciesService;
	@Autowired
	private StoryService storyService;
	@Autowired
	private SymbolService symbolService;
	@Autowired
	private WeaknessService weaknessService;
	
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
	
	@ModelAttribute("abilities")
	public List<String> getAbilities() {
		// Getting all beings
	    List<Ability> abilities = abilityService.getAllAbility(); 
	    // Transforming the list of beings into a list of being names
	    return abilities.stream()
	            .map(ability -> ability.getAbilityName())
	            .collect(Collectors.toList());
	}
	
	@ModelAttribute("factions")
	public List<String> getFactions() {
		// Getting all beings
	    List<Faction> factions = factionService.getAllFaction(); 
	    // Transforming the list of beings into a list of being names
	    return factions.stream()
	            .map(faction -> faction.getFactionName())
	            .collect(Collectors.toList());
	}

	@ModelAttribute("genders")
	public List<String> getGenders() {
		// Getting all beings
	    List<Gender> genders = genderService.getAllGender(); 
	    // Transforming the list of beings into a list of being names
	    return genders.stream()
	            .map(gender -> gender.getGenderType())
	            .collect(Collectors.toList());
	}
	
	@ModelAttribute("locations")
	public List<String> getLocations() {
		// Getting all beings
	    List<Location> locations = locationService.getAllLocation(); 
	    // Transforming the list of beings into a list of being names
	    return locations.stream()
	            .map(location -> location.getLocationName())
	            .collect(Collectors.toList());
	}
	
	@ModelAttribute("speciesList")
	public List<String> getSpecies() {
		// Getting all beings
	    List<Species> speciesList = speciesService.getAllSpecies(); 
	    // Transforming the list of beings into a list of being names
	    return speciesList.stream()
	            .map(species -> species.getSpeciesName())
	            .collect(Collectors.toList());
	}
	
	@ModelAttribute("stories")
	public List<String> getStories() {
		// Getting all beings
	    List<Story> stories = storyService.getAllStory(); 
	    // Transforming the list of beings into a list of being names
	    return stories.stream()
	            .map(story -> story.getStoryName())
	            .collect(Collectors.toList());
	}
	
	@ModelAttribute("symbols")
	public List<String> getSymbols() {
		// Getting all beings
	    List<Symbol> symbols = symbolService.getAllSymbol(); 
	    // Transforming the list of beings into a list of being names
	    return symbols.stream()
	            .map(symbol -> symbol.getSymbolName())
	            .collect(Collectors.toList());
	}
	
	@ModelAttribute("weaknesses")
	public List<String> getWeaknesses() {
		// Getting all beings
	    List<Weakness> weaknesses = weaknessService.getAllWeakness(); 
	    // Transforming the list of beings into a list of being names
	    return weaknesses.stream()
	            .map(weakness -> weakness.getWeaknessName())
	            .collect(Collectors.toList());
	}
	
	
	// CREATE
	
	@CrossOrigin
	@RequestMapping("/being/create")
	public ModelAndView createAnBeing() {

		
		ModelAndView modelAndView = new ModelAndView();
		
		BeingComposite being = new BeingComposite();
		// Adding it to the mav
		modelAndView.addObject("being", being);
		// Getting the list of being names and adding to to the mav as "entities"
		modelAndView.addObject("beings", getBeings());
		modelAndView.addObject("abilities", getAbilities());
		modelAndView.addObject("factions", getFactions());
		modelAndView.addObject("genders", getGenders());
		modelAndView.addObject("locations", getLocations());
		modelAndView.addObject("speciesList", getSpecies());
		modelAndView.addObject("stories", getStories());
		modelAndView.addObject("symbols", getSymbols());
		modelAndView.addObject("weaknesses", getWeaknesses());
		// Calling a function to set up all possible links required for the template
		modelAndView = scottishMythologyService.setUpLinks("being", "beings", modelAndView);
		
		modelAndView.setViewName("being/create-being");
		
		return modelAndView;
		
	}

	
	@CrossOrigin
	@RequestMapping("/being/save-new")
	public ModelAndView saveBeing(@Valid @ModelAttribute("being")BeingComposite being, BindingResult results) {
		
		
		ModelAndView modelAndView = new ModelAndView();
		// If the input has errors it wont be submitted
		if(results.hasErrors()) {
			return new ModelAndView("being/create-entity", "being", being);
		}
		
		String message = null;
		
		if(being != null) {
			
			// Call the service to create it
			if(scottishMythologyService.createBeingRecord(being) != null) {
				message = being.getBeingName() + " successfully added to the database.";
			}else {
				message = "An backend error occurred. Being not added to the database.";
			}
		}
		else {
			message = "An frontend error occurred. Being not added to the database.";
		}
		
		// Adding the message
		modelAndView.addObject("message", message);
		// Adding all possible necessary links
		modelAndView = scottishMythologyService.setUpLinks("being", "beings", modelAndView);
		
		modelAndView.setViewName("being/being-output");
		
		return modelAndView;
	}
	
	
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
	
	// UPDATE
	
	@CrossOrigin
	@RequestMapping("/being/edit")
	public ModelAndView editBeings(@RequestParam int pk) {
		
		ModelAndView modelAndView = new ModelAndView();
		// Getting being by Id
		BeingComposite ogBeing = scottishMythologyService.getBeingRecordById(pk);
		
		// Creating a second store for the same entity
		BeingComposite being = ogBeing;
		
		// Adding the list of being names
		modelAndView.addObject("beings", getBeings());
		
		modelAndView.addObject("being", being);
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("being", "beings", modelAndView);
		modelAndView.addObject("ogBeing", ogBeing);
		modelAndView.setViewName("being/edit-being");
		
		
		return modelAndView;
	}
	
	@CrossOrigin
	@RequestMapping("/being/save-edit")
	public ModelAndView saveEdittedBeing(@Valid @ModelAttribute("being")BeingComposite being, BindingResult results){
		
		
		ModelAndView modelAndView = new ModelAndView();
		
		if(results.hasErrors()) {
			return new ModelAndView("being/edit-being", "being", being);
		}
		
		String message = null;
		
		if(being != null) {
			
			if(scottishMythologyService.updateBeingRecord(being)) {
				
				message = being.getBeingName() + " successfully updated";
				
			}else {
				message = "A backend error occurred. Being not updated.";
			}
		}
		else {
			message = "A frontend error occurred. Being not updated.";
		}
		
		// Getting the new being by ID
		BeingComposite newBeing = scottishMythologyService.getBeingRecordById(being.getBeingPK());
		// Converting to GenericEntity
		modelAndView.addObject("being", newBeing);
		modelAndView.addObject("message", message);
		// Adding all possible links to the mav
		modelAndView = scottishMythologyService.setUpLinks("being", "beings", modelAndView);
		
		modelAndView.setViewName("being/being-output");
		
		return modelAndView;
	}
	
	// DELETE
	
	@CrossOrigin
	@RequestMapping("/being/delete/{pk}")
	public ModelAndView deleteBeings(@PathVariable int pk) {
		
		ModelAndView modelAndView = new ModelAndView();
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("being", "beings", modelAndView);
		// Getting the being by PK
		BeingComposite being = scottishMythologyService.getBeingRecordById(pk);
		// Converting to GenericEntity
	
		if(scottishMythologyService.deleteBeingRecord(pk)) {
			modelAndView.addObject("message", being.getBeingName() + " successfully deleted from Beings.");
		}else {
			modelAndView.addObject("message", "Failed to delete " + being.getBeingName() + " from Beings.");
		}
		
		// Getting list of beings as GenericEntity list
		List<BeingComposite> beingList = scottishMythologyService.getAllBeingRecords();
		
		modelAndView.addObject("entity", being);
		modelAndView.addObject("entityList", beingList);
		modelAndView.setViewName("being/show-all-beings");
		
		return modelAndView;
	}

}
