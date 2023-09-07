package com.gender.client;

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

import com.gender.dao.GenderDao;
import com.gender.entity.Gender;
import com.gender.service.GenderService;
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
class GenderApplicationTests {

	@Test
	void contextLoads() {
	}
	
	//@Autowired
	//private GenderService genderService;
	@MockBean
	private GenderDao genderDao;
	@Autowired
    private MockMvc mockMvc;
	@Autowired
	private GenderService genderService;
	
	
	// Service Tests
	@Test
	@DisplayName("Test getting gender by ID")
	void testGetByID() {
		
		// Setting up mock repository
		Gender gender = new Gender(1, "TestGender");
		doReturn(Optional.of(gender)).when(genderDao).findById(1);
		
		// Execute the service call
		Optional<Gender> returnedGender = Optional.ofNullable(genderService.getGenderById(1));
		
		// Assert the response
		Assertions.assertTrue(returnedGender.isPresent(), "Gender was not found");
		Assertions.assertSame(returnedGender.get(), gender, "The gender returned was not the same as the mock");
		
	}

	@Test
	@DisplayName("Test Not Found get gender by ID")
	void testGetByIdNotFound() {
		// Setup our mock repository
		doReturn(Optional.empty()).when(genderDao).findById(1);

		// Execute the service call
		Optional<Gender> returnedGender = Optional.ofNullable(genderService.getGenderById(1));

		// Assert the response
		Assertions.assertFalse(returnedGender.isPresent(), "Gender should NOT be found, but was");
	}
	
	@Test
	@DisplayName("Test findAll")
	void testFindAll() {
		// Setup our mock repository
		Gender gender1 = new Gender(1, "Gender Name");
		Gender gender2 = new Gender(2, "Gender 2 Name");
		doReturn(Arrays.asList(gender1, gender2)).when(genderDao).findAll();
		when(genderDao.findAll()).thenReturn(Arrays.asList(gender1, gender2));

		// Execute the service call
		List<Gender> genders = genderService.getAllGender();

		// Assert the response
		Assertions.assertEquals(2, genders.size(), "findAll should return 2 abilities");
	}
	
	@Test
	@DisplayName("Test create gender")
	void testCreate() {
	    // Setup our mock repository
	    Gender gender = new Gender(1, "Gender Name");

	    // Mocking behaviour for genderDao.save
	    doAnswer(invocation -> {
	        Gender argGender = invocation.getArgument(0);
	        return new Gender(argGender.getGenderPK() + 1, argGender.getGenderType());
	    }).when(genderDao).save(any());

	    // Mocking behaviour for getGenderByName
	    when(genderDao.findByType(anyString())).thenReturn(new Gender(2, "Gender Name"));

	    // Execute the service call
	    Gender returnedGender = genderService.createGender(gender);

	    // Assert the response
	    Assertions.assertNotNull(returnedGender, "The saved gender should not be null");
	    Assertions.assertEquals(2, returnedGender.getGenderPK(), "The ID should be incremented");
	}


	
	@Test
	@DisplayName("Test delete gender")
	void testDelete() {
	    // Setup our mock repository
	    Gender gender = new Gender(1, "Gender Name");
	    
	    doReturn(Optional.of(gender)).when(genderDao).findById(1);

	    doNothing().when(genderDao).deleteById(1);

	    // Execute the service call
	    Boolean returnedGender = genderService.deleteGender(1);

	    // Assert the response
	    Assertions.assertNotNull(returnedGender, "The deleted gender should not be null, should be boolean.");
	    
	    // After deletion, the gender should not be found.
	    doReturn(Optional.empty()).when(genderDao).findById(1);
	    Assertions.assertNull(genderService.getGenderById(1));

	    Assertions.assertEquals(true, returnedGender, "Should return true if deleted.");
	}


	
	@Test
	@DisplayName("Test edit gender")
	void testEdit() {
	    // Setup our mock repository
	    Gender gender = new Gender(1, "Gender Name");
	    
	    doReturn(Optional.of(gender)).when(genderDao).findById(1);

	    // Execute the service call
	    Gender updatedGender = new Gender(1, "Updated Gender Name");
	    genderService.updateGender(updatedGender);
	    
	    doReturn(Optional.of(updatedGender)).when(genderDao).findById(1);
	    
	    Gender returnedGender = genderService.getGenderById(1);

	    // Assert the response
	    Assertions.assertNotNull(returnedGender, "The updated gender should not be null");
	    Assertions.assertEquals(1, returnedGender.getGenderPK(), "The ID should be same as the one updated");
	    Assertions.assertEquals("Updated Gender Name", returnedGender.getGenderType(), "The Name should be updated");
	}


}
