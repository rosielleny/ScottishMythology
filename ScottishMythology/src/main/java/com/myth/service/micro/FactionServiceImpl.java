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

import com.myth.entity.Faction;

@Service
public class FactionServiceImpl implements FactionService {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private HttpHeaders headers;

	@Override
	public Faction createFaction(Faction faction) {
		
		String url = "http://localhost:7017/faction/new-faction/";

	    headers.setContentType(MediaType.APPLICATION_JSON);

	    HttpEntity<Faction> requestEntity = new HttpEntity<>(faction, headers);

	    ResponseEntity<Faction> responseEntity = restTemplate.postForEntity(url, requestEntity, Faction.class);

	    if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
	        return responseEntity.getBody();
	    } else {
	        return null;
	    }
	}

	@Override
	public List<Faction> getAllFaction() {
		
		String url = "http://localhost:7017/faction/all-faction";
		
	    ResponseEntity<List<Faction>> response =
	            restTemplate.exchange(url, HttpMethod.GET, null,
	                    new ParameterizedTypeReference<List<Faction>>() {});

	    if (response.getStatusCode() == HttpStatus.OK) {
	        return response.getBody();
	    } else {
	   
	        return null;
	    }
		
		
	}

	@Override
	public Faction getFactionById(int factionPK) {
		
		String url = "http://localhost:7017/faction/";
	    return restTemplate.getForObject(url + factionPK, Faction.class);
	}

	@Override
	public Faction getFactionByName(String factionName) {
		
		String url = "http://localhost:7017/faction/faction-by-name/";
		return restTemplate.getForObject(url + factionName, Faction.class);
	}

	@Override
	public Boolean updateFaction(Faction faction) {
		
		String url = "http://localhost:7017/faction/update-faction/" + faction.getFactionPK();
	    
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<Faction> requestEntity = new HttpEntity<>(faction, headers);

	    ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
	    return response.getStatusCode() == HttpStatus.OK;
	}

	@Override
	public Boolean deleteFaction(int factionPK) {
		
		String url = "http://localhost:7017/faction/delete-faction/" + factionPK;
	    ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
	    return response.getStatusCode() == HttpStatus.OK;
	    
	}
	


}
