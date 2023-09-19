package com.myth.entity.composite;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GenericEntity {
	// A generic entity class used for the purpose of creating reusable code.
	// Passing one of the other entities into this one will store the information with these variable names
	// In this way, code for GenericEntity can be written once and used for any entity type as long as it is passed through generic entity once
	private int entityPK;
	@NotNull
	@Size(max = 20, min = 1)
	@NotBlank
	private String entityName;
	@Size(max = 255)
	private String entityDescription;
	
	
	public boolean hasDescription() {
        return entityDescription != null && !entityDescription.isEmpty();
    }
	
	public GenericEntity() {
		
	}
	
	public GenericEntity(int entityPK, @NotNull @Size(max = 20) String entityName,
			@Size(max = 255) @NotNull String entityDescription) {
		super();
		this.entityPK = entityPK;
		this.entityName = entityName;
		this.entityDescription = entityDescription;
	}

	public GenericEntity(int entityPK, String entityName) {
		
		this.entityPK = entityPK;
		this.entityName = entityName;
		this.entityDescription = null;
	}
	
	public int getEntityPK() {
		return entityPK;
	}
	public void setEntityPK(int entityPK) {
		this.entityPK = entityPK;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getEntityDescription() {
		return entityDescription;
	}
	public void setEntityDescription(String entityDescription) {
		this.entityDescription = entityDescription;
	}
	
	
}
