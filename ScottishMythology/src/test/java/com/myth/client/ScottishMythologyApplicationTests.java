package com.myth.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

import com.myth.dao.BeingDao;
import com.myth.entity.Ability;
import com.myth.entity.Being;
import com.myth.entity.Faction;
import com.myth.entity.Gender;
import com.myth.entity.Location;
import com.myth.entity.Species;
import com.myth.entity.Story;
import com.myth.entity.Symbol;
import com.myth.entity.Weakness;
import com.myth.entity.composite.BeingComposite;
import com.myth.entity.junction.*;
import com.myth.service.BeingService;
import com.myth.service.ScottishMythologyService;
import com.myth.service.ScottishMythologyServiceImpl;
import com.myth.service.junction.BeingAbilityService;
import com.myth.service.junction.BeingLocationService;
import com.myth.service.junction.BeingStoryService;
import com.myth.service.junction.BeingSymbolismService;
import com.myth.service.junction.BeingWeaknessService;
import com.myth.service.micro.AbilityService;
import com.myth.service.micro.FactionService;
import com.myth.service.micro.GenderService;
import com.myth.service.micro.LocationService;
import com.myth.service.micro.SpeciesService;
import com.myth.service.micro.StoryService;
import com.myth.service.micro.SymbolService;
import com.myth.service.micro.WeaknessService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.atLeastOnce;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@AutoConfigureMockMvc
class ScottishMythologyApplicationTests {

	@MockBean
	BeingService beingService;
	@MockBean
	SpeciesService speciesService;
	@MockBean
	GenderService genderService;
	@MockBean
	FactionService factionService;
	@MockBean
	AbilityService abilityService;
	@MockBean
	WeaknessService weaknessService;
	@MockBean
	LocationService locationService;
	@MockBean
	SymbolService symbolService;
	@MockBean
	StoryService storyService;
	@MockBean
	BeingAbilityService beingAbilityService;
	@MockBean
	BeingLocationService beingLocationService;
	@MockBean
	BeingStoryService beingStoryService;
	@MockBean
	BeingSymbolismService beingSymbolismService;
	@MockBean
	BeingWeaknessService beingWeaknessService;
	@MockBean
	BeingDao beingDao;

	@Autowired 
	ScottishMythologyServiceImpl scotMythService;

	@Test
	void contextLoads() {
	}

	Being being = new Being(1, "Being Name", 1, "Being Description", 1, null, 1);
	Being being2 = new Being(2, "Being Name 2", 1, "Being Description 2", 1, null, 1);

	Faction faction = new Faction(1, "Faction", "Faction Description");
	Species species = new Species(1, "Species", "Species Description");
	Gender gender = new Gender(1, "Gender");

	Ability ability = new Ability(1, "Ability 1");
	Ability ability2 = new Ability(2, "Ability 2");
	KeyBeingAbility kBA1 = new KeyBeingAbility(1, 1);
	KeyBeingAbility kBA2 = new KeyBeingAbility(1, 2);
	BeingAbility bA1 = new BeingAbility(kBA1);
	BeingAbility bA2 = new BeingAbility(kBA2);
	List<BeingAbility> bAbilities = new ArrayList<>(Arrays.asList(bA1, bA2));

	Weakness weakness = new Weakness(1, "Weakness 1");
	Weakness weakness2 = new Weakness(2, "Weakness 2");
	KeyBeingWeakness kBW1 = new KeyBeingWeakness(1, 1);
	KeyBeingWeakness kBW2 = new KeyBeingWeakness(1, 2);
	BeingWeakness bW1 = new BeingWeakness(kBW1);
	BeingWeakness bW2 = new BeingWeakness(kBW2);
	List<BeingWeakness> bWeaknesses = new ArrayList<>(Arrays.asList(bW1, bW2));

	Symbol symbol = new Symbol(1, "Symbol 1");
	Symbol symbol2 = new Symbol(2, "Symbol 2");
	KeyBeingSymbolism kBSy1 = new KeyBeingSymbolism(1, 1);
	KeyBeingSymbolism kBSy2 = new KeyBeingSymbolism(1, 2);
	BeingSymbolism bSy1 = new BeingSymbolism(kBSy1);
	BeingSymbolism bSy2 = new BeingSymbolism(kBSy2);
	List<BeingSymbolism> bSymbols = new ArrayList<>(Arrays.asList(bSy1, bSy2));

	Story story = new Story(1, "Story 1", "Story Description 1");
	Story story2 = new Story(2, "Story 2", "Story Description 2");
	KeyBeingStory kBSt1 = new KeyBeingStory(1, 1);
	KeyBeingStory kBSt2 = new KeyBeingStory(1, 2);
	BeingStory bSt1 = new BeingStory(kBSt1);
	BeingStory bSt2 = new BeingStory(kBSt2);
	List<BeingStory> bStories = new ArrayList<>(Arrays.asList(bSt1, bSt2));

	Location location = new Location(1, "Location 1", "Location Description 1");
	Location location2 = new Location(2, "Location 2", "Location Description 2");
	KeyBeingLocation kBL1 = new KeyBeingLocation(1, 1);
	KeyBeingLocation kBL2 = new KeyBeingLocation(1, 2);
	BeingLocation bL1 = new BeingLocation(kBL1);
	BeingLocation bL2 = new BeingLocation(kBL2);
	List<BeingLocation> bLocations = new ArrayList<>(Arrays.asList(bL1, bL2));

	List<String> abilities = new ArrayList<>(Arrays.asList("Ability 1", "Ability 2"));
	List<String> weaknesses = new ArrayList<>(Arrays.asList("Weakness 1", "Weakness 2"));
	List<String> symbols = new ArrayList<>(Arrays.asList("Symbol 1", "Symbol 2"));
	List<String> stories = new ArrayList<>(Arrays.asList("Story 1", "Story 2"));
	List<String> locations = new ArrayList<>(Arrays.asList("Location 1", "Location 2"));

	BeingComposite beingComp = new BeingComposite(being, species.getSpeciesName(), gender.getGenderType(), faction.getFactionName(), abilities, weaknesses, symbols, stories, locations);



	@Test
	@DisplayName("Test setting up links")
	void testSetUpLinks() {

		String entityType = "entity";
		String entitiesType = "entities";
		ModelAndView mav = new ModelAndView();

		mav = scotMythService.setUpLinks(entityType, entitiesType, mav);

		Assertions.assertEquals("/entity/search", mav.getModel().get("entitySearchPath"));
		Assertions.assertEquals("/entity/entities", mav.getModel().get("showAllEntityPath"));
		Assertions.assertEquals("/entity/edit", mav.getModel().get("editEntityPath"));
		Assertions.assertEquals("/entity/delete", mav.getModel().get("deleteEntityPath"));
		Assertions.assertEquals("/entity/create", mav.getModel().get("createEntityPath"));
		Assertions.assertEquals("entity/check-entity-name", mav.getModel().get("checkEntityName"));
		Assertions.assertEquals("/entity/save-new", mav.getModel().get("entityPath"));
		Assertions.assertEquals("/entity/save-edit", mav.getModel().get("entitySaveUpdatePath"));

	}



	// CREATE

	// This test tests createBeingRecord, which calls private methods convertToComposite, getBeingFromComposite, and all createUpdate methods
	// Therefore the private methods are implicitly tested within this method.
	@Test
	@DisplayName("Test createBeingRecord success")
	public void testSuccessfulCreation() {

		// Mock service calls
		when(beingService.createBeing(being)).thenReturn(being);

		// Calls occur during ConvertToComposite which occurs during getBeingRecordByName
		when(beingService.getBeingByName(being.getBeingName())).thenReturn(being);
		when(speciesService.getSpeciesById(being.getBeingSpecies())).thenReturn(species);
		when(genderService.getGenderById(being.getBeingGender())).thenReturn(gender);
		when(factionService.getFactionById(being.getBeingFaction())).thenReturn(faction);

		when(beingAbilityService.getBeingAbilityByBeingId(being.getBeingPK())).thenReturn(bAbilities);
		when(beingWeaknessService.getBeingWeaknessByBeingId(being.getBeingPK())).thenReturn(bWeaknesses);
		when(beingStoryService.getBeingStoryByBeingId(being.getBeingPK())).thenReturn(bStories);
		when(beingSymbolismService.getBeingSymbolismByBeingId(being.getBeingPK())).thenReturn(bSymbols);
		when(beingLocationService.getBeingLocationByBeingId(being.getBeingPK())).thenReturn(bLocations);

		when(abilityService.getAbilityById(ability.getAbilityPK())).thenReturn(ability);
		when(abilityService.getAbilityById(ability2.getAbilityPK())).thenReturn(ability2);
		when(weaknessService.getWeaknessById(weakness.getWeaknessPK())).thenReturn(weakness);
		when(weaknessService.getWeaknessById(weakness2.getWeaknessPK())).thenReturn(weakness2);
		when(storyService.getStoryById(story.getStoryPK())).thenReturn(story);
		when(storyService.getStoryById(story2.getStoryPK())).thenReturn(story2);
		when(symbolService.getSymbolById(symbol.getSymbolPK())).thenReturn(symbol);
		when(symbolService.getSymbolById(symbol2.getSymbolPK())).thenReturn(symbol2);
		when(locationService.getLocationById(location.getLocationPK())).thenReturn(location);
		when(locationService.getLocationById(location2.getLocationPK())).thenReturn(location2);

		// Calls occur during getBeingFromComposite
		when(factionService.getFactionByName("Faction")).thenReturn(faction);
		when(genderService.getGenderByName("Gender")).thenReturn(gender);
		when(speciesService.getSpeciesByName("Species")).thenReturn(species);

		// Calls occur during createUpdate methods
		when(beingService.getBeingByName("Being Name")).thenReturn(being);
		when(abilityService.getAbilityByName("Ability 1")).thenReturn(ability);
		when(abilityService.getAbilityByName("Ability 2")).thenReturn(ability2);
		when(beingAbilityService.getBeingAbilityById(any())).thenReturn(null);
		when(beingAbilityService.createBeingAbility(bA1)).thenReturn(bA1);
		when(beingAbilityService.createBeingAbility(bA2)).thenReturn(bA2);


		when(weaknessService.getWeaknessByName("Weakness 1")).thenReturn(weakness);
		when(weaknessService.getWeaknessByName("Weakness 2")).thenReturn(weakness2);
		when(beingWeaknessService.getBeingWeaknessById(any())).thenReturn(null);
		when(beingWeaknessService.createBeingWeakness(bW1)).thenReturn(bW1);
		when(beingWeaknessService.createBeingWeakness(bW2)).thenReturn(bW2);



		when(storyService.getStoryByName("Story 1")).thenReturn(story);
		when(storyService.getStoryByName("Story 2")).thenReturn(story2);
		when(beingStoryService.getBeingStoryById(any())).thenReturn(null);
		when(beingStoryService.createBeingStory(bSt1)).thenReturn(bSt1);
		when(beingStoryService.createBeingStory(bSt2)).thenReturn(bSt2);



		when(symbolService.getSymbolByName("Symbol 1")).thenReturn(symbol);
		when(symbolService.getSymbolByName("Symbol 2")).thenReturn(symbol2);
		when(beingSymbolismService.getBeingSymbolismById(any())).thenReturn(null);
		when(beingSymbolismService.createBeingSymbolism(bSy1)).thenReturn(bSy1);
		when(beingSymbolismService.createBeingSymbolism(bSy2)).thenReturn(bSy2);



		when(locationService.getLocationByName("Location 1")).thenReturn(location);
		when(locationService.getLocationByName("Location 2")).thenReturn(location2);
		when(beingLocationService.getBeingLocationById(any())).thenReturn(null);
		when(beingLocationService.createBeingLocation(bL1)).thenReturn(bL1);
		when(beingLocationService.createBeingLocation(bL2)).thenReturn(bL2);


		// Call the method to test
		BeingComposite result = scotMythService.createBeingRecord(beingComp);

		// Assertions and verifications
		Assertions.assertNotNull(result);
		verify(beingService).createBeing(any());
		verify(beingService, times(2)).getBeingByName(anyString());
		verify(abilityService, atLeastOnce()).getAbilityByName(anyString());
		verify(beingAbilityService, atLeastOnce()).getBeingAbilityById(any());
	}


	@Test
	@DisplayName("Test createBeingRecord failure")
	public void testFailedCreation() {

		// Mock service calls
		when(beingService.createBeing(being)).thenReturn(being);

		// Calls occur during ConvertToComposite which occurs during getBeingRecordByName
		when(beingService.getBeingByName(being.getBeingName())).thenReturn(being);
		when(speciesService.getSpeciesById(being.getBeingSpecies())).thenReturn(species);
		when(genderService.getGenderById(being.getBeingGender())).thenReturn(gender);
		when(factionService.getFactionById(being.getBeingFaction())).thenReturn(faction);

		when(beingAbilityService.getBeingAbilityByBeingId(being.getBeingPK())).thenReturn(bAbilities);
		when(beingWeaknessService.getBeingWeaknessByBeingId(being.getBeingPK())).thenReturn(bWeaknesses);
		when(beingStoryService.getBeingStoryByBeingId(being.getBeingPK())).thenReturn(bStories);
		when(beingSymbolismService.getBeingSymbolismByBeingId(being.getBeingPK())).thenReturn(bSymbols);
		when(beingLocationService.getBeingLocationByBeingId(being.getBeingPK())).thenReturn(bLocations);

		when(abilityService.getAbilityById(ability.getAbilityPK())).thenReturn(ability);
		when(abilityService.getAbilityById(ability2.getAbilityPK())).thenReturn(ability2);
		when(weaknessService.getWeaknessById(weakness.getWeaknessPK())).thenReturn(weakness);
		when(weaknessService.getWeaknessById(weakness2.getWeaknessPK())).thenReturn(weakness2);
		when(storyService.getStoryById(story.getStoryPK())).thenReturn(story);
		when(storyService.getStoryById(story2.getStoryPK())).thenReturn(story2);
		when(symbolService.getSymbolById(symbol.getSymbolPK())).thenReturn(symbol);
		when(symbolService.getSymbolById(symbol2.getSymbolPK())).thenReturn(symbol2);
		when(locationService.getLocationById(location.getLocationPK())).thenReturn(location);
		when(locationService.getLocationById(location2.getLocationPK())).thenReturn(location2);

		// Calls occur during getBeingFromComposite
		when(factionService.getFactionByName("Faction")).thenReturn(faction);
		when(genderService.getGenderByName("Gender")).thenReturn(gender);
		when(speciesService.getSpeciesByName("Species")).thenReturn(species);

		// Calls occur during createUpdate methods
		when(beingService.getBeingByName("Being Name")).thenReturn(being);
		when(abilityService.getAbilityByName("Ability 1")).thenReturn(ability);
		when(abilityService.getAbilityByName("Ability 2")).thenReturn(ability2);
		when(beingAbilityService.getBeingAbilityById(any())).thenReturn(null);
		when(beingAbilityService.createBeingAbility(bA1)).thenReturn(bA1);
		when(beingAbilityService.createBeingAbility(bA2)).thenReturn(bA2);


		when(weaknessService.getWeaknessByName("Weakness 1")).thenReturn(weakness);
		when(weaknessService.getWeaknessByName("Weakness 2")).thenReturn(weakness2);
		when(beingWeaknessService.getBeingWeaknessById(any())).thenReturn(null);
		when(beingWeaknessService.createBeingWeakness(bW1)).thenReturn(bW1);
		when(beingWeaknessService.createBeingWeakness(bW2)).thenReturn(bW2);



		when(storyService.getStoryByName("Story 1")).thenReturn(story);
		when(storyService.getStoryByName("Story 2")).thenReturn(story2);
		when(beingStoryService.getBeingStoryById(any())).thenReturn(null);
		when(beingStoryService.createBeingStory(bSt1)).thenReturn(bSt1);
		when(beingStoryService.createBeingStory(bSt2)).thenReturn(bSt2);



		when(symbolService.getSymbolByName("Symbol 1")).thenReturn(symbol);
		when(symbolService.getSymbolByName("Symbol 2")).thenReturn(symbol2);
		when(beingSymbolismService.getBeingSymbolismById(any())).thenReturn(null);
		when(beingSymbolismService.createBeingSymbolism(bSy1)).thenReturn(bSy1);
		when(beingSymbolismService.createBeingSymbolism(bSy2)).thenReturn(bSy2);



		when(locationService.getLocationByName("Location 1")).thenReturn(location);
		when(locationService.getLocationByName("Location 2")).thenReturn(location2);
		when(beingLocationService.getBeingLocationById(any())).thenReturn(null);
		when(beingLocationService.createBeingLocation(bL1)).thenReturn(null);
		when(beingLocationService.createBeingLocation(bL2)).thenReturn(null);


		// Call the method to test
		BeingComposite result = scotMythService.createBeingRecord(beingComp);

		// Assertions and verifications
		Assertions.assertNull(result);
		verify(beingService).createBeing(any());
		verify(beingService).getBeingByName(anyString());
		verify(abilityService, atLeastOnce()).getAbilityByName(anyString());
		verify(beingAbilityService, atLeastOnce()).getBeingAbilityById(any());
	}

	// READ
	@Test
	@DisplayName("Test getAllBeingRecords() success")
	public void testGetAll(){

		BeingComposite beingComp2 = new BeingComposite(being2, species.getSpeciesName(), gender.getGenderType(), faction.getFactionName(), abilities, weaknesses, symbols, stories, locations);
		List<BeingComposite> expectedBeingCompResult = Arrays.asList(beingComp, beingComp2);

		doReturn(Arrays.asList(being, being2)).when(beingService).getAllBeing();

		when(beingService.getBeingByName(being.getBeingName())).thenReturn(being);
		when(speciesService.getSpeciesById(being.getBeingSpecies())).thenReturn(species);
		when(genderService.getGenderById(being.getBeingGender())).thenReturn(gender);
		when(factionService.getFactionById(being.getBeingFaction())).thenReturn(faction);

		when(beingService.getBeingByName(being2.getBeingName())).thenReturn(being2);
		when(speciesService.getSpeciesById(being2.getBeingSpecies())).thenReturn(species);
		when(genderService.getGenderById(being2.getBeingGender())).thenReturn(gender);
		when(factionService.getFactionById(being2.getBeingFaction())).thenReturn(faction);

		when(beingAbilityService.getBeingAbilityByBeingId(being.getBeingPK())).thenReturn(bAbilities);
		when(beingWeaknessService.getBeingWeaknessByBeingId(being.getBeingPK())).thenReturn(bWeaknesses);
		when(beingStoryService.getBeingStoryByBeingId(being.getBeingPK())).thenReturn(bStories);
		when(beingSymbolismService.getBeingSymbolismByBeingId(being.getBeingPK())).thenReturn(bSymbols);
		when(beingLocationService.getBeingLocationByBeingId(being.getBeingPK())).thenReturn(bLocations);

		when(beingAbilityService.getBeingAbilityByBeingId(being2.getBeingPK())).thenReturn(bAbilities);
		when(beingWeaknessService.getBeingWeaknessByBeingId(being2.getBeingPK())).thenReturn(bWeaknesses);
		when(beingStoryService.getBeingStoryByBeingId(being2.getBeingPK())).thenReturn(bStories);
		when(beingSymbolismService.getBeingSymbolismByBeingId(being2.getBeingPK())).thenReturn(bSymbols);
		when(beingLocationService.getBeingLocationByBeingId(being2.getBeingPK())).thenReturn(bLocations);

		when(abilityService.getAbilityById(ability.getAbilityPK())).thenReturn(ability);
		when(abilityService.getAbilityById(ability2.getAbilityPK())).thenReturn(ability2);
		when(weaknessService.getWeaknessById(weakness.getWeaknessPK())).thenReturn(weakness);
		when(weaknessService.getWeaknessById(weakness2.getWeaknessPK())).thenReturn(weakness2);
		when(storyService.getStoryById(story.getStoryPK())).thenReturn(story);
		when(storyService.getStoryById(story2.getStoryPK())).thenReturn(story2);
		when(symbolService.getSymbolById(symbol.getSymbolPK())).thenReturn(symbol);
		when(symbolService.getSymbolById(symbol2.getSymbolPK())).thenReturn(symbol2);
		when(locationService.getLocationById(location.getLocationPK())).thenReturn(location);
		when(locationService.getLocationById(location2.getLocationPK())).thenReturn(location2);


		List<BeingComposite> beingRecords = scotMythService.getAllBeingRecords();
		Assertions.assertEquals(2, beingRecords.size(), "getAllBeingRecords should return 2 being records");
		for(int i =0; i< expectedBeingCompResult.size(); i++) {

			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingPK(), beingRecords.get(i).getBeingPK(), "Check being" + i + "PK"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingSpecies(), beingRecords.get(i).getBeingSpecies(), "Check being " + i + " Species Name"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingSpeciesPK(), beingRecords.get(i).getBeingSpeciesPK(), "Check being " + i + " Species PK"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingGender(), beingRecords.get(i).getBeingGender(), "Check being " + i + " Gender Type"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingGenderPK(), beingRecords.get(i).getBeingGenderPK(), "Check being " + i + " Gender PK");
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingFaction(), beingRecords.get(i).getBeingFaction(), "Check being " + i + " Faction Name"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingFactionPK(), beingRecords.get(i).getBeingFactionPK(), "Check being " + i + " Faction PK"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingAbilities(), beingRecords.get(i).getBeingAbilities(), "Check being " + i + " Abilities"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingWeaknesses(), beingRecords.get(i).getBeingWeaknesses(), "Check being " + i + " Weaknesses"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingSymbolism(), beingRecords.get(i).getBeingSymbolism(), "Check being " + i + " Symbolism"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingStories(), beingRecords.get(i).getBeingStories(), "Check being " + i + " Stories"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingLocations(), beingRecords.get(i).getBeingLocations(), "Check being " + i + " Locations"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingName(), beingRecords.get(i).getBeingName(), "Check being " + i + " Name"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingDescription(), beingRecords.get(i).getBeingDescription(), "Check being " + i + " Description"); 

		}
	}


	@Test
	public void testGetById() {

		when(beingService.getBeingById(being.getBeingPK())).thenReturn(being);

		when(beingService.getBeingByName(being.getBeingName())).thenReturn(being);
		when(speciesService.getSpeciesById(being.getBeingSpecies())).thenReturn(species);
		when(genderService.getGenderById(being.getBeingGender())).thenReturn(gender);
		when(factionService.getFactionById(being.getBeingFaction())).thenReturn(faction);

		when(beingAbilityService.getBeingAbilityByBeingId(being.getBeingPK())).thenReturn(bAbilities);
		when(beingWeaknessService.getBeingWeaknessByBeingId(being.getBeingPK())).thenReturn(bWeaknesses);
		when(beingStoryService.getBeingStoryByBeingId(being.getBeingPK())).thenReturn(bStories);
		when(beingSymbolismService.getBeingSymbolismByBeingId(being.getBeingPK())).thenReturn(bSymbols);
		when(beingLocationService.getBeingLocationByBeingId(being.getBeingPK())).thenReturn(bLocations);

		when(abilityService.getAbilityById(ability.getAbilityPK())).thenReturn(ability);
		when(abilityService.getAbilityById(ability2.getAbilityPK())).thenReturn(ability2);

		when(weaknessService.getWeaknessById(weakness.getWeaknessPK())).thenReturn(weakness);
		when(weaknessService.getWeaknessById(weakness2.getWeaknessPK())).thenReturn(weakness2);

		when(storyService.getStoryById(story.getStoryPK())).thenReturn(story);
		when(storyService.getStoryById(story2.getStoryPK())).thenReturn(story2);

		when(symbolService.getSymbolById(symbol.getSymbolPK())).thenReturn(symbol);
		when(symbolService.getSymbolById(symbol2.getSymbolPK())).thenReturn(symbol2);

		when(locationService.getLocationById(location.getLocationPK())).thenReturn(location);
		when(locationService.getLocationById(location2.getLocationPK())).thenReturn(location2);


		BeingComposite returnedBeing = scotMythService.getBeingRecordById(being.getBeingPK());

		Assertions.assertEquals(beingComp.getBeingPK(), returnedBeing.getBeingPK(), "Check beingPK"); 
		Assertions.assertEquals(beingComp.getBeingSpecies(), returnedBeing.getBeingSpecies(), "Check being Species Name"); 
		Assertions.assertEquals(beingComp.getBeingSpeciesPK(), returnedBeing.getBeingSpeciesPK(), "Check being Species PK"); 
		Assertions.assertEquals(beingComp.getBeingGender(), returnedBeing.getBeingGender(), "Check being Gender Type"); 
		Assertions.assertEquals(beingComp.getBeingGenderPK(), returnedBeing.getBeingGenderPK(), "Check being Gender PK");
		Assertions.assertEquals(beingComp.getBeingFaction(), returnedBeing.getBeingFaction(), "Check being Faction Name"); 
		Assertions.assertEquals(beingComp.getBeingFactionPK(), returnedBeing.getBeingFactionPK(), "Check being Faction PK"); 
		Assertions.assertEquals(beingComp.getBeingAbilities(), returnedBeing.getBeingAbilities(), "Check being Abilities"); 
		Assertions.assertEquals(beingComp.getBeingWeaknesses(), returnedBeing.getBeingWeaknesses(), "Check being Weaknesses"); 
		Assertions.assertEquals(beingComp.getBeingSymbolism(), returnedBeing.getBeingSymbolism(), "Check being Symbolism"); 
		Assertions.assertEquals(beingComp.getBeingStories(), returnedBeing.getBeingStories(), "Check being Stories"); 
		Assertions.assertEquals(beingComp.getBeingLocations(), returnedBeing.getBeingLocations(), "Check being Locations"); 
		Assertions.assertEquals(beingComp.getBeingName(), returnedBeing.getBeingName(), "Check being Name"); 
		Assertions.assertEquals(beingComp.getBeingDescription(), returnedBeing.getBeingDescription(), "Check being Description"); 


	}

	@Test
	public void testGetByName() {

		when(beingService.getBeingById(being.getBeingPK())).thenReturn(being);

		when(beingService.getBeingByName(being.getBeingName())).thenReturn(being);
		when(speciesService.getSpeciesById(being.getBeingSpecies())).thenReturn(species);
		when(genderService.getGenderById(being.getBeingGender())).thenReturn(gender);
		when(factionService.getFactionById(being.getBeingFaction())).thenReturn(faction);

		when(beingAbilityService.getBeingAbilityByBeingId(being.getBeingPK())).thenReturn(bAbilities);
		when(beingWeaknessService.getBeingWeaknessByBeingId(being.getBeingPK())).thenReturn(bWeaknesses);
		when(beingStoryService.getBeingStoryByBeingId(being.getBeingPK())).thenReturn(bStories);
		when(beingSymbolismService.getBeingSymbolismByBeingId(being.getBeingPK())).thenReturn(bSymbols);
		when(beingLocationService.getBeingLocationByBeingId(being.getBeingPK())).thenReturn(bLocations);

		when(abilityService.getAbilityById(ability.getAbilityPK())).thenReturn(ability);
		when(abilityService.getAbilityById(ability2.getAbilityPK())).thenReturn(ability2);

		when(weaknessService.getWeaknessById(weakness.getWeaknessPK())).thenReturn(weakness);
		when(weaknessService.getWeaknessById(weakness2.getWeaknessPK())).thenReturn(weakness2);

		when(storyService.getStoryById(story.getStoryPK())).thenReturn(story);
		when(storyService.getStoryById(story2.getStoryPK())).thenReturn(story2);

		when(symbolService.getSymbolById(symbol.getSymbolPK())).thenReturn(symbol);
		when(symbolService.getSymbolById(symbol2.getSymbolPK())).thenReturn(symbol2);

		when(locationService.getLocationById(location.getLocationPK())).thenReturn(location);
		when(locationService.getLocationById(location2.getLocationPK())).thenReturn(location2);


		BeingComposite returnedBeing = scotMythService.getBeingRecordByName(being.getBeingName());

		Assertions.assertEquals(beingComp.getBeingPK(), returnedBeing.getBeingPK(), "Check beingPK"); 
		Assertions.assertEquals(beingComp.getBeingSpecies(), returnedBeing.getBeingSpecies(), "Check being Species Name"); 
		Assertions.assertEquals(beingComp.getBeingSpeciesPK(), returnedBeing.getBeingSpeciesPK(), "Check being Species PK"); 
		Assertions.assertEquals(beingComp.getBeingGender(), returnedBeing.getBeingGender(), "Check being Gender Type"); 
		Assertions.assertEquals(beingComp.getBeingGenderPK(), returnedBeing.getBeingGenderPK(), "Check being Gender PK");
		Assertions.assertEquals(beingComp.getBeingFaction(), returnedBeing.getBeingFaction(), "Check being Faction Name"); 
		Assertions.assertEquals(beingComp.getBeingFactionPK(), returnedBeing.getBeingFactionPK(), "Check being Faction PK"); 
		Assertions.assertEquals(beingComp.getBeingAbilities(), returnedBeing.getBeingAbilities(), "Check being Abilities"); 
		Assertions.assertEquals(beingComp.getBeingWeaknesses(), returnedBeing.getBeingWeaknesses(), "Check being Weaknesses"); 
		Assertions.assertEquals(beingComp.getBeingSymbolism(), returnedBeing.getBeingSymbolism(), "Check being Symbolism"); 
		Assertions.assertEquals(beingComp.getBeingStories(), returnedBeing.getBeingStories(), "Check being Stories"); 
		Assertions.assertEquals(beingComp.getBeingLocations(), returnedBeing.getBeingLocations(), "Check being Locations"); 
		Assertions.assertEquals(beingComp.getBeingName(), returnedBeing.getBeingName(), "Check being Name"); 
		Assertions.assertEquals(beingComp.getBeingDescription(), returnedBeing.getBeingDescription(), "Check being Description"); 


	}

	@Test
	public void testGetByFaction() {

		BeingComposite beingComp2 = new BeingComposite(being2, species.getSpeciesName(), gender.getGenderType(), faction.getFactionName(), abilities, weaknesses, symbols, stories, locations);
		List<BeingComposite> expectedBeingCompResult = Arrays.asList(beingComp, beingComp2);

		doReturn(Arrays.asList(being, being2)).when(beingService).getBeingByFaction(being.getBeingFaction());

		when(beingService.getBeingByName(being.getBeingName())).thenReturn(being);
		when(speciesService.getSpeciesById(being.getBeingSpecies())).thenReturn(species);
		when(genderService.getGenderById(being.getBeingGender())).thenReturn(gender);
		when(factionService.getFactionById(being.getBeingFaction())).thenReturn(faction);

		when(beingService.getBeingByName(being2.getBeingName())).thenReturn(being2);
		when(speciesService.getSpeciesById(being2.getBeingSpecies())).thenReturn(species);
		when(genderService.getGenderById(being2.getBeingGender())).thenReturn(gender);
		when(factionService.getFactionById(being2.getBeingFaction())).thenReturn(faction);

		when(beingAbilityService.getBeingAbilityByBeingId(being.getBeingPK())).thenReturn(bAbilities);
		when(beingWeaknessService.getBeingWeaknessByBeingId(being.getBeingPK())).thenReturn(bWeaknesses);
		when(beingStoryService.getBeingStoryByBeingId(being.getBeingPK())).thenReturn(bStories);
		when(beingSymbolismService.getBeingSymbolismByBeingId(being.getBeingPK())).thenReturn(bSymbols);
		when(beingLocationService.getBeingLocationByBeingId(being.getBeingPK())).thenReturn(bLocations);

		when(beingAbilityService.getBeingAbilityByBeingId(being2.getBeingPK())).thenReturn(bAbilities);
		when(beingWeaknessService.getBeingWeaknessByBeingId(being2.getBeingPK())).thenReturn(bWeaknesses);
		when(beingStoryService.getBeingStoryByBeingId(being2.getBeingPK())).thenReturn(bStories);
		when(beingSymbolismService.getBeingSymbolismByBeingId(being2.getBeingPK())).thenReturn(bSymbols);
		when(beingLocationService.getBeingLocationByBeingId(being2.getBeingPK())).thenReturn(bLocations);

		when(abilityService.getAbilityById(ability.getAbilityPK())).thenReturn(ability);
		when(abilityService.getAbilityById(ability2.getAbilityPK())).thenReturn(ability2);
		when(weaknessService.getWeaknessById(weakness.getWeaknessPK())).thenReturn(weakness);
		when(weaknessService.getWeaknessById(weakness2.getWeaknessPK())).thenReturn(weakness2);
		when(storyService.getStoryById(story.getStoryPK())).thenReturn(story);
		when(storyService.getStoryById(story2.getStoryPK())).thenReturn(story2);
		when(symbolService.getSymbolById(symbol.getSymbolPK())).thenReturn(symbol);
		when(symbolService.getSymbolById(symbol2.getSymbolPK())).thenReturn(symbol2);
		when(locationService.getLocationById(location.getLocationPK())).thenReturn(location);
		when(locationService.getLocationById(location2.getLocationPK())).thenReturn(location2);


		List<BeingComposite> beingRecords = scotMythService.getBeingRecordByFaction(being.getBeingFaction());
		Assertions.assertEquals(2, beingRecords.size(), "getBeingByFaction should return 2 being records");
		for(int i =0; i< expectedBeingCompResult.size(); i++) {

			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingPK(), beingRecords.get(i).getBeingPK(), "Check being" + i + "PK"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingSpecies(), beingRecords.get(i).getBeingSpecies(), "Check being " + i + " Species Name"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingSpeciesPK(), beingRecords.get(i).getBeingSpeciesPK(), "Check being " + i + " Species PK"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingGender(), beingRecords.get(i).getBeingGender(), "Check being " + i + " Gender Type"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingGenderPK(), beingRecords.get(i).getBeingGenderPK(), "Check being " + i + " Gender PK");
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingFaction(), beingRecords.get(i).getBeingFaction(), "Check being " + i + " Faction Name"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingFactionPK(), beingRecords.get(i).getBeingFactionPK(), "Check being " + i + " Faction PK"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingAbilities(), beingRecords.get(i).getBeingAbilities(), "Check being " + i + " Abilities"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingWeaknesses(), beingRecords.get(i).getBeingWeaknesses(), "Check being " + i + " Weaknesses"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingSymbolism(), beingRecords.get(i).getBeingSymbolism(), "Check being " + i + " Symbolism"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingStories(), beingRecords.get(i).getBeingStories(), "Check being " + i + " Stories"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingLocations(), beingRecords.get(i).getBeingLocations(), "Check being " + i + " Locations"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingName(), beingRecords.get(i).getBeingName(), "Check being " + i + " Name"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingDescription(), beingRecords.get(i).getBeingDescription(), "Check being " + i + " Description"); 
		}
	}

	@Test
	public void testGetByGender() {

		BeingComposite beingComp2 = new BeingComposite(being2, species.getSpeciesName(), gender.getGenderType(), faction.getFactionName(), abilities, weaknesses, symbols, stories, locations);
		List<BeingComposite> expectedBeingCompResult = Arrays.asList(beingComp, beingComp2);

		doReturn(Arrays.asList(being, being2)).when(beingService).getBeingByGender(being.getBeingGender());

		when(beingService.getBeingByName(being.getBeingName())).thenReturn(being);
		when(speciesService.getSpeciesById(being.getBeingSpecies())).thenReturn(species);
		when(genderService.getGenderById(being.getBeingGender())).thenReturn(gender);
		when(factionService.getFactionById(being.getBeingFaction())).thenReturn(faction);

		when(beingService.getBeingByName(being2.getBeingName())).thenReturn(being2);
		when(speciesService.getSpeciesById(being2.getBeingSpecies())).thenReturn(species);
		when(genderService.getGenderById(being2.getBeingGender())).thenReturn(gender);
		when(factionService.getFactionById(being2.getBeingFaction())).thenReturn(faction);

		when(beingAbilityService.getBeingAbilityByBeingId(being.getBeingPK())).thenReturn(bAbilities);
		when(beingWeaknessService.getBeingWeaknessByBeingId(being.getBeingPK())).thenReturn(bWeaknesses);
		when(beingStoryService.getBeingStoryByBeingId(being.getBeingPK())).thenReturn(bStories);
		when(beingSymbolismService.getBeingSymbolismByBeingId(being.getBeingPK())).thenReturn(bSymbols);
		when(beingLocationService.getBeingLocationByBeingId(being.getBeingPK())).thenReturn(bLocations);

		when(beingAbilityService.getBeingAbilityByBeingId(being2.getBeingPK())).thenReturn(bAbilities);
		when(beingWeaknessService.getBeingWeaknessByBeingId(being2.getBeingPK())).thenReturn(bWeaknesses);
		when(beingStoryService.getBeingStoryByBeingId(being2.getBeingPK())).thenReturn(bStories);
		when(beingSymbolismService.getBeingSymbolismByBeingId(being2.getBeingPK())).thenReturn(bSymbols);
		when(beingLocationService.getBeingLocationByBeingId(being2.getBeingPK())).thenReturn(bLocations);

		when(abilityService.getAbilityById(ability.getAbilityPK())).thenReturn(ability);
		when(abilityService.getAbilityById(ability2.getAbilityPK())).thenReturn(ability2);
		when(weaknessService.getWeaknessById(weakness.getWeaknessPK())).thenReturn(weakness);
		when(weaknessService.getWeaknessById(weakness2.getWeaknessPK())).thenReturn(weakness2);
		when(storyService.getStoryById(story.getStoryPK())).thenReturn(story);
		when(storyService.getStoryById(story2.getStoryPK())).thenReturn(story2);
		when(symbolService.getSymbolById(symbol.getSymbolPK())).thenReturn(symbol);
		when(symbolService.getSymbolById(symbol2.getSymbolPK())).thenReturn(symbol2);
		when(locationService.getLocationById(location.getLocationPK())).thenReturn(location);
		when(locationService.getLocationById(location2.getLocationPK())).thenReturn(location2);


		List<BeingComposite> beingRecords = scotMythService.getBeingRecordByGender(being.getBeingGender());
		Assertions.assertEquals(2, beingRecords.size(), "getBeingByGender should return 2 being records");
		for(int i =0; i< expectedBeingCompResult.size(); i++) {

			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingPK(), beingRecords.get(i).getBeingPK(), "Check being" + i + "PK"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingSpecies(), beingRecords.get(i).getBeingSpecies(), "Check being " + i + " Species Name"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingSpeciesPK(), beingRecords.get(i).getBeingSpeciesPK(), "Check being " + i + " Species PK"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingGender(), beingRecords.get(i).getBeingGender(), "Check being " + i + " Gender Type"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingGenderPK(), beingRecords.get(i).getBeingGenderPK(), "Check being " + i + " Gender PK");
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingFaction(), beingRecords.get(i).getBeingFaction(), "Check being " + i + " Faction Name"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingFactionPK(), beingRecords.get(i).getBeingFactionPK(), "Check being " + i + " Faction PK"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingAbilities(), beingRecords.get(i).getBeingAbilities(), "Check being " + i + " Abilities"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingWeaknesses(), beingRecords.get(i).getBeingWeaknesses(), "Check being " + i + " Weaknesses"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingSymbolism(), beingRecords.get(i).getBeingSymbolism(), "Check being " + i + " Symbolism"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingStories(), beingRecords.get(i).getBeingStories(), "Check being " + i + " Stories"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingLocations(), beingRecords.get(i).getBeingLocations(), "Check being " + i + " Locations"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingName(), beingRecords.get(i).getBeingName(), "Check being " + i + " Name"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingDescription(), beingRecords.get(i).getBeingDescription(), "Check being " + i + " Description"); 
		}
	}

	@Test
	public void testGetBySpecies() {

		BeingComposite beingComp2 = new BeingComposite(being2, species.getSpeciesName(), gender.getGenderType(), faction.getFactionName(), abilities, weaknesses, symbols, stories, locations);
		List<BeingComposite> expectedBeingCompResult = Arrays.asList(beingComp, beingComp2);

		doReturn(Arrays.asList(being, being2)).when(beingService).getBeingBySpecies(being.getBeingSpecies());

		when(beingService.getBeingByName(being.getBeingName())).thenReturn(being);
		when(speciesService.getSpeciesById(being.getBeingSpecies())).thenReturn(species);
		when(genderService.getGenderById(being.getBeingGender())).thenReturn(gender);
		when(factionService.getFactionById(being.getBeingFaction())).thenReturn(faction);

		when(beingService.getBeingByName(being2.getBeingName())).thenReturn(being2);
		when(speciesService.getSpeciesById(being2.getBeingSpecies())).thenReturn(species);
		when(genderService.getGenderById(being2.getBeingGender())).thenReturn(gender);
		when(factionService.getFactionById(being2.getBeingFaction())).thenReturn(faction);

		when(beingAbilityService.getBeingAbilityByBeingId(being.getBeingPK())).thenReturn(bAbilities);
		when(beingWeaknessService.getBeingWeaknessByBeingId(being.getBeingPK())).thenReturn(bWeaknesses);
		when(beingStoryService.getBeingStoryByBeingId(being.getBeingPK())).thenReturn(bStories);
		when(beingSymbolismService.getBeingSymbolismByBeingId(being.getBeingPK())).thenReturn(bSymbols);
		when(beingLocationService.getBeingLocationByBeingId(being.getBeingPK())).thenReturn(bLocations);

		when(beingAbilityService.getBeingAbilityByBeingId(being2.getBeingPK())).thenReturn(bAbilities);
		when(beingWeaknessService.getBeingWeaknessByBeingId(being2.getBeingPK())).thenReturn(bWeaknesses);
		when(beingStoryService.getBeingStoryByBeingId(being2.getBeingPK())).thenReturn(bStories);
		when(beingSymbolismService.getBeingSymbolismByBeingId(being2.getBeingPK())).thenReturn(bSymbols);
		when(beingLocationService.getBeingLocationByBeingId(being2.getBeingPK())).thenReturn(bLocations);

		when(abilityService.getAbilityById(ability.getAbilityPK())).thenReturn(ability);
		when(abilityService.getAbilityById(ability2.getAbilityPK())).thenReturn(ability2);
		when(weaknessService.getWeaknessById(weakness.getWeaknessPK())).thenReturn(weakness);
		when(weaknessService.getWeaknessById(weakness2.getWeaknessPK())).thenReturn(weakness2);
		when(storyService.getStoryById(story.getStoryPK())).thenReturn(story);
		when(storyService.getStoryById(story2.getStoryPK())).thenReturn(story2);
		when(symbolService.getSymbolById(symbol.getSymbolPK())).thenReturn(symbol);
		when(symbolService.getSymbolById(symbol2.getSymbolPK())).thenReturn(symbol2);
		when(locationService.getLocationById(location.getLocationPK())).thenReturn(location);
		when(locationService.getLocationById(location2.getLocationPK())).thenReturn(location2);


		List<BeingComposite> beingRecords = scotMythService.getBeingRecordBySpecies(being.getBeingSpecies());
		Assertions.assertEquals(2, beingRecords.size(), "getBeingByGender should return 2 being records");
		for(int i =0; i< expectedBeingCompResult.size(); i++) {

			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingPK(), beingRecords.get(i).getBeingPK(), "Check being" + i + "PK"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingSpecies(), beingRecords.get(i).getBeingSpecies(), "Check being " + i + " Species Name"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingSpeciesPK(), beingRecords.get(i).getBeingSpeciesPK(), "Check being " + i + " Species PK"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingGender(), beingRecords.get(i).getBeingGender(), "Check being " + i + " Gender Type"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingGenderPK(), beingRecords.get(i).getBeingGenderPK(), "Check being " + i + " Gender PK");
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingFaction(), beingRecords.get(i).getBeingFaction(), "Check being " + i + " Faction Name"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingFactionPK(), beingRecords.get(i).getBeingFactionPK(), "Check being " + i + " Faction PK"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingAbilities(), beingRecords.get(i).getBeingAbilities(), "Check being " + i + " Abilities"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingWeaknesses(), beingRecords.get(i).getBeingWeaknesses(), "Check being " + i + " Weaknesses"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingSymbolism(), beingRecords.get(i).getBeingSymbolism(), "Check being " + i + " Symbolism"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingStories(), beingRecords.get(i).getBeingStories(), "Check being " + i + " Stories"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingLocations(), beingRecords.get(i).getBeingLocations(), "Check being " + i + " Locations"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingName(), beingRecords.get(i).getBeingName(), "Check being " + i + " Name"); 
			Assertions.assertEquals(expectedBeingCompResult.get(i).getBeingDescription(), beingRecords.get(i).getBeingDescription(), "Check being " + i + " Description"); 
		}
	}

	// UPDATE
	@Test
	@DisplayName("Test updateBeingRecord success")
	public void testSuccessfulUpdate() {

		// Mock service calls
		when(beingService.createBeing(being)).thenReturn(being);

		// Calls occur during ConvertToComposite which occurs during getBeingRecordById
		when(beingService.getBeingByName(being.getBeingName())).thenReturn(being);
		when(beingService.getBeingById(being.getBeingPK())).thenReturn(being);
		when(beingService.updateBeing(any())).thenReturn(true);
	    doReturn(Optional.of(being)).when(beingDao).findById(being.getBeingPK());


		when(speciesService.getSpeciesById(being.getBeingSpecies())).thenReturn(species);
		when(genderService.getGenderById(being.getBeingGender())).thenReturn(gender);
		when(factionService.getFactionById(being.getBeingFaction())).thenReturn(faction);
		when(speciesService.getSpeciesByName(beingComp.getBeingSpecies())).thenReturn(species);
		when(genderService.getGenderByName(beingComp.getBeingGender())).thenReturn(gender);
		when(factionService.getFactionByName(beingComp.getBeingFaction())).thenReturn(faction);

		when(beingAbilityService.getBeingAbilityByBeingId(being.getBeingPK())).thenReturn(bAbilities);
		when(beingWeaknessService.getBeingWeaknessByBeingId(being.getBeingPK())).thenReturn(bWeaknesses);
		when(beingStoryService.getBeingStoryByBeingId(being.getBeingPK())).thenReturn(bStories);
		when(beingSymbolismService.getBeingSymbolismByBeingId(being.getBeingPK())).thenReturn(bSymbols);
		when(beingLocationService.getBeingLocationByBeingId(being.getBeingPK())).thenReturn(bLocations);

		when(abilityService.getAbilityById(ability.getAbilityPK())).thenReturn(ability);
		when(abilityService.getAbilityById(ability2.getAbilityPK())).thenReturn(ability2);
		when(weaknessService.getWeaknessById(weakness.getWeaknessPK())).thenReturn(weakness);
		when(weaknessService.getWeaknessById(weakness2.getWeaknessPK())).thenReturn(weakness2);
		when(storyService.getStoryById(story.getStoryPK())).thenReturn(story);
		when(storyService.getStoryById(story2.getStoryPK())).thenReturn(story2);
		when(symbolService.getSymbolById(symbol.getSymbolPK())).thenReturn(symbol);
		when(symbolService.getSymbolById(symbol2.getSymbolPK())).thenReturn(symbol2);
		when(locationService.getLocationById(location.getLocationPK())).thenReturn(location);
		when(locationService.getLocationById(location2.getLocationPK())).thenReturn(location2);


		// Calls occur during createUpdate methods
		when(beingService.getBeingByName("Being Name")).thenReturn(being);
		when(abilityService.getAbilityByName("Ability 1")).thenReturn(ability);
		when(abilityService.getAbilityByName("Ability 2")).thenReturn(ability2);
		when(beingAbilityService.getBeingAbilityById(any())).thenReturn(null);
		when(beingAbilityService.createBeingAbility(bA1)).thenReturn(bA1);
		when(beingAbilityService.createBeingAbility(bA2)).thenReturn(bA2);
		when(beingAbilityService.deleteBeingAbility(kBA1)).thenReturn(true);
		when(beingAbilityService.deleteBeingAbility(kBA2)).thenReturn(true);



		when(weaknessService.getWeaknessByName("Weakness 1")).thenReturn(weakness);
		when(weaknessService.getWeaknessByName("Weakness 2")).thenReturn(weakness2);
		when(beingWeaknessService.getBeingWeaknessById(any())).thenReturn(null);
		when(beingWeaknessService.createBeingWeakness(bW1)).thenReturn(bW1);
		when(beingWeaknessService.createBeingWeakness(bW2)).thenReturn(bW2);
		when(beingWeaknessService.deleteBeingWeakness(kBW1)).thenReturn(true);
		when(beingWeaknessService.deleteBeingWeakness(kBW2)).thenReturn(true);


		when(storyService.getStoryByName("Story 1")).thenReturn(story);
		when(storyService.getStoryByName("Story 2")).thenReturn(story2);
		when(beingStoryService.getBeingStoryById(any())).thenReturn(null);
		when(beingStoryService.createBeingStory(bSt1)).thenReturn(bSt1);
		when(beingStoryService.createBeingStory(bSt2)).thenReturn(bSt2);
		when(beingStoryService.deleteBeingStory(kBSt1)).thenReturn(true);
		when(beingStoryService.deleteBeingStory(kBSt2)).thenReturn(true);



		when(symbolService.getSymbolByName("Symbol 1")).thenReturn(symbol);
		when(symbolService.getSymbolByName("Symbol 2")).thenReturn(symbol2);
		when(beingSymbolismService.getBeingSymbolismById(any())).thenReturn(null);
		when(beingSymbolismService.createBeingSymbolism(bSy1)).thenReturn(bSy1);
		when(beingSymbolismService.createBeingSymbolism(bSy2)).thenReturn(bSy2);
		when(beingSymbolismService.deleteBeingSymbolism(kBSy1)).thenReturn(true);
		when(beingSymbolismService.deleteBeingSymbolism(kBSy2)).thenReturn(true);



		when(locationService.getLocationByName("Location 1")).thenReturn(location);
		when(locationService.getLocationByName("Location 2")).thenReturn(location2);
		when(beingLocationService.getBeingLocationById(any())).thenReturn(null);
		when(beingLocationService.createBeingLocation(bL1)).thenReturn(bL1);
		when(beingLocationService.createBeingLocation(bL2)).thenReturn(bL2);
		when(beingLocationService.deleteBeingLocation(kBL1)).thenReturn(true);
		when(beingLocationService.deleteBeingLocation(kBL2)).thenReturn(true);


		// Call the method to test
		Boolean result = scotMythService.updateBeingRecord(beingComp);

		// Assertions and verifications
		Assertions.assertTrue(result);
		
	}

	// DELETE
	@Test
	public void testSuccessfulDelete() {
		
		when(beingService.deleteBeing(being.getBeingPK())).thenReturn(true);
		
		Boolean result = scotMythService.deleteBeingRecord(being.getBeingPK());
		
		Assertions.assertTrue(result);
	}
}
