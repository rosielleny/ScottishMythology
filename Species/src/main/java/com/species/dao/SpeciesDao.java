package com.species.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.species.entity.Species;

@Repository
public interface SpeciesDao extends JpaRepository<Species, Integer>{

	
	@Query("FROM Species WHERE speciesName = :speciesName")
    Species findByName(@Param("speciesName") String speciesName);
}
