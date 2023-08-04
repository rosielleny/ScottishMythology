package com.myth.entity.junction;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class BeingSymbolism {

    @EmbeddedId
    private KeyBeingSymbolism id;
    
    
    public BeingSymbolism() {
    	
    }

	public BeingSymbolism(KeyBeingSymbolism id) {
		super();
		this.id = id;
	}

	public KeyBeingSymbolism getId() {
		return id;
	}

	public void setId(KeyBeingSymbolism id) {
		this.id = id;
	}

    
}
