package com.myth.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.myth.entity.Being;

@Repository
public interface BeingDao extends JpaRepository<Being, Integer>{
	
	@Query("FROM Being WHERE beingName =:beingName")
    Being findByName(@Param("beingName") String beingName);
	
	@Query("FROM Being WHERE beingFaction = :beingFaction")
	List<Being> findBeingByFaction(@Param("beingFaction") int beingFaction);
	
	@Query("FROM Being WHERE beingGender = :beingGender")
	List<Being> findBeingByGender(@Param("beingGender") int beingGender);
	
	@Query("FROM Being WHERE beingSpecies = :beingSpecies")
	List<Being> findBeingBySpecies(@Param("beingSpecies") int beingSpecies);

}
