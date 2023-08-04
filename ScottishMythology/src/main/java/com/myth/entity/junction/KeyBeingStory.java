package com.myth.entity.junction;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class KeyBeingStory implements Serializable {

    @Column(name = "BeingPK")
    private int beingPK;

    @Column(name = "StoryPK")
    private int storyPK;
    
    public KeyBeingStory() {
    	
    }

	public KeyBeingStory(int beingPK, int storyPK) {
		super();
		this.beingPK = beingPK;
		this.storyPK = storyPK;
	}

	public int getBeingPK() {
		return beingPK;
	}

	public void setBeingPK(int beingPK) {
		this.beingPK = beingPK;
	}

	public int getStoryPK() {
		return storyPK;
	}

	public void setStoryPK(int storyPK) {
		this.storyPK = storyPK;
	}

    
}
