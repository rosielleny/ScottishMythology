package com.myth.service;

import java.util.List;

import com.myth.entity.Being;
import com.myth.entity.composite.BeingComposite;

public interface ScottishMythologyService {
	
	// Create
	BeingComposite createBeingRecord(BeingComposite beingComposite);

	// Read
	List<BeingComposite> getAllBeingRecords();
	BeingComposite getBeingRecordById(int beingPK);
	BeingComposite getBeingRecordByName(String beingName);

	// Update
	Boolean updateBeingRecord(BeingComposite beingComposite);
	
	// Delete
	Boolean deleteBeingRecord(int beingComposite);

	


}
