package com.symbol.client;

import org.assertj.core.util.Lists;
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

import com.symbol.dao.SymbolDao;
import com.symbol.entity.Symbol;
import com.symbol.service.SymbolService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
class SymbolControllerTests {

	@Test
	void contextLoads() {
	}
	
	//@Autowired
	//private SymbolService symbolService;
	@MockBean
	private SymbolDao symbolDao;
	@Autowired
    private MockMvc mockMvc;
	@MockBean
	private SymbolService symbolService;
	
	
	// Controller Tests
	
    @Test
    @DisplayName("GET /symbol/all-symbol success") 
    void testGetSymbolsSuccess() throws Exception {
        // Setup our mocked service
        Symbol symbol1 = new Symbol(1, "Symbol Name");
        Symbol symbol2 = new Symbol(2, "Symbol 2 Name");
        doReturn(Lists.newArrayList(symbol1, symbol2)).when(symbolService).getAllSymbol();

        // Execute the GET request
        mockMvc.perform(get("/symbol/all-symbol"))
        // Validate the response code and content type
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        // Validate the returned fields
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].symbolPK", is(1)))
        .andExpect(jsonPath("$[0].symbolName", is("Symbol Name")))
        .andExpect(jsonPath("$[1].symbolPK", is(2)))
        .andExpect(jsonPath("$[1].symbolName", is("Symbol 2 Name")));
    }
	
    
    @Test
    @DisplayName("GET /symbol/1")
    void testGetSymbolById() throws Exception {
    	
    	// Setup our mocked service
        Symbol symbol = new Symbol(1, "Symbol Name");
        doReturn(symbol).when(symbolService).getSymbolById(1);

        // Execute the GET request
        mockMvc.perform(get("/symbol/{symbolPK}", 1))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.symbolPK", is(1)))
                .andExpect(jsonPath("$.symbolName", is("Symbol Name")));
    }
    
    @Test
    @DisplayName("GET /symbol/1 - Not Found")
    void testGetSymbolByIdNotFound() throws Exception {
        // Setup our mocked service
    	doReturn(null).when(symbolService).getSymbolById(1);

        // Execute the GET request
        mockMvc.perform(get("/symbol/{symbolPK}", 1))
                // Validate the response code
                .andExpect(status().isNoContent());
    }
    
    @Test
    @DisplayName("/symbol/new-symbol")
    void testCreateSymbol() throws Exception {
        // Setup our mocked service
        Symbol symbolToPost = new Symbol(0, "New Symbol");
        Symbol symbolToReturn = new Symbol(1, "New Symbol");
        doReturn(symbolToReturn).when(symbolService).createSymbol(any());

        // Execute the POST request
        mockMvc.perform(post("/symbol/new-symbol")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(symbolToPost)))

                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.symbolPK", is(1)))
                .andExpect(jsonPath("$.symbolName", is("New Symbol")));
    }
    
    @Test
    @DisplayName("PUT /symbol/update-symbol/{symbolPK}")
    void testUpdateSymbol() throws Exception {
        // Setup our mocked service
        Symbol originalSymbol = new Symbol(1, "Original Symbol");
        Symbol updatedSymbol = new Symbol(1, "Updated Symbol");
        doReturn(originalSymbol).when(symbolService).getSymbolById(1);
        doReturn(true).when(symbolService).updateSymbol(any());

        // Execute the PUT request
        mockMvc.perform(put("/symbol/update-symbol/{symbolPK}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedSymbol)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.symbolPK", is(1)))
                .andExpect(jsonPath("$.symbolName", is("Updated Symbol")));
    }
    
    @Test
    @DisplayName("PUT /symbol/update-symbol/{symbolPK} - Not Found")
    void testUpdateSymbolNotFound() throws Exception {
        // Setup our mocked service
        Symbol symbolToPut = new Symbol(1, "New Symbol");
        
        doReturn(null).when(symbolService).getSymbolById(1);
        doReturn(false).when(symbolService).updateSymbol(symbolToPut);

        // Execute the PUT request
        mockMvc.perform(put("/symbol/update-symbol/{symbolPK}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(symbolToPut)))
                // Validate the response code and content type
                .andExpect(status().isNoContent());
    }

    
    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
