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

import com.myth.entity.Location;

@Service
public class LocationServiceImpl implements LocationService {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	HttpHeaders headers;

	@Override
	public Location createLocation(Location location) {
		
		String url = "http://localhost:7077/location/new-location/";

	    headers.setContentType(MediaType.APPLICATION_JSON);

	    HttpEntity<Location> requestEntity = new HttpEntity<>(location, headers);

	    ResponseEntity<Location> responseEntity = restTemplate.postForEntity(url, requestEntity, Location.class);

	    if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
	        return responseEntity.getBody();
	    } else {
	        return null;
	    }
	}

	@Override
	public List<Location> getAllLocation() {
		
		String url = "http://localhost:7077/location/all-location";
		
	    ResponseEntity<List<Location>> response =
	            restTemplate.exchange(url, HttpMethod.GET, null,
	                    new ParameterizedTypeReference<List<Location>>() {});

	    if (response.getStatusCode() == HttpStatus.OK) {
	        return response.getBody();
	    } else {
	   
	        return null;
	    }
		
		
	}

	@Override
	public Location getLocationById(int locationPK) {
		
		String url = "http://localhost:7077/location/";
	    return restTemplate.getForObject(url + locationPK, Location.class);
	}

	@Override
	public Location getLocationByName(String locationName) {
		
		String url = "http://localhost:7077/location/location-by-name/";
		return restTemplate.getForObject(url + locationName, Location.class);
	}

	@Override
	public Boolean updateLocation(Location location) {
		
		String url = "http://localhost:7077/location/update-location/" + location.getLocationPK();
	    
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<Location> requestEntity = new HttpEntity<>(location, headers);

	    ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
	    return response.getStatusCode() == HttpStatus.OK;
	}

	@Override
	public Boolean deleteLocation(int locationPK) {
		
		String url = "http://localhost:7077/location/delete-location/" + locationPK;
	    ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
	    return response.getStatusCode() == HttpStatus.OK;
	    
	}
	


}
