package com.story.resource;

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

import com.story.entity.Story;
import com.story.service.StoryService;


@Controller
public class StoryResource {
	
	@Autowired
	private StoryService storyService;
	
	// Create
	@PostMapping(path = "/story/new-story", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Story> createStory(@RequestBody Story story) {

		Story newStory = storyService.createStory(story);

		if (newStory != null) {
			return new ResponseEntity<>(newStory, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}


	// Read
	// Get by ID
	@CrossOrigin
	@GetMapping(path = "/story/{storyPK}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Story> getStoryById(@PathVariable int storyPK){

		Story story = storyService.getStoryById(storyPK);

		if (story != null) {
			return new ResponseEntity<Story>(story, HttpStatus.OK);
		}else {
			return new ResponseEntity<Story>(story, HttpStatus.NO_CONTENT);
		}
	}
	
	// Read
	// Get by Name
	@CrossOrigin
	@GetMapping(path = "/story/story-by-name/{storyName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Story> getStoryByName(@PathVariable String storyName){

		Story story = storyService.getStoryByName(storyName);

		if (story != null) {
			return new ResponseEntity<Story>(story, HttpStatus.OK);
		}else {
			return new ResponseEntity<Story>(story, HttpStatus.NO_CONTENT);
		}
	}

	// Read
	// Get all
	@GetMapping(path = "/story/all-story", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Story>> getAllStory(){

		List<Story> storyList = storyService.getAllStory();

		if(storyList.size() > 0) {
			return new ResponseEntity<List<Story>>(storyList, HttpStatus.OK);
		}else {
			return new ResponseEntity<List<Story>>(storyList, HttpStatus.NO_CONTENT);
		}
	}
	
	// Update
	@PutMapping(path = "/story/update-story/{storyPK}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Story> updateStory(@PathVariable int storyPK, @RequestBody Story story){

		boolean updated = storyService.updateStory(story);
		Story storyUpdated = storyService.getStoryById(storyPK);
		if(updated) {
			return new ResponseEntity<Story>(story, HttpStatus.OK);
		}else {
			return new ResponseEntity<Story>(story, HttpStatus.NO_CONTENT);
		}

	}

	// Delete
	@DeleteMapping(path = "/story/delete-story/{storyPK}")
	public ResponseEntity<Boolean> deleteStory(@PathVariable int storyPK){

		boolean deleted = storyService.deleteStory(storyPK);

		if(deleted) {
			return new ResponseEntity<Boolean>(deleted, HttpStatus.OK);
		}else {
			return new ResponseEntity<Boolean>(deleted, HttpStatus.NO_CONTENT);
		}
	}
}
