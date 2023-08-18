package com.myth.entity.junction;

import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class BeingStory {

    @EmbeddedId
    private KeyBeingStory id;
    
    
    public BeingStory() {
    	
    }

	public BeingStory(KeyBeingStory id) {
		super();
		this.id = id;
	}

	public KeyBeingStory getId() {
		return id;
	}

	public void setId(KeyBeingStory id) {
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
		BeingStory other = (BeingStory) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "BeingStory [id=" + id + "]";
	}

    
}
