package com.symbol.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.symbol.entity.Symbol;
import com.symbol.dao.SymbolDao;
import com.symbol.entity.Symbol;

@Service
public class SymbolServiceImpl implements SymbolService {

	@Autowired
	private SymbolDao symbolDao;
	
	// Create
	
	@Override
	public Symbol createSymbol(Symbol symbol) {
		
		if(symbolDao.save(symbol) != null) {
			Symbol createdSymbol = getSymbolByName(symbol.getSymbolName());
			return createdSymbol;
		} 
		else {
			return null;
		}
		
	}
	
	//Read

	@Override
	public List<Symbol> getAllSymbol() {
		
		return symbolDao.findAll();
	}

	@Override
	public Symbol getSymbolById(int symbolPK) {
		
		Symbol symbol = symbolDao.findById(symbolPK).orElse(null);
		return symbol;
	}

	@Override
	public Symbol getSymbolByName(String symbolName) {
		
		return symbolDao.findByName(symbolName);
	}

	// Update
	
	@Override
	public Boolean updateSymbol(Symbol symbol) {
		
		Symbol symbolExists = getSymbolById(symbol.getSymbolPK());
		
		if(symbolExists !=null) {
			
			symbolDao.save(symbol);
			return true;
			
		}
		return false;
	}

	// Delete
	
	@Override
	public Boolean deleteSymbol(int symbolPK) {
		
		Symbol symbol = getSymbolById(symbolPK);
		
		if(symbol !=null) {
			
			symbolDao.deleteById(symbolPK);
			return true;
			
		}
		return false;
	}

}
