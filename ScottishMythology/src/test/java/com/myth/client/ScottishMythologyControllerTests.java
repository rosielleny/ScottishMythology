package com.myth.client;

import org.hamcrest.Matchers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@AutoConfigureMockMvc
class ScottishMythologyControllerTests {

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
	ScottishMythologyService scotMythService;
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
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	Being being = new Being(1, "Being Name", 1, "Being Description", 1, null, 1);
	Being being2 = new Being(2, "Being Name 2", 1, "Being Description 2", 1, null, 1);

	Faction faction = new Faction(1, "Faction", "Faction Description");
	Faction faction2 = new Faction(2, "Faction 2", "Faction Description 2");

	Species species = new Species(1, "Species", "Species Description");
	Species species2 = new Species(2, "Species 2", "Species Description 2");
	
	Gender gender = new Gender(1, "Gender");
	Gender gender2 = new Gender(2, "Gender 2");


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
	List<BeingStory> bBeings = new ArrayList<>(Arrays.asList(bSt1, bSt2));

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
	List<String> beings = new ArrayList<>(Arrays.asList("Being 1", "Being 2"));
	List<String> locations = new ArrayList<>(Arrays.asList("Location 1", "Location 2"));


	BeingComposite beingComp = new BeingComposite(being, species.getSpeciesName(), gender.getGenderType(), faction.getFactionName(), abilities, weaknesses, symbols, beings, locations);
	BeingComposite beingComp2 = new BeingComposite(being, species.getSpeciesName(), gender.getGenderType(), faction.getFactionName(), abilities, weaknesses, symbols, beings, locations);



	// CREATE
		@Test
		public void testCreateAnBeing() throws Exception {
			
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.addObject("entitySearchPath", "/" + "being" + "/search");
			modelAndView.addObject("showAllEntityPath", "/" + "being" + "/" + "beings");
			modelAndView.addObject("editEntityPath", "/" + "being" + "/edit");
			modelAndView.addObject("deleteEntityPath", "/" + "being" + "/delete");
			modelAndView.addObject("createEntityPath", "/" + "being" + "/create");
			modelAndView.addObject("checkEntityName", "being" + "/check-" + "being" + "-name");
			modelAndView.addObject("entityPath", "/" + "being" + "/save-new");
			modelAndView.addObject("entitySaveUpdatePath", "/" + "being" + "/save-edit");
			
			
			modelAndView.addObject("label", "Being" + " Name:");
			modelAndView.addObject("entityType", "Being");
			
			when(scotMythService.getAllBeingRecords()).thenReturn(new ArrayList<>(Arrays.asList(beingComp, beingComp2)));
			when(abilityService.getAllAbility()).thenReturn(new ArrayList<>(Arrays.asList(ability, ability2)));
			when(factionService.getAllFaction()).thenReturn(new ArrayList<>(Arrays.asList(faction, faction2)));
			when(speciesService.getAllSpecies()).thenReturn(new ArrayList<>(Arrays.asList(species, species2)));
			when(genderService.getAllGender()).thenReturn(new ArrayList<>(Arrays.asList(gender, gender2)));
			when(locationService.getAllLocation()).thenReturn(new ArrayList<>(Arrays.asList(location, location2)));
			when(storyService.getAllStory()).thenReturn(new ArrayList<>(Arrays.asList(story, story2)));
			when(symbolService.getAllSymbol()).thenReturn(new ArrayList<>(Arrays.asList(symbol, symbol2)));
			when(weaknessService.getAllWeakness()).thenReturn(new ArrayList<>(Arrays.asList(weakness, weakness2)));
			
			  Mockito.when(scotMythService.setUpLinks(anyString(), anyString(), any(ModelAndView.class)))
	            .thenReturn(modelAndView);

	        mockMvc.perform(get("/being/create"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("being/create-being"))
	            .andExpect(model().attributeExists("being"))
	            .andExpect(model().attributeExists("beings"))
	            .andReturn();
		}



		@Test
		@DisplayName("Test form validation failure")
		public void testFormValidationFailure() throws Exception {
			
			
			Being createdBeing = being;
			createdBeing.setBeingName(null);
			
			when(beingService.getBeingById(any(Integer.class))).thenReturn(createdBeing);
			when(beingService.createBeing(any(Being.class))).thenReturn(createdBeing);
			when(beingService.getBeingByName(any(String.class))).thenReturn(createdBeing);

			mockMvc.perform(post("/being/save-new")
					.param("beingName", "") // Invalid input
					)
			.andExpect(status().isOk()) // Expect HTTP 200 status
			.andExpect(model().attributeHasFieldErrors("being", "beingName")) // Expect validation error
			.andExpect(view().name("being/create-being")); // Expect view name
		}

		@Test
		@DisplayName("Test successful being creation")
		public void testSuccessfulBeingCreation() throws Exception {
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.addObject("entitySearchPath", "/" + "being" + "/search");
			modelAndView.addObject("showAllEntityPath", "/" + "being" + "/" + "beings");
			modelAndView.addObject("editEntityPath", "/" + "being" + "/edit");
			modelAndView.addObject("deleteEntityPath", "/" + "being" + "/delete");
			modelAndView.addObject("createEntityPath", "/" + "being" + "/create");
			modelAndView.addObject("checkEntityName", "being" + "/check-" + "being" + "-name");
			modelAndView.addObject("entityPath", "/" + "being" + "/save-new");
			modelAndView.addObject("entitySaveUpdatePath", "/" + "being" + "/save-edit");
			
			
			modelAndView.addObject("label", "Being" + " Name:");
			modelAndView.addObject("entityType", "Being");
			
			when(scotMythService.createBeingRecord(beingComp)).thenReturn(beingComp);
			Mockito.when(scotMythService.setUpLinks(anyString(), anyString(), any(ModelAndView.class)))
            .thenReturn(modelAndView);
		
			 BindingResult bindingResult = mock(BindingResult.class);
		        when(bindingResult.hasErrors()).thenReturn(false); // No validation errors

		        when(scotMythService.createBeingRecord(beingComp)).thenReturn(beingComp);

		        // Perform the HTTP POST request
		        mockMvc.perform(MockMvcRequestBuilders.post("/being/save-new")
		                .flashAttr("being", beingComp) // Add the being as a model attribute
		                .flashAttr("org.springframework.validation.BindingResult.being", bindingResult)) // Add BindingResult
		                .andExpect(MockMvcResultMatchers.status().isOk())
		                .andExpect(MockMvcResultMatchers.model().attribute("message", "Being Name successfully added to the database."))
		                .andExpect(MockMvcResultMatchers.view().name("being/being-output"));
		    
		}

		@Test
		@DisplayName("Test error handling")
		public void testErrorHandling() throws Exception {
			// Mock an error during being creation
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.addObject("entitySearchPath", "/" + "being" + "/search");
			modelAndView.addObject("showAllEntityPath", "/" + "being" + "/" + "beings");
			modelAndView.addObject("editEntityPath", "/" + "being" + "/edit");
			modelAndView.addObject("deleteEntityPath", "/" + "being" + "/delete");
			modelAndView.addObject("createEntityPath", "/" + "being" + "/create");
			modelAndView.addObject("checkEntityName", "being" + "/check-" + "being" + "-name");
			modelAndView.addObject("entityPath", "/" + "being" + "/save-new");
			modelAndView.addObject("entitySaveUpdatePath", "/" + "being" + "/save-edit");
			
			
			modelAndView.addObject("label", "Being" + " Name:");
			modelAndView.addObject("entityType", "Being");
			
			when(scotMythService.createBeingRecord(beingComp)).thenReturn(beingComp);
			
			Mockito.when(scotMythService.setUpLinks(anyString(), anyString(), any(ModelAndView.class)))
            .thenReturn(modelAndView);
			
			when(scotMythService.createBeingRecord(any(BeingComposite.class))).thenReturn(null);
			
			System.out.println(beingComp);

			// Create a POST request with valid input
			mockMvc.perform(post("/being/save-new")
			        .param("beingPK", "1")
			        .param("beingName", "Being Name")
			        .param("beingSpeciesPK", "1")
			        .param("beingDescription", "Being Description")
			        .param("beingGenderPK", "1")
			        .param("beingArt", "")
			        .param("beingSpecies", "Species")
			        .param("beingGender", "Gender")
			        .param("beingFactionPK", "1")
			        .param("beingFaction", "Faction")
			        .param("beingAbilities", "Ability 1", "Ability 2")
			        .param("beingWeaknesses", "Weakness 1", "Weakness 2")
			        .param("beingSymbolism", "Symbol 1", "Symbol 2")
			        .param("beingStories", "Being 1", "Being 2")
			        .param("beingLocations", "Location 1", "Location 2")
			)
			.andExpect(status().isOk()) // Expect HTTP 200 status
			.andExpect(model().attribute("message", "An error occurred. Being not added to the database.")) // Expect error message
			.andExpect(view().name("being/being-output")); // Expect view name
		}
		
		// READ
		
	    @Test
	    @DisplayName("Test show all beings")
	    public void testShowAllBeings() throws Exception {
	        // Mock your beingService to return a list of Being objects
	        List<BeingComposite> beings = new ArrayList<>();
	       
	        beings.add(beingComp2);
	        beings.add(beingComp);
	        
	        when(scotMythService.getAllBeingRecords()).thenReturn(beings);
	        
	        ModelAndView modelAndView = new ModelAndView();
			modelAndView.addObject("entitySearchPath", "/" + "being" + "/search");
			modelAndView.addObject("showAllEntityPath", "/" + "being" + "/" + "beings");
			modelAndView.addObject("editEntityPath", "/" + "being" + "/edit");
			modelAndView.addObject("deleteEntityPath", "/" + "being" + "/delete");
			modelAndView.addObject("createEntityPath", "/" + "being" + "/create");
			modelAndView.addObject("checkEntityName", "being" + "/check-" + "being" + "-name");
			modelAndView.addObject("entityPath", "/" + "being" + "/save-new");
			modelAndView.addObject("entitySaveUpdatePath", "/" + "being" + "/save-edit");
			
			
			modelAndView.addObject("label", "Being" + " Name:");
			modelAndView.addObject("entityType", "Being");
			
			when(scotMythService.createBeingRecord(beingComp)).thenReturn(beingComp);
			Mockito.when(scotMythService.setUpLinks(anyString(), anyString(), any(ModelAndView.class)))
            .thenReturn(modelAndView);

	        mockMvc.perform(get("/being/beings"))
	            .andExpect(status().isOk()) // Expect HTTP 200 status
	            .andExpect(view().name("being/show-all-beings")) // Expect view name
	            .andExpect(model().attributeExists("beingList")) // Expect model attribute
	            .andExpect(model().attribute("beingList", Matchers.hasSize(2))); // Expect beingList size to match the number of beings
	    }
	    
	    @Test
	    @DisplayName("Test search beings by name")
	    public void testSearchBeingsByName() throws Exception {
	       
	        
	        ModelAndView modelAndView = new ModelAndView();
			modelAndView.addObject("entitySearchPath", "/" + "being" + "/search");
			modelAndView.addObject("showAllEntityPath", "/" + "being" + "/" + "beings");
			modelAndView.addObject("editEntityPath", "/" + "being" + "/edit");
			modelAndView.addObject("deleteEntityPath", "/" + "being" + "/delete");
			modelAndView.addObject("createEntityPath", "/" + "being" + "/create");
			modelAndView.addObject("checkEntityName", "being" + "/check-" + "being" + "-name");
			modelAndView.addObject("entityPath", "/" + "being" + "/save-new");
			modelAndView.addObject("entitySaveUpdatePath", "/" + "being" + "/save-edit");
			
			
			modelAndView.addObject("label", "Being" + " Name:");
			modelAndView.addObject("entityType", "Being");
			
			when(scotMythService.createBeingRecord(beingComp)).thenReturn(beingComp);
			Mockito.when(scotMythService.setUpLinks(anyString(), anyString(), any(ModelAndView.class)))
            .thenReturn(modelAndView);
	        
	        when(beingService.getBeingByName("ExistingBeing")).thenReturn(being);
	        when(beingService.getBeingByName("NonExistingBeing")).thenReturn(null);
	        when(scotMythService.getBeingRecordByName("ExistingBeing")).thenReturn(beingComp);
	        when(scotMythService.getBeingRecordByName("NonExistingBeing")).thenReturn(beingComp2);
	        
	        mockMvc.perform(get("/being/search")
	                .param("name", "ExistingBeing")) // Valid input for an existing being
	            .andExpect(status().isOk()) // Expect HTTP 200 status
	            .andExpect(view().name("being/show-being")) // Expect view name
	            .andExpect(model().attributeExists("being")) // Expect model attribute
	            .andExpect(model().attribute("being", Matchers.hasProperty("beingName", is("Being Name")))); // Expect being attribute

	        mockMvc.perform(get("/being/search")
	                .param("name", "NonExistingBeing")) // Valid input for a non-existing being
	            .andExpect(status().isOk()) // Expect HTTP 200 status
	            .andExpect(view().name("being/show-being")) // Expect view name
	            .andExpect(model().attribute("message", is("No beings were found for NonExistingBeing"))); // Expect message attribute
	    }
	    
	    // UPDATE
	    
	    @Test
	    @DisplayName("Test edit beings by ID")
	    public void testEditBeingsById() throws Exception {
	    	
	    	List<BeingComposite> beingComps = new ArrayList<>();
	    	beingComps.add(beingComp2);
	    	beingComps.add(beingComp);
	        
	    	 ModelAndView modelAndView = new ModelAndView();
				modelAndView.addObject("entitySearchPath", "/" + "being" + "/search");
				modelAndView.addObject("showAllEntityPath", "/" + "being" + "/" + "beings");
				modelAndView.addObject("editEntityPath", "/" + "being" + "/edit");
				modelAndView.addObject("deleteEntityPath", "/" + "being" + "/delete");
				modelAndView.addObject("createEntityPath", "/" + "being" + "/create");
				modelAndView.addObject("checkEntityName", "being" + "/check-" + "being" + "-name");
				modelAndView.addObject("entityPath", "/" + "being" + "/save-new");
				modelAndView.addObject("entitySaveUpdatePath", "/" + "being" + "/save-edit");
				
				
				modelAndView.addObject("label", "Being" + " Name:");
				modelAndView.addObject("entityType", "Being");
				
			
			
				when(scotMythService.createBeingRecord(beingComp)).thenReturn(beingComp);
				Mockito.when(scotMythService.setUpLinks(anyString(), anyString(), any(ModelAndView.class)))
	            .thenReturn(modelAndView);
	        
	        when(beingService.getBeingById(1)).thenReturn(being);
	        when(beingService.getBeingById(2)).thenReturn(being2);
	        when(scotMythService.getBeingRecordById(1)).thenReturn(beingComp);
			when(scotMythService.getBeingRecordById(2)).thenReturn(beingComp2);
	        when(scotMythService.getAllBeingRecords()).thenReturn(beingComps);
	        
	        mockMvc.perform(get("/being/edit")
	                .param("pk", "1")) // Valid input for an existing being
	            .andExpect(status().isOk()) // Expect HTTP 200 status
	            .andExpect(view().name("being/edit-being")) // Expect view name
	            .andExpect(model().attributeExists("being")) // Expect model attribute
	            .andExpect(model().attributeExists("ogBeing")) // Expect model attribute
	            .andExpect(model().attribute("being", Matchers.hasProperty("beingName", is("Being Name")))); // Expect being attribute
	    }
	    
	    

	    // DELETE
	    
	    @Test
	    @DisplayName("Test successful being deletion")
	    public void testSuccessfulBeingDeletion() throws Exception {
	    	
	        int beingIdToDelete = 1;
	        List<Being> beings = new ArrayList<>();
	        beings.add(being2);
	        beings.add(being);
	        List<BeingComposite> beingComps = new ArrayList<>();
	        beingComps.add(beingComp2);
	        beingComps.add(beingComp);
	        
	        ModelAndView modelAndView = new ModelAndView();
			modelAndView.addObject("entitySearchPath", "/" + "being" + "/search");
			modelAndView.addObject("showAllEntityPath", "/" + "being" + "/" + "beings");
			modelAndView.addObject("editEntityPath", "/" + "being" + "/edit");
			modelAndView.addObject("deleteEntityPath", "/" + "being" + "/delete");
			modelAndView.addObject("createEntityPath", "/" + "being" + "/create");
			modelAndView.addObject("checkEntityName", "being" + "/check-" + "being" + "-name");
			modelAndView.addObject("entityPath", "/" + "being" + "/save-new");
			modelAndView.addObject("entitySaveUpdatePath", "/" + "being" + "/save-edit");
			
			
			modelAndView.addObject("label", "Being" + " Name:");
			modelAndView.addObject("entityType", "Being");
			
		
			when(scotMythService.createBeingRecord(beingComp)).thenReturn(beingComp);
			Mockito.when(scotMythService.setUpLinks(anyString(), anyString(), any(ModelAndView.class)))
            .thenReturn(modelAndView);

	        when(scotMythService.getBeingRecordById(being.getBeingPK())).thenReturn(beingComp);
	        when(scotMythService.deleteBeingRecord(being.getBeingPK())).thenReturn(true);
	        when(beingService.getAllBeing()).thenReturn(beings);
	        when(scotMythService.getAllBeingRecords()).thenReturn(beingComps);



	        mockMvc.perform(get("/being/delete/{pk}", beingIdToDelete))
	            .andExpect(status().isOk())
	            .andExpect(model().attribute("message", "Being Name successfully deleted from Beings."))
	            .andExpect(view().name("being/show-all-beings"));

	        verify(scotMythService, times(1)).getBeingRecordById(beingIdToDelete);
	        verify(scotMythService, times(1)).deleteBeingRecord(beingIdToDelete);
	    }

	    @Test
	    @DisplayName("Test unsuccessful being deletion")
	    public void testUnsuccessfulBeingDeletion() throws Exception {
	        int beingIdToDelete = 1;
	      
	        List<BeingComposite> beingComps = new ArrayList<>();
	        beingComps.add(beingComp2);
	        beingComps.add(beingComp);
	        
	        ModelAndView modelAndView = new ModelAndView();
			modelAndView.addObject("entitySearchPath", "/" + "being" + "/search");
			modelAndView.addObject("showAllEntityPath", "/" + "being" + "/" + "beings");
			modelAndView.addObject("editEntityPath", "/" + "being" + "/edit");
			modelAndView.addObject("deleteEntityPath", "/" + "being" + "/delete");
			modelAndView.addObject("createEntityPath", "/" + "being" + "/create");
			modelAndView.addObject("checkEntityName", "being" + "/check-" + "being" + "-name");
			modelAndView.addObject("entityPath", "/" + "being" + "/save-new");
			modelAndView.addObject("entitySaveUpdatePath", "/" + "being" + "/save-edit");
			
			
			modelAndView.addObject("label", "Being" + " Name:");
			modelAndView.addObject("entityType", "Being");
			
		
			when(scotMythService.createBeingRecord(beingComp)).thenReturn(beingComp);
			Mockito.when(scotMythService.setUpLinks(anyString(), anyString(), any(ModelAndView.class)))
            .thenReturn(modelAndView);
	        
	        
	        when(scotMythService.getBeingRecordById(beingIdToDelete)).thenReturn(beingComp);
	        when(scotMythService.deleteBeingRecord(beingIdToDelete)).thenReturn(false);
	        when(scotMythService.getAllBeingRecords()).thenReturn(beingComps);

	        mockMvc.perform(get("/being/delete/{pk}", beingIdToDelete))
	            .andExpect(status().isOk())
	            .andExpect(model().attribute("message", "Failed to delete Being Name from Beings."))
	            .andExpect(view().name("being/show-all-beings"));

	        // Verify that the service method was called with the correct parameter
	        verify(scotMythService, times(1)).getBeingRecordById(beingIdToDelete);
	        verify(scotMythService, times(1)).deleteBeingRecord(beingIdToDelete);
	    }
}
