package com.myth.entity.junction;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class KeyBeingSymbolism implements Serializable {

    @Column(name = "BeingPK")
    private int beingPK;

    @Column(name = "SymbolPK")
    private int symbolPK;
    
    public KeyBeingSymbolism() {
    	
    }

	public KeyBeingSymbolism(int beingPK, int symbolPK) {
		super();
		this.beingPK = beingPK;
		this.symbolPK = symbolPK;
	}

	public int getBeingPK() {
		return beingPK;
	}

	public void setBeingPK(int beingPK) {
		this.beingPK = beingPK;
	}

	public int getSymbolPK() {
		return symbolPK;
	}

	public void setSymbolPK(int symbolPK) {
		this.symbolPK = symbolPK;
	}

    
}
