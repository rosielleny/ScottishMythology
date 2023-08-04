package com.myth.dao.junction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.myth.entity.junction.BeingLocation;

import com.myth.entity.junction.KeyBeingLocation;

@Repository
public interface BeingLocationDao extends JpaRepository<BeingLocation, KeyBeingLocation>{

	@Query("FROM BeingLocation WHERE beingPK = :beingPK")
	List<BeingLocation> findByBeing(@Param("beingPK") int beingPK);
	@Query("FROM BeingLocation WHERE locationPK = :locationPK")
	List<BeingLocation> findByLocation(@Param("locationPK") int locationPK);

}
