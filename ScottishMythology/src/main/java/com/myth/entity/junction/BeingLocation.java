package com.myth.entity.junction;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class BeingLocation {

    @EmbeddedId
    private KeyBeingLocation id;
    
    
    public BeingLocation() {
    	
    }

	public BeingLocation(KeyBeingLocation id) {
		super();
		this.id = id;
	}

	public KeyBeingLocation getId() {
		return id;
	}

	public void setId(KeyBeingLocation id) {
		this.id = id;
	}

    
}
