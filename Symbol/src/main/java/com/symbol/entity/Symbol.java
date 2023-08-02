package com.symbol.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Symbol {
	
//	CREATE TABLE Symbol(
//			SymbolPK int auto_increment primary key,
//		    SymbolName varchar(20)
//		);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int symbolPK;
	private String symbolName;
	
	
	public Symbol() {
		
	}
	
	public Symbol(int symbolPK, String symbolName) {
		super();
		this.symbolPK = symbolPK;
		this.symbolName = symbolName;
	}
	
	public int getSymbolPK() {
		return symbolPK;
	}
	public void setSymbolPK(int symbolPK) {
		this.symbolPK = symbolPK;
	}
	public String getSymbolName() {
		return symbolName;
	}
	public void setSymbolName(String symbolName) {
		this.symbolName = symbolName;
	}
	
	
}
