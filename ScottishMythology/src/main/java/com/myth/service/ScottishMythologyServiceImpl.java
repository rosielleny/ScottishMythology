package com.myth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.myth.entity.*;
import com.myth.entity.composite.BeingComposite;
import com.myth.entity.junction.*;
import com.myth.service.junction.*;
import com.myth.service.micro.*;

@Service
public class ScottishMythologyServiceImpl implements ScottishMythologyService {

	@Autowired
	BeingService beingService;
	@Autowired
	SpeciesService speciesService;
	@Autowired
	GenderService genderService;
	@Autowired
	FactionService factionService;
	@Autowired
	AbilityService abilityService;
	@Autowired
	WeaknessService weaknessService;
	@Autowired
	LocationService locationService;
	@Autowired
	SymbolService symbolService;
	@Autowired
	StoryService storyService;
	@Autowired
	BeingAbilityService beingAbilityService;
	@Autowired
	BeingLocationService beingLocationService;
	@Autowired
	BeingStoryService beingStoryService;
	@Autowired
	BeingSymbolismService beingSymbolismService;
	@Autowired
	BeingWeaknessService beingWeaknessService;
	
	
	
	@Override
	public BeingComposite createBeingRecord(BeingComposite beingComposite) {

		try {
			// Extracting Being from BeingComposite
			Being being = getBeingFromComposite(beingComposite); 
	
			// Creating a new Being
			beingService.createBeing(being);
			Being newBeing = beingService.getBeingByName(beingComposite.getBeingName());
			beingComposite.setBeingPK(newBeing.getBeingPK());

			// Extracting attributes from BeingComposite and creating new components
			boolean abilitiesCreated = createUpdateBeingAbility(beingComposite);
			System.out.println("Ability: "+abilitiesCreated);
			boolean weaknessesCreated = createUpdateBeingWeakness(beingComposite);
			System.out.println("Weakness: "+weaknessesCreated);
			boolean symbolsCreated = createUpdateBeingSymbol(beingComposite);
			System.out.println("Symbol: "+symbolsCreated);
			boolean storiesCreated = createUpdateBeingStory(beingComposite);
			System.out.println("Story: "+storiesCreated);
			boolean locationsCreated = createUpdateBeingLocation(beingComposite);
			System.out.println("Location: "+locationsCreated);

			if (abilitiesCreated && weaknessesCreated && symbolsCreated && storiesCreated && locationsCreated) {
				return getBeingRecordByName(being.getBeingName());
			} else {
				// Handling error by deleting the being
				beingService.deleteBeing(being.getBeingPK());

				return null;
			}
		} catch (Exception e) {

			return null; 
		}

	}

	@Override
	public List<BeingComposite> getAllBeingRecords() {

		List<Being> beings = beingService.getAllBeing();
		List<BeingComposite> beingRecords = new ArrayList<BeingComposite>();

		for(Being being: beings) {

			BeingComposite beingComposite = convertToComposite(being);

			beingRecords.add(beingComposite);	
		}

		if(beingRecords.size()>0) {

			return beingRecords;
		}

		return null;

	}

	@Override
	public BeingComposite getBeingRecordById(int beingPK) {

		Being being = beingService.getBeingById(beingPK);

		return convertToComposite(being);

	}

	@Override
	public BeingComposite getBeingRecordByName(String beingName) {

		Being being = beingService.getBeingByName(beingName);

		return convertToComposite(being);
	}
	
	@Transactional
	@Override
	public Boolean updateBeingRecord(BeingComposite beingComposite) {

		try {
			boolean abilityUpdated = false;
			boolean locationsUpdated = false;
			boolean storiesUpdated = false;
			boolean symbolismUpdated = false;
			boolean weaknessesUpdated = false;

			BeingComposite oldBC = getBeingRecordById(beingComposite.getBeingPK());
			Being being = beingService.getBeingById(beingComposite.getBeingPK());

			if (being == null) {
				return false;
			}
			boolean beingUpdated = beingService.updateBeing(being);


			if(oldBC.getBeingAbilities().equals(beingComposite.getBeingAbilities()) != true) {

				// Comparing the two lists against each other
				for(String i: oldBC.getBeingAbilities()) {
					// If an entry has been removed from the new list, it will be deleted from the junction table
					if(!beingComposite.getBeingAbilities().contains(i)) {

						Ability ability = abilityService.getAbilityByName(i);
						int id = ability.getAbilityPK();
						KeyBeingAbility key = new KeyBeingAbility(oldBC.getBeingPK(), id);
						beingAbilityService.deleteBeingAbility(key);
					}
				}
				abilityUpdated = createUpdateBeingAbility(beingComposite); // Handles entries that have been added
			}
			if(oldBC.getBeingLocations().equals(beingComposite.getBeingLocations()) != true) {

				for(String i: oldBC.getBeingLocations()) {

					if(!beingComposite.getBeingLocations().contains(i)) {

						Location location = locationService.getLocationByName(i);
						int id = location.getLocationPK();
						KeyBeingLocation key = new KeyBeingLocation(oldBC.getBeingPK(), id);
						beingLocationService.deleteBeingLocation(key);
					}
				}
				locationsUpdated = createUpdateBeingLocation(beingComposite);
			}
			if(oldBC.getBeingStories().equals(beingComposite.getBeingStories()) != true) {

				for(String i: oldBC.getBeingStories()) {

					if(!beingComposite.getBeingStories().contains(i)) {

						Story story = storyService.getStoryByName(i);
						int id = story.getStoryPK();
						KeyBeingStory key = new KeyBeingStory(oldBC.getBeingPK(), id);
						beingStoryService.deleteBeingStory(key);
					}
				}
				storiesUpdated = createUpdateBeingStory(beingComposite);
			}
			if(oldBC.getBeingSymbolism().equals(beingComposite.getBeingSymbolism()) != true) {

				for(String i: oldBC.getBeingSymbolism()) {

					if(!beingComposite.getBeingSymbolism().contains(i)) {

						Symbol symbol = symbolService.getSymbolByName(i);
						int id = symbol.getSymbolPK();
						KeyBeingSymbolism key = new KeyBeingSymbolism(oldBC.getBeingPK(), id);
						beingSymbolismService.deleteBeingSymbolism(key);
					}
				}
				symbolismUpdated = createUpdateBeingSymbol(beingComposite);
			}
			if(oldBC.getBeingWeaknesses().equals(beingComposite.getBeingWeaknesses()) != true) {

				for(String i: oldBC.getBeingWeaknesses()) {

					if(!beingComposite.getBeingWeaknesses().contains(i)) {

						Weakness weakness = weaknessService.getWeaknessByName(i);
						int id = weakness.getWeaknessPK();
						KeyBeingWeakness key = new KeyBeingWeakness(oldBC.getBeingPK(), id);
						beingWeaknessService.deleteBeingWeakness(key);
					}
				}
				weaknessesUpdated = createUpdateBeingWeakness(beingComposite);
			}
			if(abilityUpdated && locationsUpdated && storiesUpdated && symbolismUpdated && weaknessesUpdated && beingUpdated) {
				return true;
			}

			return false;

		}catch(Exception e){

			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean deleteBeingRecord(int beingPK) {
		
		return beingService.deleteBeing(beingPK);
	}

	// Method used to convert Being to a BeingComposite objects
	private BeingComposite convertToComposite(Being being) {

		String species = speciesService.getSpeciesById(being.getBeingSpecies()).getSpeciesName();
		String gender = genderService.getGenderById(being.getBeingGender()).getGenderType();
		String faction = factionService.getFactionById(being.getBeingFaction()).getFactionName();

		List<String> abilities = getAbilitiesFromBeingAbility(being.getBeingPK());
		List<String> weaknesses = getWeaknessesFromBeingWeakness(being.getBeingPK());
		List<String> symbols = getSymbolsFromBeingSymbolism(being.getBeingPK());
		List<String> stories = getStoriesFromBeingStory(being.getBeingPK());
		List<String> location = getLocationsFromBeingLocation(being.getBeingPK());

		return new BeingComposite(being, species, gender, faction, abilities, weaknesses, symbols, stories, location);

	}

	// Called by convertToComposite to get a list of Abilities belonging to the being
	private List<String> getAbilitiesFromBeingAbility(int beingPK) {

		List<BeingAbility> bAList = beingAbilityService.getBeingAbilityByBeingId(beingPK);
		List<String> abilities = new ArrayList<String>();

		for(BeingAbility ba: bAList) {

			KeyBeingAbility key = ba.getId();
			int abilityPK = key.getAbilityPK();
			Ability ability = abilityService.getAbilityById(abilityPK);
			String abilityName = ability.getAbilityName();

			abilities.add(abilityName);	
		}

		return abilities;
	}

	// Called by convertToComposite to get a list of Weaknesses belonging to the being
	private List<String> getWeaknessesFromBeingWeakness(int beingPk) {

		List<BeingWeakness> bWeaknessList = beingWeaknessService.getBeingWeaknessByBeingId(beingPk);
		List<String> weaknesss = new ArrayList<String>();

		for(BeingWeakness bWeakness: bWeaknessList) {

			KeyBeingWeakness key = bWeakness.getId();
			int weaknessPK = key.getWeaknessPK();
			Weakness weakness = weaknessService.getWeaknessById(weaknessPK);
			String weaknessName = weakness.getWeaknessName();

			weaknesss.add(weaknessName);	
		}

		return weaknesss;
	}

	// Called by convertToComposite to get a list of Symbols belonging to the being
	private List<String> getSymbolsFromBeingSymbolism(int beingPK) {

		List<BeingSymbolism> bSymbolList = beingSymbolismService.getBeingSymbolismByBeingId(beingPK);
		List<String> symbols = new ArrayList<String>();

		for(BeingSymbolism bSymbol: bSymbolList) {

			KeyBeingSymbolism key = bSymbol.getId();
			int symbolPK = key.getSymbolPK();
			Symbol symbol = symbolService.getSymbolById(symbolPK);
			String symbolName = symbol.getSymbolName();

			symbols.add(symbolName);	
		}

		return symbols;
	}

	// Called by convertToComposite to get a list of Stories belonging to the being
	private List<String> getStoriesFromBeingStory(int beingPK) {

		List<BeingStory> bStoryList = beingStoryService.getBeingStoryByBeingId(beingPK);
		List<String> storys = new ArrayList<String>();

		for(BeingStory bStory: bStoryList) {

			KeyBeingStory key = bStory.getId();
			int storyPK = key.getStoryPK();
			Story story = storyService.getStoryById(storyPK);
			String storyName = story.getStoryName();

			storys.add(storyName);	
		}

		return storys;
	}

	// Called by convertToComposite to get a list of Locations belonging to the being
	private List<String> getLocationsFromBeingLocation(int beingPK) {

		List<BeingLocation> bLocationsList = beingLocationService.getBeingLocationByBeingId(beingPK);
		List<String> locations = new ArrayList<String>();

		for(BeingLocation bLocations: bLocationsList) {

			KeyBeingLocation key = bLocations.getId();
			int locationPK = key.getLocationPK();
			Location location = locationService.getLocationById(locationPK);
			String locationName = location.getLocationName();

			locations.add(locationName);	
		}

		return locations;
	}

	// The following method is used to get Being from BeingComposite

	private Being getBeingFromComposite(BeingComposite beingComposite) {

		int beingPK = beingComposite.getBeingPK();
		String beingName = beingComposite.getBeingName();
		// Species
		Species species = speciesService.getSpeciesByName(beingComposite.getBeingSpecies());
		int beingSpecies = species.getSpeciesPK();
		String beingDescription = beingComposite.getBeingDescription();
		
		// Gender
		Gender gender = genderService.getGenderByName(beingComposite.getBeingGender());
		int beingGender = gender.getGenderPK();
		
		byte[] beingArt = beingComposite.getBeingArt();
		
		// Faction
		Faction faction = factionService.getFactionByName(beingComposite.getBeingFaction());
		int beingFaction = faction.getFactionPK();

		return new Being(beingPK, beingName, beingSpecies, beingDescription, beingGender, beingArt, beingFaction);


	}

	// CREATE UPDATE ///////////////////////////
	// Method used to extract abilities from BeingComposite and use them to create new entries in the AbilityBeings table

	private Boolean createUpdateBeingAbility(BeingComposite beingComposite) {

		boolean allAbilitiesCreatedSuccessfully = false;
		List<String> abilities = beingComposite.getBeingAbilities();

		for (String abilityName : abilities) {
			Ability ability = abilityService.getAbilityByName(abilityName);

			if (ability != null) {
				// Ability exists, create BeingAbility entry
				KeyBeingAbility key = new KeyBeingAbility(beingComposite.getBeingPK(), ability.getAbilityPK());
				
				if(beingAbilityService.getBeingAbilityById(key) == null) {
					BeingAbility beingAbility = new BeingAbility(key);
					beingAbilityService.createBeingAbility(beingAbility);
					allAbilitiesCreatedSuccessfully = true;
				}
				
				
			} else {
				allAbilitiesCreatedSuccessfully = false;
			}
		}

		return allAbilitiesCreatedSuccessfully;

	}

	private Boolean createUpdateBeingWeakness(BeingComposite beingComposite) {

		boolean allWeaknesssCreatedSuccessfully = false;
		List<String> weaknesss = beingComposite.getBeingWeaknesses();

		for (String weaknessName : weaknesss) {
			Weakness weakness = weaknessService.getWeaknessByName(weaknessName);

			if (weakness != null) {
				// Weakness exists, create BeingWeakness entry
				KeyBeingWeakness key = new KeyBeingWeakness(beingComposite.getBeingPK(), weakness.getWeaknessPK());
				
				if(beingWeaknessService.getBeingWeaknessById(key) == null) {
					BeingWeakness beingWeakness = new BeingWeakness(key);
					beingWeaknessService.createBeingWeakness(beingWeakness);
					allWeaknesssCreatedSuccessfully = true;
				}
				
			} else {
				allWeaknesssCreatedSuccessfully = false;
			}
		}
		return allWeaknesssCreatedSuccessfully;
	}

	private Boolean createUpdateBeingSymbol(BeingComposite beingComposite) {

		boolean allSymbolsCreatedSuccessfully = false;
		List<String> symbols = beingComposite.getBeingSymbolism();

		for (String symbolName : symbols) {
			Symbol symbol = symbolService.getSymbolByName(symbolName);

			if (symbol != null) {
				// Symbol exists, create BeingSymbolism entry
				KeyBeingSymbolism key = new KeyBeingSymbolism(beingComposite.getBeingPK(), symbol.getSymbolPK());
				
				if(beingSymbolismService.getBeingSymbolismById(key) == null) {
					BeingSymbolism beingSymbolism = new BeingSymbolism(key);
					beingSymbolismService.createBeingSymbolism(beingSymbolism);
					allSymbolsCreatedSuccessfully = true;
				}
			} else 
				allSymbolsCreatedSuccessfully = false;
		}



		return allSymbolsCreatedSuccessfully;

	}

	private Boolean createUpdateBeingStory(BeingComposite beingComposite) {

		boolean allStorysCreatedSuccessfully = true;
		List<String> storys = beingComposite.getBeingStories();

		for (String storyName : storys) {
			Story story = storyService.getStoryByName(storyName);

			if (story != null) {
				// Story exists, create BeingStory entry
				KeyBeingStory key = new KeyBeingStory(beingComposite.getBeingPK(), story.getStoryPK());
				
				if(beingStoryService.getBeingStoryById(key) == null) {
					BeingStory beingStory = new BeingStory(key);
					beingStoryService.createBeingStory(beingStory);
					allStorysCreatedSuccessfully = true;
				}
				
			} else 
				// Failed to create story, set the flag to false
				allStorysCreatedSuccessfully = false;

		}


		return allStorysCreatedSuccessfully;

	}


	private Boolean createUpdateBeingLocation(BeingComposite beingComposite) {

		boolean allLocationsCreatedSuccessfully = true;
		List<String> locations = beingComposite.getBeingLocations();

		for (String locationName : locations) {
			Location location = locationService.getLocationByName(locationName);

			if (location != null) {
				// Location exists, create BeingLocation entry
				KeyBeingLocation key = new KeyBeingLocation(beingComposite.getBeingPK(), location.getLocationPK());
				
				if(beingLocationService.getBeingLocationById(key) == null) {
					BeingLocation beingLocation = new BeingLocation(key);
					beingLocationService.createBeingLocation(beingLocation);
					allLocationsCreatedSuccessfully = true;
				}
				
			} else {

				// Failed to create location, set the flag to false
				allLocationsCreatedSuccessfully = false;

			}
		}

		return allLocationsCreatedSuccessfully;

	}
	
	@Override
	public ModelAndView setUpLinks(String entity, String entities, ModelAndView modelAndView) {
	
		
		modelAndView.addObject("entitySearchPath", "/" + entity + "/search");
		modelAndView.addObject("showAllEntityPath", "/" + entity + "/" + entities);
		modelAndView.addObject("editEntityPath", "/" + entity + "/edit");
		modelAndView.addObject("deleteEntityPath", "/" + entity + "/delete");
		modelAndView.addObject("createEntityPath", "/" + entity + "/create");
		modelAndView.addObject("checkEntityName", entity + "/check-" + entity + "-name");
		modelAndView.addObject("entityPath", "/" + entity + "/save-new");
		modelAndView.addObject("entitySaveUpdatePath", "/" + entity + "/save-edit");
		
		String entityUpper = entity.replace(entity.charAt(0), Character.toUpperCase(entity.charAt(0)));
		
		modelAndView.addObject("label", entityUpper + " Name:");
		modelAndView.addObject("entityType", entityUpper);
		
		return modelAndView;
	}

}
