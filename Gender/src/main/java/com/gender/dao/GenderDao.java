package com.gender.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gender.entity.Gender;

@Repository
public interface GenderDao extends JpaRepository<Gender, Integer>{

	
	@Query("FROM Gender WHERE genderType = :genderType")
    Gender findByType(@Param("genderType") String genderType);
}
