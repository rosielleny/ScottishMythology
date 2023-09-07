package com.gender.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gender.entity.Gender;
import com.gender.dao.GenderDao;
import com.gender.entity.Gender;

@Service
public class GenderServiceImpl implements GenderService {

	@Autowired
	private GenderDao genderDao;
	
	// Create
	
	@Override
	public Gender createGender(Gender gender) {
		
		if(genderDao.save(gender) != null) {
			Gender createdGender = getGenderByType(gender.getGenderType());
			return createdGender;
		} 
		else {
			return null;
		}
		
	}
	
	//Read

	@Override
	public List<Gender> getAllGender() {
		
		return genderDao.findAll();
	}

	@Override
	public Gender getGenderById(int genderPK) {
		
		Gender gender = genderDao.findById(genderPK).orElse(null);
		return gender;
	}

	@Override
	public Gender getGenderByType(String genderType) {
		
		return genderDao.findByType(genderType);
	}

	// Update
	
	@Override
	public Boolean updateGender(Gender gender) {
		
		Gender genderExists = getGenderById(gender.getGenderPK());
		
		if(genderExists !=null) {
			
			genderDao.save(gender);
			return true;
			
		}
		return false;
	}

	// Delete
	
	@Override
	public Boolean deleteGender(int genderPK) {
		
		Gender gender = getGenderById(genderPK);
		
		if(gender !=null) {
			
			genderDao.deleteById(genderPK);
			return true;
			
		}
		return false;
	}

}
