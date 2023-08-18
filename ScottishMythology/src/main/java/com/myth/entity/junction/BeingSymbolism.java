package com.myth.entity.junction;

import java.util.Objects;

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
		BeingSymbolism other = (BeingSymbolism) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "BeingSymbolism [id=" + id + "]";
	}

    
	
}
