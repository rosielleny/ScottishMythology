package com.faction.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.faction.entity.Faction;

@Repository
public interface FactionDao extends JpaRepository<Faction, Integer>{

	
	@Query("FROM Faction WHERE factionName = :factionName")
    Faction findByName(@Param("factionName") String factionName);
}
