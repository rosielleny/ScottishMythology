package com.symbol.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.symbol.dao.SymbolDao;
import com.symbol.entity.Symbol;
import com.symbol.service.SymbolService;

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
class SymbolApplicationTests {

	@Test
	void contextLoads() {
	}
	
	//@Autowired
	//private SymbolService symbolService;
	@MockBean
	private SymbolDao symbolDao;
	@Autowired
    private MockMvc mockMvc;
	@Autowired
	private SymbolService symbolService;
	
	
	// Service Tests
	@Test
	@DisplayName("Test getting symbol by ID")
	void testGetByID() {
		
		// Setting up mock repository
		Symbol symbol = new Symbol(1, "TestSymbol");
		doReturn(Optional.of(symbol)).when(symbolDao).findById(1);
		
		// Execute the service call
		Optional<Symbol> returnedSymbol = Optional.ofNullable(symbolService.getSymbolById(1));
		
		// Assert the response
		Assertions.assertTrue(returnedSymbol.isPresent(), "Symbol was not found");
		Assertions.assertSame(returnedSymbol.get(), symbol, "The symbol returned was not the same as the mock");
		
	}

	@Test
	@DisplayName("Test Not Found get symbol by ID")
	void testGetByIdNotFound() {
		// Setup our mock repository
		doReturn(Optional.empty()).when(symbolDao).findById(1);

		// Execute the service call
		Optional<Symbol> returnedSymbol = Optional.ofNullable(symbolService.getSymbolById(1));

		// Assert the response
		Assertions.assertFalse(returnedSymbol.isPresent(), "Symbol should NOT be found, but was");
	}
	
	@Test
	@DisplayName("Test findAll")
	void testFindAll() {
		// Setup our mock repository
		Symbol symbol1 = new Symbol(1, "Symbol Name");
		Symbol symbol2 = new Symbol(2, "Symbol 2 Name");
		doReturn(Arrays.asList(symbol1, symbol2)).when(symbolDao).findAll();
		when(symbolDao.findAll()).thenReturn(Arrays.asList(symbol1, symbol2));

		// Execute the service call
		List<Symbol> symbols = symbolService.getAllSymbol();

		// Assert the response
		Assertions.assertEquals(2, symbols.size(), "findAll should return 2 abilities");
	}
	
	@Test
	@DisplayName("Test create symbol")
	void testCreate() {
	    // Setup our mock repository
	    Symbol symbol = new Symbol(1, "Symbol Name");

	    // Mocking behaviour for symbolDao.save
	    doAnswer(invocation -> {
	        Symbol argSymbol = invocation.getArgument(0);
	        return new Symbol(argSymbol.getSymbolPK() + 1, argSymbol.getSymbolName());
	    }).when(symbolDao).save(any());

	    // Mocking behaviour for getSymbolByName
	    when(symbolDao.findByName(anyString())).thenReturn(new Symbol(2, "Symbol Name"));

	    // Execute the service call
	    Symbol returnedSymbol = symbolService.createSymbol(symbol);

	    // Assert the response
	    Assertions.assertNotNull(returnedSymbol, "The saved symbol should not be null");
	    Assertions.assertEquals(2, returnedSymbol.getSymbolPK(), "The ID should be incremented");
	}


	
	@Test
	@DisplayName("Test delete symbol")
	void testDelete() {
	    // Setup our mock repository
	    Symbol symbol = new Symbol(1, "Symbol Name");
	    
	    doReturn(Optional.of(symbol)).when(symbolDao).findById(1);

	    doNothing().when(symbolDao).deleteById(1);

	    // Execute the service call
	    Boolean returnedSymbol = symbolService.deleteSymbol(1);

	    // Assert the response
	    Assertions.assertNotNull(returnedSymbol, "The deleted symbol should not be null, should be boolean.");
	    
	    // After deletion, the symbol should not be found.
	    doReturn(Optional.empty()).when(symbolDao).findById(1);
	    Assertions.assertNull(symbolService.getSymbolById(1));

	    Assertions.assertEquals(true, returnedSymbol, "Should return true if deleted.");
	}


	
	@Test
	@DisplayName("Test edit symbol")
	void testEdit() {
	    // Setup our mock repository
	    Symbol symbol = new Symbol(1, "Symbol Name");
	    
	    doReturn(Optional.of(symbol)).when(symbolDao).findById(1);

	    // Execute the service call
	    Symbol updatedSymbol = new Symbol(1, "Updated Symbol Name");
	    symbolService.updateSymbol(updatedSymbol);
	    
	    doReturn(Optional.of(updatedSymbol)).when(symbolDao).findById(1);
	    
	    Symbol returnedSymbol = symbolService.getSymbolById(1);

	    // Assert the response
	    Assertions.assertNotNull(returnedSymbol, "The updated symbol should not be null");
	    Assertions.assertEquals(1, returnedSymbol.getSymbolPK(), "The ID should be same as the one updated");
	    Assertions.assertEquals("Updated Symbol Name", returnedSymbol.getSymbolName(), "The Name should be updated");
	}


}
