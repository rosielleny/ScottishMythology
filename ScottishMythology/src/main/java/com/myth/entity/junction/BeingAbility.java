package com.myth.entity.junction;

import java.util.Objects;

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

	@Override
	public String toString() {
		return "BeingAbility [id=" + id + "]";
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
		BeingAbility other = (BeingAbility) obj;
		return Objects.equals(id, other.id);
	}

	
    
}
