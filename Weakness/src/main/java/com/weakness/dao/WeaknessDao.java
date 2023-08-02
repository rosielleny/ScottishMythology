package com.weakness.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.weakness.entity.Weakness;

@Repository
public interface WeaknessDao extends JpaRepository<Weakness, Integer>{

	
	@Query("FROM Weakness WHERE weaknessName = :weaknessName")
    Weakness findByName(@Param("weaknessName") String weaknessName);
}
