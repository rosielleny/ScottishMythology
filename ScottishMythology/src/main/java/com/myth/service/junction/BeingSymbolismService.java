package com.myth.service.junction;

import java.util.List;

import com.myth.entity.junction.BeingSymbolism;
import com.myth.entity.junction.KeyBeingSymbolism;

public interface BeingSymbolismService{

	// Create
	BeingSymbolism createBeingSymbolism(BeingSymbolism beingSymbolism);

	// Read
	List<BeingSymbolism> getAllBeingSymbolism();
	BeingSymbolism getBeingSymbolismById(KeyBeingSymbolism beingSymbolismPK);
	List<BeingSymbolism> getBeingSymbolismByBeingId(int beingPK);
	List<BeingSymbolism> getBeingSymbolismBySymbolismId(int symbolPK);

	// Update
	Boolean updateBeingSymbolism(BeingSymbolism beingSymbolism);

	// Delete
	Boolean deleteBeingSymbolism(KeyBeingSymbolism beingSymbolismPK);

}
