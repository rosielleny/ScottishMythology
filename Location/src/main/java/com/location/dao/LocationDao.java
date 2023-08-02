package com.location.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.location.entity.Location;

@Repository
public interface LocationDao extends JpaRepository<Location, Integer>{

	
	@Query("FROM Location WHERE locationName = :locationName")
    Location findByName(@Param("locationName") String locationName);
}
