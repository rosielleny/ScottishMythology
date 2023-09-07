package com.species.client;

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

import com.species.dao.SpeciesDao;
import com.species.entity.Species;
import com.species.service.SpeciesService;
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
class SpeciesApplicationTests {

	@Test
	void contextLoads() {
	}
	
	//@Autowired
	//private SpeciesService speciesService;
	@MockBean
	private SpeciesDao speciesDao;
	@Autowired
    private MockMvc mockMvc;
	@Autowired
	private SpeciesService speciesService;
	
	
	// Service Tests
	@Test
	@DisplayName("Test getting species by ID")
	void testGetByID() {
		
		// Setting up mock repository
		Species species = new Species(1, "TestSpecies", "Description");
		doReturn(Optional.of(species)).when(speciesDao).findById(1);
		
		// Execute the service call
		Optional<Species> returnedSpecies = Optional.ofNullable(speciesService.getSpeciesById(1));
		
		// Assert the response
		Assertions.assertTrue(returnedSpecies.isPresent(), "Species was not found");
		Assertions.assertSame(returnedSpecies.get(), species, "The species returned was not the same as the mock");
		
	}

	@Test
	@DisplayName("Test Not Found get species by ID")
	void testGetByIdNotFound() {
		// Setup our mock repository
		doReturn(Optional.empty()).when(speciesDao).findById(1);

		// Execute the service call
		Optional<Species> returnedSpecies = Optional.ofNullable(speciesService.getSpeciesById(1));

		// Assert the response
		Assertions.assertFalse(returnedSpecies.isPresent(), "Species should NOT be found, but was");
	}
	
	@Test
	@DisplayName("Test findAll")
	void testFindAll() {
		// Setup our mock repository
		Species species1 = new Species(1, "Species Name", "Description");
		Species species2 = new Species(2, "Species 2 Name", "Description");
		doReturn(Arrays.asList(species1, species2)).when(speciesDao).findAll();
		when(speciesDao.findAll()).thenReturn(Arrays.asList(species1, species2));

		// Execute the service call
		List<Species> speciess = speciesService.getAllSpecies();

		// Assert the response
		Assertions.assertEquals(2, speciess.size(), "findAll should return 2 abilities");
	}
	
	@Test
	@DisplayName("Test create species")
	void testCreate() {
	    // Setup our mock repository
	    Species species = new Species(1, "Species Name", "Description");

	    // Mocking behaviour for speciesDao.save
	    doAnswer(invocation -> {
	        Species argSpecies = invocation.getArgument(0);
	        return new Species(argSpecies.getSpeciesPK() + 1, argSpecies.getSpeciesName(), argSpecies.getSpeciesDescription());
	    }).when(speciesDao).save(any());

	    // Mocking behaviour for getSpeciesByName
	    when(speciesDao.findByName(anyString())).thenReturn(new Species(2, "Species Name", "Description"));

	    // Execute the service call
	    Species returnedSpecies = speciesService.createSpecies(species);

	    // Assert the response
	    Assertions.assertNotNull(returnedSpecies, "The saved species should not be null");
	    Assertions.assertEquals(2, returnedSpecies.getSpeciesPK(), "The ID should be incremented");
	}


	
	@Test
	@DisplayName("Test delete species")
	void testDelete() {
	    // Setup our mock repository
	    Species species = new Species(1, "Species Name", "Description");
	    
	    doReturn(Optional.of(species)).when(speciesDao).findById(1);

	    doNothing().when(speciesDao).deleteById(1);

	    // Execute the service call
	    Boolean returnedSpecies = speciesService.deleteSpecies(1);

	    // Assert the response
	    Assertions.assertNotNull(returnedSpecies, "The deleted species should not be null, should be boolean.");
	    
	    // After deletion, the species should not be found.
	    doReturn(Optional.empty()).when(speciesDao).findById(1);
	    Assertions.assertNull(speciesService.getSpeciesById(1));

	    Assertions.assertEquals(true, returnedSpecies, "Should return true if deleted.");
	}


	
	@Test
	@DisplayName("Test edit species")
	void testEdit() {
	    // Setup our mock repository
	    Species species = new Species(1, "Species Name", "Description");
	    
	    doReturn(Optional.of(species)).when(speciesDao).findById(1);

	    // Execute the service call
	    Species updatedSpecies = new Species(1, "Updated Species Name", "Description");
	    speciesService.updateSpecies(updatedSpecies);
	    
	    doReturn(Optional.of(updatedSpecies)).when(speciesDao).findById(1);
	    
	    Species returnedSpecies = speciesService.getSpeciesById(1);

	    // Assert the response
	    Assertions.assertNotNull(returnedSpecies, "The updated species should not be null");
	    Assertions.assertEquals(1, returnedSpecies.getSpeciesPK(), "The ID should be same as the one updated");
	    Assertions.assertEquals("Updated Species Name", returnedSpecies.getSpeciesName(), "The Name should be updated");
	}


}
