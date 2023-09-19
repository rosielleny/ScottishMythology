package com.myth.client;

import org.aspectj.lang.annotation.Before;
import org.hamcrest.Matchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper; // Import ObjectMapper

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.myth.controller.SymbolController;
import com.myth.entity.Symbol;
import com.myth.entity.composite.GenericEntity;

import com.myth.service.micro.SymbolService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.mockito.Mockito.*;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
class SymbolControllerTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private MockMvc mockMvc;


	@MockBean
	private SymbolService symbolService;
	
	@Autowired
	private SymbolController symbolController;
	
	// CREATE
	@Test
	public void testCreateAnSymbol() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/symbol/create"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("entity/create-entity"))
				.andReturn();

		ModelAndView modelAndView = result.getModelAndView();

		Object entityAttribute = modelAndView.getModel().get("entity");
		Object entitiesAttribute = modelAndView.getModel().get("entities");

		Assertions.assertNotNull(entityAttribute, "The 'entity' attribute should not be null");
		Assertions.assertNotNull(entitiesAttribute, "The 'entities' attribute should not be null");
	}



	@Test
	@DisplayName("Test form validation failure")
	public void testFormValidationFailure() throws Exception {
		Symbol createdSymbol = new Symbol();
		createdSymbol.setSymbolPK(1);
		createdSymbol.setSymbolName("New Symbol");
		when(symbolService.getSymbolById(any(Integer.class))).thenReturn(createdSymbol);
		when(symbolService.createSymbol(any(Symbol.class))).thenReturn(createdSymbol);
		when(symbolService.getSymbolByName(any(String.class))).thenReturn(createdSymbol);

		mockMvc.perform(post("/symbol/save-new")
				.param("entityName", "") // Invalid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attributeHasFieldErrors("entity", "entityName")) // Expect validation error
		.andExpect(view().name("entity/create-entity")); // Expect view name
	}

	@Test
	@DisplayName("Test successful symbol creation")
	public void testSuccessfulSymbolCreation() throws Exception {
		Symbol createdSymbol = new Symbol();
		createdSymbol.setSymbolPK(1);
		createdSymbol.setSymbolName("New Symbol");
		GenericEntity genEnt = new GenericEntity(createdSymbol.getSymbolPK(), createdSymbol.getSymbolName());
		when(symbolService.getSymbolById(any(Integer.class))).thenReturn(createdSymbol);
		when(symbolService.createSymbol(any(Symbol.class))).thenReturn(createdSymbol);
		when(symbolService.getSymbolByName(any(String.class))).thenReturn(createdSymbol);

		mockMvc.perform(post("/symbol/save-new")
				.param("entityName", "New Symbol") // Valid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attribute("message", "New Symbol successfully added to the database.")) // Expect success message
		.andExpect(model().attribute("entity", samePropertyValuesAs(genEnt))) // Expect entity attribute to be updated
		.andExpect(view().name("entity/entity-output")); // Expect view name
	}

	@Test
	@DisplayName("Test error handling")
	public void testErrorHandling() throws Exception {
		// Mock an error during symbol creation
		when(symbolService.createSymbol(any(Symbol.class))).thenReturn(null);

		// Create a POST request with valid input
		mockMvc.perform(post("/symbol/save-new")
				.param("entityName", "New Symbol") // Valid input
				)
		.andExpect(status().isOk()) // Expect HTTP 200 status
		.andExpect(model().attribute("message", "An error occurred. Symbol not added to the database.")) // Expect error message
		.andExpect(view().name("entity/entity-output")); // Expect view name
	}
	
	// READ
	
    @Test
    @DisplayName("Test show all symbols")
    public void testShowAllAbilities() throws Exception {
        // Mock your symbolService to return a list of Symbol objects
        List<Symbol> symbols = new ArrayList<>();
        Symbol symbol1 = new Symbol(1, "Symbol 1");
        Symbol symbol2 = new Symbol(2, "Symbol 2");
        symbols.add(symbol2);
        symbols.add(symbol1);
        
        when(symbolService.getAllSymbol()).thenReturn(symbols);

        mockMvc.perform(get("/symbol/symbolism"))
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-all-entities")) // Expect view name
            .andExpect(model().attributeExists("entityList")) // Expect model attribute
            .andExpect(model().attribute("entityList", Matchers.hasSize(2))); // Expect entityList size to match the number of symbols
    }
    
    @Test
    @DisplayName("Test search symbols by name")
    public void testSearchAbilitiesByName() throws Exception {
        Symbol mockSymbol = new Symbol();
        mockSymbol.setSymbolPK(1);
        mockSymbol.setSymbolName("Mocked Symbol");
        
        when(symbolService.getSymbolByName("ExistingSymbol")).thenReturn(mockSymbol);
        when(symbolService.getSymbolByName("NonExistingSymbol")).thenReturn(null);
        
        mockMvc.perform(get("/symbol/search")
                .param("name", "ExistingSymbol")) // Valid input for an existing symbol
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-entity")) // Expect view name
            .andExpect(model().attributeExists("entity")) // Expect model attribute
            .andExpect(model().attribute("entity", Matchers.hasProperty("entityName", is("Mocked Symbol")))); // Expect entity attribute

        mockMvc.perform(get("/symbol/search")
                .param("name", "NonExistingSymbol")) // Valid input for a non-existing symbol
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/show-entity")) // Expect view name
            .andExpect(model().attribute("message", is("No symbols were found for NonExistingSymbol"))); // Expect message attribute
    }
    
    // UPDATE
    
    @Test
    @DisplayName("Test edit symbols by ID")
    public void testEditAbilitiesById() throws Exception {
        Symbol mockSymbol = new Symbol();
        mockSymbol.setSymbolPK(1);
        mockSymbol.setSymbolName("Mocked Symbol");
        
        when(symbolService.getSymbolById(1)).thenReturn(mockSymbol);
        
        mockMvc.perform(get("/symbol/edit")
                .param("pk", "1")) // Valid input for an existing symbol
            .andExpect(status().isOk()) // Expect HTTP 200 status
            .andExpect(view().name("entity/edit-entity")) // Expect view name
            .andExpect(model().attributeExists("entity")) // Expect model attribute
            .andExpect(model().attributeExists("ogEntity")) // Expect model attribute
            .andExpect(model().attribute("entity", Matchers.hasProperty("entityName", is("Mocked Symbol")))); // Expect entity attribute
    }
    
    // DELETE
    
    @Test
    @DisplayName("Test successful symbol deletion")
    public void testSuccessfulSymbolDeletion() throws Exception {
        int symbolIdToDelete = 1;
        Symbol deletedSymbol = new Symbol();
        deletedSymbol.setSymbolPK(symbolIdToDelete);
        deletedSymbol.setSymbolName("Deleted Symbol");
        List<Symbol> symbols = new ArrayList<>();
        Symbol symbol1 = new Symbol(3, "Symbol 3");
        Symbol symbol2 = new Symbol(2, "Symbol 2");
        symbols.add(symbol2);
        symbols.add(symbol1);

        when(symbolService.getSymbolById(symbolIdToDelete)).thenReturn(deletedSymbol);
        when(symbolService.deleteSymbol(symbolIdToDelete)).thenReturn(true);
        when(symbolService.getAllSymbol()).thenReturn(symbols);


        mockMvc.perform(get("/symbol/delete/{pk}", symbolIdToDelete))
            .andExpect(status().isOk())
            .andExpect(model().attribute("message", "Deleted Symbol successfully deleted from Symbolism."))
            .andExpect(view().name("entity/show-all-entities"));

        verify(symbolService, times(1)).getSymbolById(symbolIdToDelete);
        verify(symbolService, times(1)).deleteSymbol(symbolIdToDelete);
    }

    @Test
    @DisplayName("Test unsuccessful symbol deletion")
    public void testUnsuccessfulSymbolDeletion() throws Exception {
        int symbolIdToDelete = 1;
        Symbol symbolToDelete = new Symbol();
        symbolToDelete.setSymbolPK(symbolIdToDelete);
        symbolToDelete.setSymbolName("Deletion Failure");
        List<Symbol> symbols = new ArrayList<>();
        Symbol symbol1 = new Symbol(3, "Symbol 3");
        Symbol symbol2 = new Symbol(2, "Symbol 2");
        symbols.add(symbol2);
        symbols.add(symbol1);
        symbols.add(symbolToDelete);
        

        when(symbolService.getSymbolById(symbolIdToDelete)).thenReturn(symbolToDelete);
        when(symbolService.deleteSymbol(symbolIdToDelete)).thenReturn(false);
        when(symbolService.getAllSymbol()).thenReturn(symbols);

        mockMvc.perform(get("/symbol/delete/{pk}", symbolIdToDelete))
            .andExpect(status().isOk())
            .andExpect(model().attribute("message", "Failed to delete Deletion Failure from Symbolism."))
            .andExpect(view().name("entity/show-all-entities"));

        // Verify that the service method was called with the correct parameter
        verify(symbolService, times(1)).getSymbolById(symbolIdToDelete);
        verify(symbolService, times(1)).deleteSymbol(symbolIdToDelete);
    }

    // EXTRA

    @Test
    public void testSneakyAddSymbol_Success() {
        // Create a sample Symbol object for testing
        Symbol symbol = new Symbol();
        symbol.setSymbolName("Test Symbol");

        when(symbolService.createSymbol(symbol)).thenReturn(symbol);

        // Make a POST request to the endpoint
        ResponseEntity<Map<String, Boolean>> responseEntity = symbolController.sneakyAddSymbol(symbol);

        // Verify that the response is as expected
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().get("success"));
    }

    @Test
    public void testSneakyAddSymbol_Failure() {
        // Create a sample Symbol object for testing
        Symbol symbol = new Symbol();
        symbol.setSymbolName("Test Symbol");

        when(symbolService.createSymbol(symbol)).thenReturn(null);

        // Make a POST request to the endpoint
        ResponseEntity<Map<String, Boolean>> responseEntity = symbolController.sneakyAddSymbol(symbol);

        // Verify that the response is as expected
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertFalse(responseEntity.getBody().get("success"));
    }
    
    public static String asJsonString(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
