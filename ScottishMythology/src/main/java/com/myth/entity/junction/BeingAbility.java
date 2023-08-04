package com.myth.entity.junction;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class BeingAbility {

    @EmbeddedId
    private KeyBeingAbility id;
    
    
    public BeingAbility() {
    	
    }

	public BeingAbility(KeyBeingAbility id) {
		super();
		this.id = id;
	}

	public KeyBeingAbility getId() {
		return id;
	}

	public void setId(KeyBeingAbility id) {
		this.id = id;
	}

    
}
