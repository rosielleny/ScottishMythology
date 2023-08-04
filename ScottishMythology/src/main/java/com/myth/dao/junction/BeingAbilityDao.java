package com.myth.dao.junction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.myth.entity.junction.BeingAbility;
import com.myth.entity.junction.KeyBeingAbility;


@Repository
public interface BeingAbilityDao extends JpaRepository<BeingAbility, KeyBeingAbility>{
	
	@Query("FROM BeingAbility WHERE beingPK = :beingPK")
    List<BeingAbility> findByBeing(@Param("beingPK") int beingPK);
	@Query("FROM BeingAbility WHERE abilityPK = :abilityPK")
    List<BeingAbility> findByAbility(@Param("abilityPK") int abilityPK);

}
