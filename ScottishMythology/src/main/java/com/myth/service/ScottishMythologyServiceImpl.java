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
	
	
	//@Transactional
	@Override
	public BeingComposite createBeingRecord(BeingComposite beingComposite) {

		try {
			// Extracting Being from BeingComposite
			System.out.println(beingComposite);
			
			Being being = getBeingFromComposite(beingComposite); 
			
			System.out.println(being);
			
			// Creating a new Being
			beingService.createBeing(being);
			Being newBeing = beingService.getBeingByName(beingComposite.getBeingName());
			beingComposite.setBeingPK(newBeing.getBeingPK());
			
			System.out.println(beingComposite);
			
			// Extracting attributes from BeingComposite and creating new components
			boolean abilitiesCreated = createUpdateBeingAbility(beingComposite);
			
			boolean weaknessesCreated = createUpdateBeingWeakness(beingComposite);
		
			boolean symbolsCreated = createUpdateBeingSymbol(beingComposite);
		
			boolean storiesCreated = createUpdateBeingStory(beingComposite);
		
			boolean locationsCreated = createUpdateBeingLocation(beingComposite);
	

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

	// READ
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
	
	@Override 
	public List<BeingComposite> getBeingRecordByFaction(int factionPK){
		
		List<Being> beingList = beingService.getBeingByFaction(factionPK);
		List<BeingComposite> beingRecordList = new ArrayList<BeingComposite>();
		
		for(Being being: beingList) {
			
			BeingComposite beingComposite = convertToComposite(being);
			
			beingRecordList.add(beingComposite);	
		}
		
		return beingRecordList;
	}
	@Override 
	public List<BeingComposite> getBeingRecordByGender(int genderPK){
		
		List<Being> beingList = beingService.getBeingByFaction(genderPK);
		List<BeingComposite> beingRecordList = new ArrayList<BeingComposite>();
		
		for(Being being: beingList) {
			
			BeingComposite beingComposite = convertToComposite(being);
			
			beingRecordList.add(beingComposite);	
		}
		
		return beingRecordList;
	}
	@Override 
	public List<BeingComposite> getBeingRecordBySpecies(int speciesPK){
		
		List<Being> beingList = beingService.getBeingByFaction(speciesPK);
		List<BeingComposite> beingRecordList = new ArrayList<BeingComposite>();
		
		for(Being being: beingList) {
			
			BeingComposite beingComposite = convertToComposite(being);
			
			beingRecordList.add(beingComposite);	
		}
		
		return beingRecordList;
	}
	
	// UPDATE
	
	//@Transactional
	@Override
	public Boolean updateBeingRecord(BeingComposite beingComposite) {

		try {
			boolean abilityUpdated = false;
			boolean abilityDeleted = true;
			boolean abilityAdded = true;
			
			boolean locationsUpdated = false;
			boolean locationDeleted = true;
			boolean locationAdded = true;
			
			boolean storiesUpdated = false;
			boolean storyDeleted = true;
			boolean storyAdded = true;
			
			boolean symbolismUpdated = false;
			boolean symbolDeleted = true;
			boolean symbolAdded = true;
			
			boolean weaknessesUpdated = false;
			boolean weaknessDeleted = true;
			boolean weaknessAdded = true;

			BeingComposite oldBC = getBeingRecordById(beingComposite.getBeingPK());
			Being being = getBeingFromComposite(beingComposite);
			
			System.out.println(being);
			System.out.println(beingComposite);

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
						
						abilityDeleted = beingAbilityService.deleteBeingAbility(key);
					}
				}
				
				for(String i: beingComposite.getBeingAbilities()) {
					
					if(!oldBC.getBeingAbilities().contains(i)) {
						
						abilityAdded = createUpdateBeingAbility(beingComposite);
						break;
						
					}
				}
				abilityUpdated = abilityAdded && abilityDeleted; // Handles entries that have been added
				
			}else if(oldBC.getBeingAbilities().equals(beingComposite.getBeingAbilities())) {
				
				abilityUpdated = true;
			}
			
			if(oldBC.getBeingLocations().equals(beingComposite.getBeingLocations()) != true) {

				for(String i: oldBC.getBeingLocations()) {

					if(!beingComposite.getBeingLocations().contains(i)) {

						Location location = locationService.getLocationByName(i);
						int id = location.getLocationPK();
						KeyBeingLocation key = new KeyBeingLocation(oldBC.getBeingPK(), id);
						locationDeleted = beingLocationService.deleteBeingLocation(key);
					}
				}
				
				for(String i: beingComposite.getBeingLocations()) {
					
					if(!oldBC.getBeingLocations().contains(i)) {
						
						locationAdded = createUpdateBeingLocation(beingComposite);
						break;
						
					}
				}
				locationsUpdated = locationAdded && locationDeleted;
				
			}else if(oldBC.getBeingLocations().equals(beingComposite.getBeingLocations())) {
				
				locationsUpdated = true;
			}
			
			if(oldBC.getBeingStories().equals(beingComposite.getBeingStories()) != true) {

				for(String i: oldBC.getBeingStories()) {

					if(!beingComposite.getBeingStories().contains(i)) {

						Story story = storyService.getStoryByName(i);
						int id = story.getStoryPK();
						KeyBeingStory key = new KeyBeingStory(oldBC.getBeingPK(), id);
						storyDeleted = beingStoryService.deleteBeingStory(key);
					}
				}
				
				for(String i: beingComposite.getBeingStories()) {
					
					if(!oldBC.getBeingStories().contains(i)) {
						
						storyAdded = createUpdateBeingStory(beingComposite);
						break;
						
					}
				}
				storiesUpdated = storyAdded && storyDeleted;
				
			}else if(oldBC.getBeingStories().equals(beingComposite.getBeingStories())) {
				
				storiesUpdated = true;
			}
			
			if(oldBC.getBeingSymbolism().equals(beingComposite.getBeingSymbolism()) != true) {

				for(String i: oldBC.getBeingSymbolism()) {

					if(!beingComposite.getBeingSymbolism().contains(i)) {

						Symbol symbol = symbolService.getSymbolByName(i);
						int id = symbol.getSymbolPK();
						KeyBeingSymbolism key = new KeyBeingSymbolism(oldBC.getBeingPK(), id);
						symbolDeleted = beingSymbolismService.deleteBeingSymbolism(key);
					}
				}
				
				for(String i: beingComposite.getBeingSymbolism()) {
					
					if(!oldBC.getBeingSymbolism().contains(i)) {
						
						symbolAdded = createUpdateBeingSymbol(beingComposite);
						break;
						
					}
				}
				symbolismUpdated = symbolAdded && symbolDeleted;
				
			}else if(oldBC.getBeingSymbolism().equals(beingComposite.getBeingSymbolism())) {
				
				symbolismUpdated = true;
			}
			
			if(oldBC.getBeingWeaknesses().equals(beingComposite.getBeingWeaknesses()) != true) {

				for(String i: oldBC.getBeingWeaknesses()) {

					if(!beingComposite.getBeingWeaknesses().contains(i)) {

						Weakness weakness = weaknessService.getWeaknessByName(i);
						int id = weakness.getWeaknessPK();
						KeyBeingWeakness key = new KeyBeingWeakness(oldBC.getBeingPK(), id);
						weaknessDeleted = beingWeaknessService.deleteBeingWeakness(key);
					}
				}
				
				for(String i: beingComposite.getBeingWeaknesses()) {
					
					if(!oldBC.getBeingWeaknesses().contains(i)) {
						
						weaknessAdded = createUpdateBeingWeakness(beingComposite);
						break;
						
					}
				}
				weaknessesUpdated = weaknessAdded && weaknessDeleted;
				
			}else if(oldBC.getBeingWeaknesses().equals(beingComposite.getBeingWeaknesses())) {
				
				weaknessesUpdated = true;
			}
			
			System.out.println("Ability: " + abilityUpdated);
			System.out.println("Location: " + locationsUpdated);
			System.out.println("Story: " + storiesUpdated);
			System.out.println("Symbol: " + symbolismUpdated);
			System.out.println("Weakness: " + weaknessesUpdated);
			System.out.println("Being: " + beingUpdated);
			
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
		
		int beingSpecies = 0;
		int beingGender = 0;
		int beingFaction = 0;
		
		int beingPK = beingComposite.getBeingPK();
		System.out.println(beingComposite.getBeingPK());
		
		String beingName = beingComposite.getBeingName();
		// Species
		try {
			Species species = speciesService.getSpeciesByName(beingComposite.getBeingSpecies());
			beingSpecies = species.getSpeciesPK();
		}
		catch(Exception e) {
			System.out.println("An error occurred in the Species Service.");
		}
		
		String beingDescription = beingComposite.getBeingDescription();
		
		
		// Gender
		try {
			Gender gender = genderService.getGenderByName(beingComposite.getBeingGender());
			beingGender = gender.getGenderPK();
		}catch(Exception e) {
			
			System.out.println("An error occurred in the Gender Service.");
		}
		
		
		byte[] beingArt = null;
		
		if(beingComposite.getBeingArt() != null) {
			beingArt = beingComposite.getBeingArt();
		}
		
		// Faction
		try {
			Faction faction = factionService.getFactionByName(beingComposite.getBeingFaction());
			beingFaction = faction.getFactionPK();
		}catch(Exception e) {
			
			System.out.println("An error occurred in the Faction Service.");
		}
		
		Being newBeing = new Being(beingPK, beingName, beingSpecies, beingDescription, beingGender, beingArt, beingFaction);
		System.out.println(newBeing);
		return newBeing;


	}

	// CREATE UPDATE ///////////////////////////
	// Method used to extract abilities from BeingComposite and use them to create new entries in the AbilityBeings table

	private Boolean createUpdateBeingAbility(BeingComposite beingComposite) {

		boolean allAbilitiesCreatedSuccessfully = true;
		List<String> abilities = beingComposite.getBeingAbilities();

		for (String abilityName : abilities) {
			Ability ability = abilityService.getAbilityByName(abilityName);

			if (ability != null) {
				// Ability exists, create BeingAbility entry
				KeyBeingAbility key = new KeyBeingAbility(beingComposite.getBeingPK(), ability.getAbilityPK());
				
				if(beingAbilityService.getBeingAbilityById(key) == null && allAbilitiesCreatedSuccessfully) {
					BeingAbility beingAbility = new BeingAbility(key);
					beingAbilityService.createBeingAbility(beingAbility);
					allAbilitiesCreatedSuccessfully = true;
				}
				
				
			} else {
				allAbilitiesCreatedSuccessfully = false;
				System.out.println("There was a problem creating/updating BeingAbilities.");
			}
		}

		return allAbilitiesCreatedSuccessfully;

	}

	private Boolean createUpdateBeingWeakness(BeingComposite beingComposite) {

		boolean allWeaknesssCreatedSuccessfully = true;
		List<String> weaknesss = beingComposite.getBeingWeaknesses();

		for (String weaknessName : weaknesss) {
			Weakness weakness = weaknessService.getWeaknessByName(weaknessName);

			if (weakness != null) {
				// Weakness exists, create BeingWeakness entry
				KeyBeingWeakness key = new KeyBeingWeakness(beingComposite.getBeingPK(), weakness.getWeaknessPK());
				
				if(beingWeaknessService.getBeingWeaknessById(key) == null && allWeaknesssCreatedSuccessfully) {
					BeingWeakness beingWeakness = new BeingWeakness(key);
					beingWeaknessService.createBeingWeakness(beingWeakness);
					allWeaknesssCreatedSuccessfully = true;
				}
				
			} else {
				allWeaknesssCreatedSuccessfully = false;
				System.out.println("There was a problem creating/updating BeingWeaknesses.");
			}
		}
		return allWeaknesssCreatedSuccessfully;
	}

	private Boolean createUpdateBeingSymbol(BeingComposite beingComposite) {

		boolean allSymbolsCreatedSuccessfully = true;
		List<String> symbols = beingComposite.getBeingSymbolism();

		for (String symbolName : symbols) {
			Symbol symbol = symbolService.getSymbolByName(symbolName);



			if (symbol != null) {
				// Symbol exists, create BeingSymbolism entry
				KeyBeingSymbolism key = new KeyBeingSymbolism(beingComposite.getBeingPK(), symbol.getSymbolPK());



				if(beingSymbolismService.getBeingSymbolismById(key) == null && allSymbolsCreatedSuccessfully) {
					BeingSymbolism beingSymbolism = new BeingSymbolism(key);
					beingSymbolismService.createBeingSymbolism(beingSymbolism);
					allSymbolsCreatedSuccessfully = true;
				}
				else {
					allSymbolsCreatedSuccessfully = false;
					System.out.println("There was a problem creating/updating BeingSymbolism.");
				}

			}
		}

		System.out.println(allSymbolsCreatedSuccessfully);
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
				
				if(beingStoryService.getBeingStoryById(key) == null && allStorysCreatedSuccessfully) {
					BeingStory beingStory = new BeingStory(key);
					beingStoryService.createBeingStory(beingStory);
					if(beingStoryService.getBeingStoryById(key) != null)
						allStorysCreatedSuccessfully = true;
					}
				else {
					allStorysCreatedSuccessfully = false;
					System.out.println("There was a problem creating/updating BeingStories.");
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
				
				if(beingLocationService.getBeingLocationById(key) == null && allLocationsCreatedSuccessfully) {
					BeingLocation beingLocation = new BeingLocation(key);
					beingLocationService.createBeingLocation(beingLocation);
					allLocationsCreatedSuccessfully = true;
				}
				
			} else {

				// Failed to create location, set the flag to false
				allLocationsCreatedSuccessfully = false;
				System.out.println("There was a problem creating/updating BeingLocations.");

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
		
		String entityUpper = Character.toUpperCase(entity.charAt(0)) + entity.substring(1);
		
		modelAndView.addObject("label", entityUpper + " Name:");
		modelAndView.addObject("entityType", entityUpper);
		
		return modelAndView;
	}

}
