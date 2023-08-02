package com.symbol.service;

import java.util.List;

import com.symbol.entity.Symbol;

public interface SymbolService {
	
	// Create
	Symbol createSymbol(Symbol symbol);
	
	// Read
	List<Symbol> getAllSymbol();
	Symbol getSymbolById(int symbolPK);
	Symbol getSymbolByName(String symbolName);
	
	// Update
	Boolean updateSymbol(Symbol symbol);
	
	// Delete
	Boolean deleteSymbol(int symbolPK);
	

}
