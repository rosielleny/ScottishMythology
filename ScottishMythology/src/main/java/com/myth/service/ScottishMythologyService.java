package com.myth.service;

import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import com.myth.entity.Being;
import com.myth.entity.composite.BeingComposite;

public interface ScottishMythologyService {
	
	// Create
	BeingComposite createBeingRecord(BeingComposite beingComposite);

	// Read
	List<BeingComposite> getAllBeingRecords();
	BeingComposite getBeingRecordById(int beingPK);
	BeingComposite getBeingRecordByName(String beingName);
	List<BeingComposite> getBeingRecordByFaction(int factionPK);
	List<BeingComposite> getBeingRecordByGender(int genderPK);
	List<BeingComposite> getBeingRecordBySpecies(int speciesPK);

	// Update
	Boolean updateBeingRecord(BeingComposite beingComposite);
	
	// Delete
	Boolean deleteBeingRecord(int beingComposite);

	// Links for controllers
	ModelAndView setUpLinks(String entity, String entities, ModelAndView modelAndView);


	


}
