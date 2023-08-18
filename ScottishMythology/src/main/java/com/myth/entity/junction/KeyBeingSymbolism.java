package com.myth.entity.junction;

import java.io.Serializable;
import java.util.Objects;

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

	@Override
	public int hashCode() {
		return Objects.hash(beingPK, symbolPK);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KeyBeingSymbolism other = (KeyBeingSymbolism) obj;
		return beingPK == other.beingPK && symbolPK == other.symbolPK;
	}

	@Override
	public String toString() {
		return "KeyBeingSymbolism [beingPK=" + beingPK + ", symbolPK=" + symbolPK + "]";
	}

	
    
}
