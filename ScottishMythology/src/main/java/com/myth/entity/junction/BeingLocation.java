package com.myth.entity.junction;

import java.util.Objects;

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

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BeingLocation other = (BeingLocation) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "BeingLocation [id=" + id + "]";
	}

    
}
