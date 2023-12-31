package com.story.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



@Entity
public class Story {
	
//	CREATE TABLE Story(
//			StoryPK int auto_increment primary key,
//		    StoryTitle varchar(100),
//		    StoryDescription varchar(500)
//		);
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int storyPK;
	private String storyName;
	private String storyDescription;
	
	
	public Story() {
		
	}
	
	public Story(int storyPK, String storyName, String storyDescription) {
		super();
		this.storyPK = storyPK;
		this.storyName = storyName;
		this.storyDescription = storyDescription;
	}

	public int getStoryPK() {
		return storyPK;
	}
	public void setStoryPK(int storyPK) {
		this.storyPK = storyPK;
	}
	public String getStoryName() {
		return storyName;
	}
	public void setStoryName(String storyName) {
		this.storyName = storyName;
	}

	public String getStoryDescription() {
		return storyDescription;
	}

	public void setStoryDescription(String storyDescription) {
		this.storyDescription = storyDescription;
	}

	
}
