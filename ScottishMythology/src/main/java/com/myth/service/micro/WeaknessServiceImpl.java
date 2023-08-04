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

import com.myth.entity.Weakness;

@Service
public class WeaknessServiceImpl implements WeaknessService {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	HttpHeaders headers;

	@Override
	public Weakness createWeakness(Weakness weakness) {
		
		String url = "http://localhost:7071/weakness/new-weakness/";

	    headers.setContentType(MediaType.APPLICATION_JSON);

	    HttpEntity<Weakness> requestEntity = new HttpEntity<>(weakness, headers);

	    ResponseEntity<Weakness> responseEntity = restTemplate.postForEntity(url, requestEntity, Weakness.class);

	    if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
	        return responseEntity.getBody();
	    } else {
	        return null;
	    }
	}

	@Override
	public List<Weakness> getAllWeakness() {
		
		String url = "http://localhost:7071/weakness/all-weakness";
		
	    ResponseEntity<List<Weakness>> response =
	            restTemplate.exchange(url, HttpMethod.GET, null,
	                    new ParameterizedTypeReference<List<Weakness>>() {});

	    if (response.getStatusCode() == HttpStatus.OK) {
	        return response.getBody();
	    } else {
	   
	        return null;
	    }
		
		
	}

	@Override
	public Weakness getWeaknessById(int weaknessPK) {
		
		String url = "http://localhost:7071/weakness/";
	    return restTemplate.getForObject(url + weaknessPK, Weakness.class);
	}

	@Override
	public Weakness getWeaknessByName(String weaknessName) {
		
		String url = "http://localhost:7071/weakness/weakness-by-name/";
		return restTemplate.getForObject(url + weaknessName, Weakness.class);
	}

	@Override
	public Boolean updateWeakness(Weakness weakness) {
		
		String url = "http://localhost:7071/weakness/update-weakness/" + weakness.getWeaknessPK();
	    
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<Weakness> requestEntity = new HttpEntity<>(weakness, headers);

	    ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
	    return response.getStatusCode() == HttpStatus.OK;
	}

	@Override
	public Boolean deleteWeakness(int weaknessPK) {
		
		String url = "http://localhost:7071/weakness/delete-weakness/" + weaknessPK;
	    ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
	    return response.getStatusCode() == HttpStatus.OK;
	    
	}
	


}
