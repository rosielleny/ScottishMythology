package com.story.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.story.entity.Story;

@Repository
public interface StoryDao extends JpaRepository<Story, Integer>{

	
	@Query("FROM Story WHERE storyName = :storyName")
    Story findByName(@Param("storyName") String storyName);
}
