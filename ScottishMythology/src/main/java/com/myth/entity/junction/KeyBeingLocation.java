package com.myth.entity.junction;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class KeyBeingLocation implements Serializable {

    @Column(name = "BeingPK")
    private int beingPK;

    @Column(name = "LocationPK")
    private int locationPK;
    
    public KeyBeingLocation() {
    	
    }

	public KeyBeingLocation(int beingPK, int locationPK) {
		super();
		this.beingPK = beingPK;
		this.locationPK = locationPK;
	}

	public int getBeingPK() {
		return beingPK;
	}

	public void setBeingPK(int beingPK) {
		this.beingPK = beingPK;
	}

	public int getLocationPK() {
		return locationPK;
	}

	public void setLocationPK(int locationPK) {
		this.locationPK = locationPK;
	}

	@Override
	public int hashCode() {
		return Objects.hash(beingPK, locationPK);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KeyBeingLocation other = (KeyBeingLocation) obj;
		return beingPK == other.beingPK && locationPK == other.locationPK;
	}

	@Override
	public String toString() {
		return "KeyBeingLocation [beingPK=" + beingPK + ", locationPK=" + locationPK + "]";
	}

    
}
