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

import com.myth.entity.Ability;

@Service
public class AbilityServiceImpl implements AbilityService {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private HttpHeaders headers;

	@Override
	public Ability createAbility(Ability ability) {
		
		String url = "http://localhost:7037/ability/new-ability/";

	    headers.setContentType(MediaType.APPLICATION_JSON);

	    HttpEntity<Ability> requestEntity = new HttpEntity<>(ability, headers);

	    ResponseEntity<Ability> responseEntity = restTemplate.postForEntity(url, requestEntity, Ability.class);

	    if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
	        return responseEntity.getBody();
	    } else {
	        return null;
	    }
	}

	@Override
	public List<Ability> getAllAbility() {
		
		String url = "http://localhost:7037/ability/all-ability";
		
	    ResponseEntity<List<Ability>> response =
	            restTemplate.exchange(url, HttpMethod.GET, null,
	                    new ParameterizedTypeReference<List<Ability>>() {});

	    if (response.getStatusCode() == HttpStatus.OK) {
	        return response.getBody();
	    } else {
	   
	        return null;
	    }
		
		
	}

	@Override
	public Ability getAbilityById(int abilityPK) {
		
		String url = "http://localhost:7037/ability/";
	    return restTemplate.getForObject(url + abilityPK, Ability.class);
	}

	@Override
	public Ability getAbilityByName(String abilityName) {
		
		String url = "http://localhost:7037/ability/ability-by-name/";
		return restTemplate.getForObject(url + abilityName, Ability.class);
	}

	@Override
	public Boolean updateAbility(Ability ability) {
		
		String url = "http://localhost:7037/ability/update-ability/" + ability.getAbilityPK();
	    
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<Ability> requestEntity = new HttpEntity<>(ability, headers);

	    ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
	    return response.getStatusCode() == HttpStatus.OK;
	}

	@Override
	public Boolean deleteAbility(int abilityPK) {
		
		String url = "http://localhost:7037/ability/delete-ability/" + abilityPK;
	    ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
	    return response.getStatusCode() == HttpStatus.OK;
	    
	}
	


}
