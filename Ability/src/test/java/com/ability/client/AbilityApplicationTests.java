package com.ability.client;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.http.MediaType;

import com.ability.dao.AbilityDao;
import com.ability.entity.Ability;
import com.ability.service.AbilityService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@AutoConfigureMockMvc
class AbilityApplicationTests {

	@Test
	void contextLoads() {
	}
	
	//@Autowired
	//private AbilityService abilityService;
	@MockBean
	private AbilityDao abilityDao;
	@Autowired
    private MockMvc mockMvc;
	@Autowired
	private AbilityService abilityService;
	
	
	// Service Tests
	@Test
	@DisplayName("Test getting ability by ID")
	void testGetByID() {
		
		// Setting up mock repository
		Ability ability = new Ability(1, "TestAbility");
		doReturn(Optional.of(ability)).when(abilityDao).findById(1);
		
		// Execute the service call
		Optional<Ability> returnedAbility = Optional.ofNullable(abilityService.getAbilityById(1));
		
		// Assert the response
		Assertions.assertTrue(returnedAbility.isPresent(), "Ability was not found");
		Assertions.assertSame(returnedAbility.get(), ability, "The ability returned was not the same as the mock");
		
	}

	@Test
	@DisplayName("Test Not Found get ability by ID")
	void testGetByIdNotFound() {
		// Setup our mock repository
		doReturn(Optional.empty()).when(abilityDao).findById(1);

		// Execute the service call
		Optional<Ability> returnedAbility = Optional.ofNullable(abilityService.getAbilityById(1));

		// Assert the response
		Assertions.assertFalse(returnedAbility.isPresent(), "Ability should NOT be found, but was");
	}
	
	@Test
	@DisplayName("Test findAll")
	void testFindAll() {
		// Setup our mock repository
		Ability ability1 = new Ability(1, "Ability Name");
		Ability ability2 = new Ability(2, "Ability 2 Name");
		doReturn(Arrays.asList(ability1, ability2)).when(abilityDao).findAll();
		when(abilityDao.findAll()).thenReturn(Arrays.asList(ability1, ability2));

		// Execute the service call
		List<Ability> abilitys = abilityService.getAllAbility();

		// Assert the response
		Assertions.assertEquals(2, abilitys.size(), "findAll should return 2 abilities");
	}
	
	@Test
	@DisplayName("Test create ability")
	void testCreate() {
	    // Setup our mock repository
	    Ability ability = new Ability(1, "Ability Name");

	    // Mocking behaviour for abilityDao.save
	    doAnswer(invocation -> {
	        Ability argAbility = invocation.getArgument(0);
	        return new Ability(argAbility.getAbilityPK() + 1, argAbility.getAbilityName());
	    }).when(abilityDao).save(any());

	    // Mocking behaviour for getAbilityByName
	    when(abilityDao.findByName(anyString())).thenReturn(new Ability(2, "Ability Name"));

	    // Execute the service call
	    Ability returnedAbility = abilityService.createAbility(ability);

	    // Assert the response
	    Assertions.assertNotNull(returnedAbility, "The saved ability should not be null");
	    Assertions.assertEquals(2, returnedAbility.getAbilityPK(), "The ID should be incremented");
	}


	
	@Test
	@DisplayName("Test delete ability")
	void testDelete() {
	    // Setup our mock repository
	    Ability ability = new Ability(1, "Ability Name");
	    
	    doReturn(Optional.of(ability)).when(abilityDao).findById(1);

	    doNothing().when(abilityDao).deleteById(1);

	    // Execute the service call
	    Boolean returnedAbility = abilityService.deleteAbility(1);

	    // Assert the response
	    Assertions.assertNotNull(returnedAbility, "The deleted ability should not be null, should be boolean.");
	    
	    // After deletion, the ability should not be found.
	    doReturn(Optional.empty()).when(abilityDao).findById(1);
	    Assertions.assertNull(abilityService.getAbilityById(1));

	    Assertions.assertEquals(true, returnedAbility, "Should return true if deleted.");
	}


	
	@Test
	@DisplayName("Test edit ability")
	void testEdit() {
	    // Setup our mock repository
	    Ability ability = new Ability(1, "Ability Name");
	    
	    doReturn(Optional.of(ability)).when(abilityDao).findById(1);

	    // Execute the service call
	    Ability updatedAbility = new Ability(1, "Updated Ability Name");
	    abilityService.updateAbility(updatedAbility);
	    
	    doReturn(Optional.of(updatedAbility)).when(abilityDao).findById(1);
	    
	    Ability returnedAbility = abilityService.getAbilityById(1);

	    // Assert the response
	    Assertions.assertNotNull(returnedAbility, "The updated ability should not be null");
	    Assertions.assertEquals(1, returnedAbility.getAbilityPK(), "The ID should be same as the one updated");
	    Assertions.assertEquals("Updated Ability Name", returnedAbility.getAbilityName(), "The Name should be updated");
	}


}
