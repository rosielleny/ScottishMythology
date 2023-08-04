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

import com.myth.entity.Species;

@Service
public class SpeciesServiceImpl implements SpeciesService {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	HttpHeaders headers;

	@Override
	public Species createSpecies(Species species) {
		
		String url = "http://localhost:7031/species/new-species/";

	    headers.setContentType(MediaType.APPLICATION_JSON);

	    HttpEntity<Species> requestEntity = new HttpEntity<>(species, headers);

	    ResponseEntity<Species> responseEntity = restTemplate.postForEntity(url, requestEntity, Species.class);

	    if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
	        return responseEntity.getBody();
	    } else {
	        return null;
	    }
	}

	@Override
	public List<Species> getAllSpecies() {
		
		String url = "http://localhost:7031/species/all-species";
		
	    ResponseEntity<List<Species>> response =
	            restTemplate.exchange(url, HttpMethod.GET, null,
	                    new ParameterizedTypeReference<List<Species>>() {});

	    if (response.getStatusCode() == HttpStatus.OK) {
	        return response.getBody();
	    } else {
	   
	        return null;
	    }
		
		
	}

	@Override
	public Species getSpeciesById(int speciesPK) {
		
		String url = "http://localhost:7031/species/";
	    return restTemplate.getForObject(url + speciesPK, Species.class);
	}

	@Override
	public Species getSpeciesByName(String speciesName) {
		
		String url = "http://localhost:7031/species/species-by-name/";
		return restTemplate.getForObject(url + speciesName, Species.class);
	}

	@Override
	public Boolean updateSpecies(Species species) {
		
		String url = "http://localhost:7031/species/update-species/" + species.getSpeciesPK();
	    
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<Species> requestEntity = new HttpEntity<>(species, headers);

	    ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
	    return response.getStatusCode() == HttpStatus.OK;
	}

	@Override
	public Boolean deleteSpecies(int speciesPK) {
		
		String url = "http://localhost:7031/species/delete-species/" + speciesPK;
	    ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
	    return response.getStatusCode() == HttpStatus.OK;
	    
	}
	


}
