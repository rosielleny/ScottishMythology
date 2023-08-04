package com.myth.entity.junction;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class BeingStory {

    @EmbeddedId
    private KeyBeingStory id;
    
    
    public BeingStory() {
    	
    }

	public BeingStory(KeyBeingStory id) {
		super();
		this.id = id;
	}

	public KeyBeingStory getId() {
		return id;
	}

	public void setId(KeyBeingStory id) {
		this.id = id;
	}

    
}
