package com.myth.entity.junction;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class KeyBeingWeakness implements Serializable {

    @Column(name = "BeingPK")
    private int beingPK;

    @Column(name = "WeaknessPK")
    private int weaknessPK;
    
    public KeyBeingWeakness() {
    	
    }

	public KeyBeingWeakness(int beingPK, int weaknessPK) {
		super();
		this.beingPK = beingPK;
		this.weaknessPK = weaknessPK;
	}

	public int getBeingPK() {
		return beingPK;
	}

	public void setBeingPK(int beingPK) {
		this.beingPK = beingPK;
	}

	public int getWeaknessPK() {
		return weaknessPK;
	}

	public void setWeaknessPK(int weaknessPK) {
		this.weaknessPK = weaknessPK;
	}

    
}
