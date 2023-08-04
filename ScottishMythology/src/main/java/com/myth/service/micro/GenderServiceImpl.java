package com.myth.service.micro;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.myth.entity.Gender;

@Service
public class GenderServiceImpl implements GenderService {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	HttpHeaders headers;

	@Override
	public Gender createGender(Gender ability) {
		
		String url = "http://localhost:7011/ability/new-ability/";

	    headers.setContentType(MediaType.APPLICATION_JSON);

	    HttpEntity<Gender> requestEntity = new HttpEntity<>(ability, headers);

	    ResponseEntity<Gender> responseEntity = restTemplate.postForEntity(url, requestEntity, Gender.class);

	    if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
	        return responseEntity.getBody();
	    } else {
	        return null;
	    }
	}

	@Override
	public List<Gender> getAllGender() {
		
		String url = "http://localhost:7011/ability/all-ability";
		
	    ResponseEntity<List<Gender>> response =
	            restTemplate.exchange(url, HttpMethod.GET, null,
	                    new ParameterizedTypeReference<List<Gender>>() {});

	    if (response.getStatusCode() == HttpStatus.OK) {
	        return response.getBody();
	    } else {
	   
	        return null;
	    }
		
		
	}

	@Override
	public Gender getGenderById(int abilityPK) {
		
		String url = "http://localhost:7011/ability/";
	    return restTemplate.getForObject(url + abilityPK, Gender.class);
	}

	@Override
	public Gender getGenderByName(String abilityName) {
		
		String url = "http://localhost:7011/ability/ability-by-name/";
		return restTemplate.getForObject(url + abilityName, Gender.class);
	}

	@Override
	public Boolean updateGender(Gender ability) {
		
		String url = "http://localhost:7011/ability/update-ability/" + ability.getGenderPK();
	    
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<Gender> requestEntity = new HttpEntity<>(ability, headers);

	    ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
	    return response.getStatusCode() == HttpStatus.OK;
	}

	@Override
	public Boolean deleteGender(int abilityPK) {
		
		String url = "http://localhost:7011/ability/delete-ability/" + abilityPK;
	    ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
	    return response.getStatusCode() == HttpStatus.OK;
	    
	}
	


}
