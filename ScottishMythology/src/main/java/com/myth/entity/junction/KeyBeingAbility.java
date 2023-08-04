package com.myth.entity.junction;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class KeyBeingAbility implements Serializable {

    @Column(name = "BeingPK")
    private int beingPK;

    @Column(name = "AbilityPK")
    private int abilityPK;
    
    public KeyBeingAbility() {
    	
    }

	public KeyBeingAbility(int beingPK, int abilityPK) {
		super();
		this.beingPK = beingPK;
		this.abilityPK = abilityPK;
	}

	public int getBeingPK() {
		return beingPK;
	}

	public void setBeingPK(int beingPK) {
		this.beingPK = beingPK;
	}

	public int getAbilityPK() {
		return abilityPK;
	}

	public void setAbilityPK(int abilityPK) {
		this.abilityPK = abilityPK;
	}

    
}
