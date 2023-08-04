package com.myth.dao.junction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.myth.entity.junction.BeingSymbolism;
import com.myth.entity.junction.KeyBeingSymbolism;

@Repository
public interface BeingSymbolismDao extends JpaRepository<BeingSymbolism, KeyBeingSymbolism>{

	
	@Query("FROM BeingSymbol WHERE beingPK = :beingPK")
    List<BeingSymbolism> findByBeing(@Param("beingPK") int beingPK);
	@Query("FROM BeingSymbol WHERE symbolPK = :symbolPK")
    List<BeingSymbolism> findBySymbol(@Param("symbolPK") int symbolPK);
	
}
