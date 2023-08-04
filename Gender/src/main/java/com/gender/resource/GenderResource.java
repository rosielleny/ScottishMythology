package com.gender.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.gender.entity.Gender;
import com.gender.service.GenderService;


@Controller
public class GenderResource {
	
	@Autowired
	private GenderService genderService;
	
	// Create
	@PostMapping(path = "/gender/new-gender", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Gender> createGender(@RequestBody Gender gender) {

		Gender newGender = genderService.createGender(gender);

		if (newGender != null) {
			return new ResponseEntity<>(newGender, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}


	// Read
	// Get by ID
	@CrossOrigin
	@GetMapping(path = "/gender/{genderPK}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Gender> getGenderById(@PathVariable int genderPK){

		Gender gender = genderService.getGenderById(genderPK);

		if (gender != null) {
			return new ResponseEntity<Gender>(gender, HttpStatus.OK);
		}else {
			return new ResponseEntity<Gender>(gender, HttpStatus.NO_CONTENT);
		}
	}
	
	// Read
	// Get by Type
	@CrossOrigin
	@GetMapping(path = "/gender/gender-by-type/{genderType}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Gender> getGenderByType(@PathVariable String genderType){

		Gender gender = genderService.getGenderByType(genderType);

		if (gender != null) {
			return new ResponseEntity<Gender>(gender, HttpStatus.OK);
		}else {
			return new ResponseEntity<Gender>(gender, HttpStatus.NO_CONTENT);
		}
	}

	// Read
	// Get all
	@GetMapping(path = "/gender/all-gender", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Gender>> getAllGender(){

		List<Gender> genderList = genderService.getAllGender();

		if(genderList.size() > 0) {
			return new ResponseEntity<List<Gender>>(genderList, HttpStatus.OK);
		}else {
			return new ResponseEntity<List<Gender>>(genderList, HttpStatus.NO_CONTENT);
		}
	}
	
	// Update
	@PutMapping(path = "/gender/update-gender/{genderPK}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Gender> updateGender(@PathVariable int genderPK, @RequestBody Gender gender){

		boolean updated = genderService.updateGender(gender);
		Gender genderUpdated = genderService.getGenderById(genderPK);
		if(updated) {
			return new ResponseEntity<Gender>(gender, HttpStatus.OK);
		}else {
			return new ResponseEntity<Gender>(gender, HttpStatus.NO_CONTENT);
		}

	}

	// Delete
	@DeleteMapping(path = "/gender/delete-gender/{genderPK}")
	public ResponseEntity<Boolean> deleteGender(@PathVariable int genderPK){

		boolean deleted = genderService.deleteGender(genderPK);

		if(deleted) {
			return new ResponseEntity<Boolean>(deleted, HttpStatus.OK);
		}else {
			return new ResponseEntity<Boolean>(deleted, HttpStatus.NO_CONTENT);
		}
	}
}
