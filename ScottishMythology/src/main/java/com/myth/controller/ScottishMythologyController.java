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
import com.myth.entity.junction.BeingAbility;
import com.myth.entity.junction.BeingLocation;
import com.myth.entity.junction.BeingStory;
import com.myth.entity.junction.BeingSymbolism;
import com.myth.entity.junction.BeingWeakness;
import com.myth.entity.junction.KeyBeingAbility;
import com.myth.entity.junction.KeyBeingLocation;
import com.myth.entity.junction.KeyBeingStory;
import com.myth.entity.junction.KeyBeingSymbolism;
import com.myth.entity.junction.KeyBeingWeakness;
import com.myth.service.*;
import com.myth.service.junction.BeingAbilityService;
import com.myth.service.junction.BeingLocationService;
import com.myth.service.junction.BeingStoryService;
import com.myth.service.junction.BeingSymbolismService;
import com.myth.service.junction.BeingWeaknessService;
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
	@Autowired
	private BeingAbilityService beingAbilityService;
	@Autowired
	private BeingLocationService beingLocationService;
	@Autowired
	private BeingStoryService beingStoryService;
	@Autowired
	private BeingSymbolismService beingSymbolismService;
	@Autowired
	private BeingWeaknessService beingWeaknessService;
	@Autowired
	private BeingService beingService;

	// HOME

	@CrossOrigin
	@RequestMapping("/")
	public ModelAndView home() {

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
		modelAndView = scottishMythologyService.setUpLinks("being", "beings", modelAndView);
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

		modelAndView.setViewName("being/create-being");

		return modelAndView;

	}


	@CrossOrigin
	@RequestMapping("/being/save-new")
	public ModelAndView saveBeing(@Valid @ModelAttribute("being")BeingComposite being, BindingResult results) {


		ModelAndView modelAndView = new ModelAndView();
		modelAndView = scottishMythologyService.setUpLinks("being", "beings", modelAndView);

		// If the input has errors it wont be submitted
		if(results.hasErrors()) {
			return new ModelAndView("being/create-being", "being", being);
		}

		String message = null;

		try {
			
			if(being != null) {

				
				// Call the service to create it
				if(scottishMythologyService.createBeingRecord(being) != null) {
					message = being.getBeingName() + " successfully added to the database.";
				}else {
					throw new RuntimeException("A backend error occurred. Being not added to the database.");
					}
			
			}
			else {
				message = "A frontend error occurred. Being not added to the database.";
			}
			
		}catch(Exception e) {
			message = "An error occurred. Being not added to the database.";
		}

		// Adding the message
		modelAndView.addObject("message", message);
		// Adding all possible necessary links

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

		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("being", "beings", modelAndView);
		modelAndView.addObject("beingList", beingList);

		modelAndView.setViewName("being/show-all-beings");

		return modelAndView;
	}

	@CrossOrigin
	@RequestMapping("/being/search")
	public ModelAndView searchBeingsByName(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView();
		// Getting being by name
		Being beingBase = beingService.getBeingByName(request.getParameter("name"));
		BeingComposite being = null;

		modelAndView = scottishMythologyService.setUpLinks("being", "beings", modelAndView);

		if(beingBase!=null) {
			being = scottishMythologyService.getBeingRecordByName(request.getParameter("name"));
		}
		// Setting up all possible links

		if(being!=null) {
			// If being was found
			modelAndView.addObject("being", being);
			modelAndView.setViewName("being/show-being");
		}
		else {
			// Ability, Weakness, and Symbols all have potential cross-over, so they all must be displayed in a search
			if(abilityService.getAbilityByName(request.getParameter("name")) != null || weaknessService.getWeaknessByName(request.getParameter("name")) !=null || symbolService.getSymbolByName(request.getParameter("name")) !=null) {
				System.out.println("Ability/Weakness/Symbol has been found");
				List<BeingComposite> beingsList = new ArrayList<BeingComposite>();

				if(abilityService.getAbilityByName(request.getParameter("name")) !=null) {
					// Finding the ability name that was searched
					System.out.println("Ability has been found");

					Ability abilitySearched = abilityService.getAbilityByName(request.getParameter("name"));
					int aID = abilitySearched.getAbilityPK();

					System.out.println("Ability searched: " + abilitySearched);
					System.out.println("Ability ID: " + aID);

					// Using ability's ID to get the IDs of beings which are matched to that ability

					List<BeingAbility> beingAbilities = beingAbilityService.getBeingAbilityByAbilityId(aID);
					if(beingAbilities !=null) {
						for(BeingAbility beingAbility : beingAbilities) {

							KeyBeingAbility baKey = beingAbility.getId();
							int beingID = baKey.getBeingPK();

							BeingComposite foundBeing = scottishMythologyService.getBeingRecordById(beingID);

							beingsList.add(foundBeing);


						}
					}

					System.out.println(beingsList);
					// Abilities can also be weaknesses so we want to search both of these at the same time
				}if(weaknessService.getWeaknessByName(request.getParameter("name")) !=null) {

					Weakness weaknessSearched = weaknessService.getWeaknessByName(request.getParameter("name"));
					int wID = weaknessSearched.getWeaknessPK();

					// Using weakness's ID to get the IDs of beings which are matched to that weakness

					List<BeingWeakness> beingWeaknesses = beingWeaknessService.getBeingWeaknessByWeaknessId(wID);

					if(beingWeaknesses != null) {
						for(BeingWeakness beingWeakness : beingWeaknesses) {

							KeyBeingWeakness bwKey = beingWeakness.getId();
							int beingID = bwKey.getBeingPK();

							BeingComposite foundBeing = scottishMythologyService.getBeingRecordById(beingID);

							if(!beingsList.contains(foundBeing)) {
								beingsList.add(foundBeing);
							}
						}
					}

					System.out.println(beingsList);
				}if(symbolService.getSymbolByName(request.getParameter("name")) !=null) {

					Symbol symbolSearched = symbolService.getSymbolByName(request.getParameter("name"));
					int sID = symbolSearched.getSymbolPK();

					// Using symbol's ID to get the IDs of beings which are matched to that symbol

					List<BeingSymbolism> beingSymbols = beingSymbolismService.getBeingSymbolismBySymbolismId(sID);

					if(beingSymbols != null) {
						for(BeingSymbolism beingSymbol : beingSymbols) {

							KeyBeingSymbolism bsKey = beingSymbol.getId();
							int beingID = bsKey.getBeingPK();

							BeingComposite foundBeing = scottishMythologyService.getBeingRecordById(beingID);

							if(!beingsList.contains(foundBeing)) {
								beingsList.add(foundBeing);
							}
						}
					}
				}


				if(beingsList.size() >0) {
					modelAndView.addObject("message", "Beings found for "+ request.getParameter("name"));
					modelAndView.addObject("beingList", beingsList);
				}
				else {
					modelAndView.addObject("message", "No beings were found for " + request.getParameter("name"));
				}
			}
			else if(factionService.getFactionByName(request.getParameter("name")) !=null){

				Faction factionSearched = factionService.getFactionByName(request.getParameter("name"));
				int fID = factionSearched.getFactionPK();

				// Using faction's ID to get the IDs of beings which are matched to that faction

				List<BeingComposite> beingsList = scottishMythologyService.getBeingRecordByFaction(fID);

				if(beingsList !=null && beingsList.size() >0) {
					modelAndView.addObject("message", "Beings found for "+ request.getParameter("name"));
					modelAndView.addObject("beingList", beingsList);
				}
				else {
					modelAndView.addObject("message", "No beings were found for " + request.getParameter("name"));
				}

			}
			else if(genderService.getGenderByName(request.getParameter("name")) !=null) {

				Gender genderSearched = genderService.getGenderByName(request.getParameter("name"));
				int gID = genderSearched.getGenderPK();

				List<BeingComposite> beingsList = scottishMythologyService.getBeingRecordByGender(gID);

				if(beingsList !=null && beingsList.size() >0) {
					modelAndView.addObject("message", "Beings found for "+ request.getParameter("name"));
					modelAndView.addObject("beingList", beingsList);
				}
				else {
					modelAndView.addObject("message", "No beings were found for " + request.getParameter("name"));
				}
			}
			else if(speciesService.getSpeciesByName(request.getParameter("name")) !=null) {

				Species speciesSearched = speciesService.getSpeciesByName(request.getParameter("name"));
				int spID = speciesSearched.getSpeciesPK();

				List<BeingComposite> beingsList = scottishMythologyService.getBeingRecordBySpecies(spID);

				if(beingsList !=null && beingsList.size() >0) {
					modelAndView.addObject("message", "Beings found for "+ request.getParameter("name"));
					modelAndView.addObject("beingList", beingsList);
				}
				else {
					modelAndView.addObject("message", "No beings were found for " + request.getParameter("name"));
				}
			}
			else if(locationService.getLocationByName(request.getParameter("name")) !=null) {

				Location locationSearched = locationService.getLocationByName(request.getParameter("name"));
				int lID = locationSearched.getLocationPK();

				// Using symbol's ID to get the IDs of beings which are matched to that symbol

				List<BeingLocation> beingLocations = beingLocationService.getBeingLocationByLocationId(lID);
				List<BeingComposite> beingsList = new ArrayList<BeingComposite>();

				for(BeingLocation beingLocation : beingLocations) {

					KeyBeingLocation blKey = beingLocation.getId();
					int beingID = blKey.getBeingPK();

					BeingComposite foundBeing = scottishMythologyService.getBeingRecordById(beingID);

					if(!beingsList.contains(foundBeing)) {
						beingsList.add(foundBeing);
					}
				}

				if(beingsList !=null && beingsList.size() >0) {
					modelAndView.addObject("message", "Beings found for "+ request.getParameter("name"));
					modelAndView.addObject("beingList", beingsList);
				}
				else {
					modelAndView.addObject("message", "No beings were found for " + request.getParameter("name"));
				}
			}
			else if(storyService.getStoryByName(request.getParameter("name")) !=null) {

				Story storySearched = storyService.getStoryByName(request.getParameter("name"));
				int storID = storySearched.getStoryPK();

				// Using symbol's ID to get the IDs of beings which are matched to that symbol

				List<BeingStory> beingStories = beingStoryService.getBeingStoryByStoryId(storID);
				List<BeingComposite> beingsList = new ArrayList<BeingComposite>();

				for(BeingStory beingStory : beingStories) {

					KeyBeingStory blKey = beingStory.getId();
					int beingID = blKey.getBeingPK();

					BeingComposite foundBeing = scottishMythologyService.getBeingRecordById(beingID);

					if(!beingsList.contains(foundBeing)) {
						beingsList.add(foundBeing);
					}
				}

				if(beingsList !=null && beingsList.size() >0) {
					modelAndView.addObject("message", "Beings found for "+ request.getParameter("name"));
					modelAndView.addObject("beingList", beingsList);
				}
				else {
					modelAndView.addObject("message", "No beings were found for " + request.getParameter("name"));
				}
			}
			else {

				modelAndView.addObject("message", "No beings were found for " + request.getParameter("name"));
			}
		}

		modelAndView.setViewName("being/show-being");
		return modelAndView;
	}

	// UPDATE

	@CrossOrigin
	@RequestMapping("/being/edit")
	public ModelAndView editBeings(@RequestParam int pk) {

		ModelAndView modelAndView = new ModelAndView();
		
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("being", "beings", modelAndView);
		
		// Getting being by Id
		BeingComposite ogBeing = scottishMythologyService.getBeingRecordById(pk);

		// Creating a second store for the same entity
		BeingComposite being = ogBeing;

		// Adding the list of being names
		modelAndView.addObject("beings", getBeings());

		modelAndView.addObject("being", being);
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
