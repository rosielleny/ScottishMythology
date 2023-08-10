package com.myth.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.myth.entity.Story;
import com.myth.entity.composite.GenericEntity;
import com.myth.service.ScottishMythologyService;
import com.myth.service.micro.StoryService;

@Controller
public class StoryController {
	

	@Autowired
	private StoryService storyService;
	@Autowired
	private ScottishMythologyService scottishMythologyService;
	
	// Model Attributes
	
	@ModelAttribute("stories")
	public List<String> getStories() {
		// Getting all stories
	    List<Story> stories = storyService.getAllStory();
	    // Transforming the list of stories into a list of story names
	    return stories.stream()
	            .map(story -> story.getStoryName())
	            .collect(Collectors.toList());
	}
	
	// A function to convert a list of Stories into a list of GenericEntities
	public List<GenericEntity> convertToGenericEntityList(List<Story> storyList){
		
		List<GenericEntity> entityList = new ArrayList<GenericEntity>();
		// For each story in the list, pass the values into the GenericEntity constructor and add to the entity list
		for(Story story: storyList) {
			GenericEntity entity = new GenericEntity(story.getStoryPK(), story.getStoryName(), story.getStoryDescription());
			entityList.add(entity);
		}
		
		return entityList;
	}
	
	
	// CREATE
	
	@CrossOrigin
	@RequestMapping("/story/create")
	public ModelAndView createAnStory() {
		ModelAndView modelAndView = new ModelAndView();
		// Creating GenericEntity object
		GenericEntity entity = new GenericEntity(0, "", " ");
		// Adding it to the mav
		modelAndView.addObject("entity", entity);
		modelAndView.addObject("entities", getStories());
		// Getting the list of story names and adding to to the mav as "entities"
		modelAndView = scottishMythologyService.setUpLinks("story", "stories", modelAndView);
		// Calling a function to set up all possible links required for the template
		modelAndView.setViewName("entity/create-entity");
		
		return modelAndView;
		
	}

	
	@CrossOrigin
	@RequestMapping("/story/save-new")
	public ModelAndView saveStory(@Valid @ModelAttribute("entity")GenericEntity entity, BindingResult results) {
		
		
		ModelAndView modelAndView = new ModelAndView();
		// If the input has errors it wont be submitted
		if(results.hasErrors()) {
			return new ModelAndView("entity/create-entity", "entity", entity);
		}
		
		String message = null;
		
		if(entity != null) {
			// If we have Entity we convert it back into Story
			Story story = new Story();
			story.setStoryName(entity.getEntityName());
			story.setStoryDescription(entity.getEntityDescription());
			// Then call the service to create it
			storyService.createStory(story);
			message = story.getStoryName() + " successfully added to the database.";
		}
		else {
			message = "An error occurred. Story not added to the database.";
		}
		// Adding the message
		modelAndView.addObject("message", message);
		// Adding all possible necessary links
		modelAndView = scottishMythologyService.setUpLinks("story", "stories", modelAndView);
		
		modelAndView.setViewName("entity/entity-output");
		
		return modelAndView;
	}
	
	
	// READ
	// Functions as the main page, showing all entities and offering a search bar for individual entities at the top
	@CrossOrigin
	@RequestMapping("/story/stories")
	public ModelAndView showAllStories() {
		
		ModelAndView modelAndView = new ModelAndView();
		
		// Getting all stories and converting the list to a list of GenericEntities
		List<GenericEntity> entityList = convertToGenericEntityList(storyService.getAllStory());
		
		modelAndView.addObject("entityList", entityList);
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("story", "stories", modelAndView);
	
		modelAndView.setViewName("entity/show-all-entities");
	
		return modelAndView;
	}
	
	@CrossOrigin
	@RequestMapping("/story/search")
	public ModelAndView searchStoriesByName(HttpServletRequest request) {
		
		ModelAndView modelAndView = new ModelAndView();
		// Getting story by name
		Story story = storyService.getStoryByName(request.getParameter("name"));
		// Setting up all possible links
		modelAndView = scottishMythologyService.setUpLinks("story", "stories", modelAndView);
		
		if(story!=null) {
			// If story was found, we convert to GenericEntity
			GenericEntity entity = new GenericEntity(story.getStoryPK(), story.getStoryName(), story.getStoryDescription());
			modelAndView.addObject("entity", entity);
			modelAndView.setViewName("entity/show-entity");
		}
		else {
			
			modelAndView.addObject("message", "No stories were found for " + request.getParameter("name"));
			modelAndView.setViewName("entity/show-entity");
		}
		
		return modelAndView;
	}
	
	// UPDATE
	
	@CrossOrigin
	@RequestMapping("/story/edit")
	public ModelAndView editStories(@RequestParam int pk) {
		
		ModelAndView modelAndView = new ModelAndView();
		// Getting story by Id
		Story story = storyService.getStoryById(pk);
		// Converting story to GenericEntity
		GenericEntity ogEntity = new GenericEntity(story.getStoryPK(), story.getStoryName(), story.getStoryDescription());
		// Creating a second store for the same entity
		GenericEntity entity = new GenericEntity(ogEntity.getEntityPK(), ogEntity.getEntityName(), ogEntity.getEntityDescription());
		
		// Adding the list of story names
		modelAndView.addObject("entities", getStories());
		// Adding all possible links
		modelAndView.addObject("entity", entity);
		modelAndView = scottishMythologyService.setUpLinks("story", "stories", modelAndView);
		modelAndView.addObject("ogEntity", ogEntity);
		modelAndView.setViewName("entity/edit-entity");
		
		
		return modelAndView;
	}
	
	@CrossOrigin
	@RequestMapping("/story/save-edit")
	public ModelAndView saveEdittedStory(@Valid @ModelAttribute("entity")GenericEntity entity, BindingResult results){
		
		
		ModelAndView modelAndView = new ModelAndView();
		
		if(results.hasErrors()) {
			return new ModelAndView("entity/edit-entity", "entity", entity);
		}
		
		String message = null;
		
		if(entity != null) {
			// Getting the original story by PK
			Story story = storyService.getStoryById(entity.getEntityPK());
			// Setting the name to the new name
			story.setStoryName(entity.getEntityName());
			// Setting the desc to the new desc
			story.setStoryDescription(entity.getEntityDescription());
			if(storyService.updateStory(story)) {
				
				message = story.getStoryName() + " successfully updated";
			}else {
				message = "An error occurred. Story not updated. Backend";
			}
		}
		else {
			message = "An error occurred. Story not updated. Frontend";
		}
		// Getting the new story by ID
		Story newStory = storyService.getStoryById(entity.getEntityPK());
		// Converting to GenericEntity
		GenericEntity newEntity = new GenericEntity(newStory.getStoryPK(), newStory.getStoryName(), newStory.getStoryDescription());
		modelAndView.addObject("entity", newEntity);
		modelAndView.addObject("message", message);
		// Adding all possible links to the mav
		modelAndView = scottishMythologyService.setUpLinks("story", "stories", modelAndView);
		
		modelAndView.setViewName("entity/entity-output");
		
		return modelAndView;
	}
	
	// DELETE
	
	@CrossOrigin
	@RequestMapping("/story/delete/{pk}")
	public ModelAndView deleteStories(@PathVariable int pk) {
		
		ModelAndView modelAndView = new ModelAndView();
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("story", "stories", modelAndView);
		// Getting the story by PK
		Story story = storyService.getStoryById(pk);
		// Converting to GenericEntity
		GenericEntity entity = new GenericEntity(story.getStoryPK(), story.getStoryName(), story.getStoryDescription());
		
		if(storyService.deleteStory(pk)) {
			modelAndView.addObject("message", story.getStoryName() + " successfully deleted from Stories.");
		}else {
			modelAndView.addObject("message", "Failed to delete " + story.getStoryName() + " from Stories.");
		}
		// Getting list of stories as GenericEntity list
		List<GenericEntity> entityList = convertToGenericEntityList(storyService.getAllStory());
		
		modelAndView.addObject("entity", entity);
		modelAndView.addObject("entityList", entityList);
		modelAndView.setViewName("entity/show-all-entities");
		
		return modelAndView;
	}

}
