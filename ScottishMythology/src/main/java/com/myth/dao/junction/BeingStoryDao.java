package com.myth.dao.junction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.myth.entity.junction.BeingStory;
import com.myth.entity.junction.KeyBeingStory;

@Repository
public interface BeingStoryDao extends JpaRepository<BeingStory, KeyBeingStory>{
	
	@Query("FROM BeingStory WHERE beingPK = :beingPK")
    List<BeingStory> findByBeing(@Param("beingPK") int beingPK);
	@Query("FROM BeingStory WHERE storyPK = :storyPK")
    List<BeingStory> findByStory(@Param("storyPK") int storyPK);

}
