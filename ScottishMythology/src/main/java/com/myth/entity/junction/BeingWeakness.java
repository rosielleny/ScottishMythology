package com.myth.entity.junction;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class BeingWeakness {

    @EmbeddedId
    private KeyBeingWeakness id;
    
    
    public BeingWeakness() {
    	
    }

	public BeingWeakness(KeyBeingWeakness id) {
		super();
		this.id = id;
	}

	public KeyBeingWeakness getId() {
		return id;
	}

	public void setId(KeyBeingWeakness id) {
		this.id = id;
	}

    
}
