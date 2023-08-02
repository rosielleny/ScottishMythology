package com.ability.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ability.entity.Ability;

@Repository
public interface AbilityDao extends JpaRepository<Ability, Integer>{

	
	@Query("FROM Ability WHERE abilityName = :abilityName")
    Ability findByName(@Param("abilityName") String abilityName);
}
