package com.myth.entity.junction;

import java.util.Objects;

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
		BeingWeakness other = (BeingWeakness) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "BeingWeakness [id=" + id + "]";
	}

	
    
}
