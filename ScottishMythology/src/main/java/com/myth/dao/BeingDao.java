package com.myth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.myth.entity.Being;

@Repository
public interface BeingDao extends JpaRepository<Being, Integer>{
	
	@Query("FROM Being WHERE beingName LIKE %:beingName%")
    Being findByName(@Param("beingName") String beingName);

}
