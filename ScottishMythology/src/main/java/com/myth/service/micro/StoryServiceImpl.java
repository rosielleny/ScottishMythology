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

import com.myth.entity.Story;

@Service
public class StoryServiceImpl implements StoryService {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	HttpHeaders headers;

	@Override
	public Story createStory(Story story) {
		
		String url = "http://localhost:7073/story/new-story/";

	    headers.setContentType(MediaType.APPLICATION_JSON);

	    HttpEntity<Story> requestEntity = new HttpEntity<>(story, headers);

	    ResponseEntity<Story> responseEntity = restTemplate.postForEntity(url, requestEntity, Story.class);

	    if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
	        return responseEntity.getBody();
	    } else {
	        return null;
	    }
	}

	@Override
	public List<Story> getAllStory() {
		
		String url = "http://localhost:7073/story/all-story";
		
	    ResponseEntity<List<Story>> response =
	            restTemplate.exchange(url, HttpMethod.GET, null,
	                    new ParameterizedTypeReference<List<Story>>() {});

	    if (response.getStatusCode() == HttpStatus.OK) {
	        return response.getBody();
	    } else {
	   
	        return null;
	    }
		
		
	}

	@Override
	public Story getStoryById(int storyPK) {
		
		String url = "http://localhost:7073/story/";
	    return restTemplate.getForObject(url + storyPK, Story.class);
	}

	@Override
	public Story getStoryByName(String storyName) {
		
		String url = "http://localhost:7073/story/story-by-name/";
		return restTemplate.getForObject(url + storyName, Story.class);
	}

	@Override
	public Boolean updateStory(Story story) {
		
		String url = "http://localhost:7073/story/update-story/" + story.getStoryPK();
	    
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<Story> requestEntity = new HttpEntity<>(story, headers);

	    ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
	    return response.getStatusCode() == HttpStatus.OK;
	}

	@Override
	public Boolean deleteStory(int storyPK) {
		
		String url = "http://localhost:7073/story/delete-story/" + storyPK;
	    ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
	    return response.getStatusCode() == HttpStatus.OK;
	    
	}
	


}
