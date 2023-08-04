package com.myth.dao.junction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.myth.entity.junction.BeingWeakness;
import com.myth.entity.junction.KeyBeingWeakness;

@Repository
public interface BeingWeaknessDao extends JpaRepository<BeingWeakness, KeyBeingWeakness>{

	@Query("FROM BeingWeakness WHERE beingPK = :beingPK")
    List<BeingWeakness> findByBeing(@Param("beingPK") int beingPK);
	@Query("FROM BeingWeakness WHERE weaknessPK = :weaknessPK")
    List<BeingWeakness> findByWeakness(@Param("weaknessPK") int weaknessPK);
}
