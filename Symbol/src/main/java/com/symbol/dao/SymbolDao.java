package com.symbol.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.symbol.entity.Symbol;

@Repository
public interface SymbolDao extends JpaRepository<Symbol, Integer>{

	
	@Query("FROM Symbol WHERE symbolName = :symbolName")
    Symbol findByName(@Param("symbolName") String symbolName);
}
