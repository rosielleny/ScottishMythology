package com.symbol.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.symbol.entity.Symbol;
import com.symbol.service.SymbolService;


@Controller
public class SymbolResource {
	
	@Autowired
	private SymbolService symbolService;
	
	// Create
	@PostMapping(path = "/symbol/new-symbol", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Symbol> createSymbol(@RequestBody Symbol symbol) {

		Symbol newSymbol = symbolService.createSymbol(symbol);

		if (newSymbol != null) {
			return new ResponseEntity<>(newSymbol, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}


	// Read
	// Get by ID
	@CrossOrigin
	@GetMapping(path = "/symbol/{symbolPK}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Symbol> getSymbolById(@PathVariable int symbolPK){

		Symbol symbol = symbolService.getSymbolById(symbolPK);

		if (symbol != null) {
			return new ResponseEntity<Symbol>(symbol, HttpStatus.OK);
		}else {
			return new ResponseEntity<Symbol>(symbol, HttpStatus.NO_CONTENT);
		}
	}
	
	// Read
	// Get by Name
	@CrossOrigin
	@GetMapping(path = "/symbol/symbol-by-name/{symbolName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Symbol> getSymbolByName(@PathVariable String symbolName){

		Symbol symbol = symbolService.getSymbolByName(symbolName);

		if (symbol != null) {
			return new ResponseEntity<Symbol>(symbol, HttpStatus.OK);
		}else {
			return new ResponseEntity<Symbol>(symbol, HttpStatus.NO_CONTENT);
		}
	}

	// Read
	// Get all
	@GetMapping(path = "/symbol/all-symbol", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Symbol>> getAllSymbol(){

		List<Symbol> symbolList = symbolService.getAllSymbol();

		if(symbolList.size() > 0) {
			return new ResponseEntity<List<Symbol>>(symbolList, HttpStatus.OK);
		}else {
			return new ResponseEntity<List<Symbol>>(symbolList, HttpStatus.NO_CONTENT);
		}
	}
	
	// Update
	@PutMapping(path = "/symbol/update-symbol/{symbolPK}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Symbol> updateSymbol(@PathVariable int symbolPK, @RequestBody Symbol symbol){

		boolean updated = symbolService.updateSymbol(symbol);
		Symbol symbolUpdated = symbolService.getSymbolById(symbolPK);
		if(updated) {
			return new ResponseEntity<Symbol>(symbol, HttpStatus.OK);
		}else {
			return new ResponseEntity<Symbol>(symbol, HttpStatus.NO_CONTENT);
		}

	}

	// Delete
	@DeleteMapping(path = "/symbol/delete-symbol/{symbolPK}")
	public ResponseEntity<Boolean> deleteSymbol(@PathVariable int symbolPK){

		boolean deleted = symbolService.deleteSymbol(symbolPK);

		if(deleted) {
			return new ResponseEntity<Boolean>(deleted, HttpStatus.OK);
		}else {
			return new ResponseEntity<Boolean>(deleted, HttpStatus.NO_CONTENT);
		}
	}
}
