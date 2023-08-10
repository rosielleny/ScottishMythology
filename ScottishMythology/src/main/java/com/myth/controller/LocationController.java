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

import com.myth.entity.Location;
import com.myth.entity.composite.GenericEntity;
import com.myth.service.ScottishMythologyService;
import com.myth.service.micro.LocationService;

@Controller
public class LocationController {

	@Autowired
	private LocationService locationService;
	@Autowired
	private ScottishMythologyService scottishMythologyService;

	// Model Attributes

	@ModelAttribute("locations")
	public List<String> getLocations() {
		// Getting all locations
		List<Location> locations = locationService.getAllLocation();
		// Transforming the list of locations into a list of location names
		return locations.stream()
				.map(location -> location.getLocationName())
				.collect(Collectors.toList());
	}

	// A function to convert a list of Locations into a list of GenericEntities
	public List<GenericEntity> convertToGenericEntityList(List<Location> locationList){

		List<GenericEntity> entityList = new ArrayList<GenericEntity>();
		// For each location in the list, pass the values into the GenericEntity constructor and add to the entity list
		for(Location location: locationList) {
			GenericEntity entity = new GenericEntity(location.getLocationPK(), location.getLocationName(), location.getLocationDescription());
			entityList.add(entity);
		}

		return entityList;
	}


	// CREATE

	@CrossOrigin
	@RequestMapping("/location/create")
	public ModelAndView createAnLocation() {
		ModelAndView modelAndView = new ModelAndView();
		// Creating GenericEntity object
		GenericEntity entity = new GenericEntity(0, "", " ");
		// Adding it to the mav
		modelAndView.addObject("entity", entity);
		modelAndView.addObject("entities", getLocations());
		// Getting the list of location names and adding to to the mav as "entities"
		modelAndView = scottishMythologyService.setUpLinks("location", "locations", modelAndView);
		// Calling a function to set up all possible links required for the template
		modelAndView.setViewName("entity/create-entity");

		return modelAndView;

	}


	@CrossOrigin
	@RequestMapping("/location/save-new")
	public ModelAndView saveLocation(@Valid @ModelAttribute("entity")GenericEntity entity, BindingResult results) {


		ModelAndView modelAndView = new ModelAndView();
		// If the input has errors it wont be submitted
		if(results.hasErrors()) {
			return new ModelAndView("entity/create-entity", "entity", entity);
		}

		String message = null;

		if(entity != null) {
			// If we have Entity we convert it back into Location
			Location location = new Location();
			location.setLocationName(entity.getEntityName());
			location.setLocationDescription(entity.getEntityDescription());
			// Then call the service to create it
			locationService.createLocation(location);
			message = location.getLocationName() + " successfully added to the database.";
		}
		else {
			message = "An error occurred. Location not added to the database.";
		}
		// Adding the message
		modelAndView.addObject("message", message);
		// Adding all possible necessary links
		modelAndView = scottishMythologyService.setUpLinks("location", "locations", modelAndView);

		modelAndView.setViewName("entity/entity-output");

		return modelAndView;
	}


	// READ
	// Functions as the main page, showing all entities and offering a search bar for individual entities at the top
	@CrossOrigin
	@RequestMapping("/location/locations")
	public ModelAndView showAllLocations() {

		ModelAndView modelAndView = new ModelAndView();

		// Getting all locations and converting the list to a list of GenericEntities
		List<GenericEntity> entityList = convertToGenericEntityList(locationService.getAllLocation());

		modelAndView.addObject("entityList", entityList);
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("location", "locations", modelAndView);

		modelAndView.setViewName("entity/show-all-entities");

		return modelAndView;
	}

	@CrossOrigin
	@RequestMapping("/location/search")
	public ModelAndView searchLocationsByName(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView();
		// Getting location by name
		Location location = locationService.getLocationByName(request.getParameter("name"));
		// Setting up all possible links
		modelAndView = scottishMythologyService.setUpLinks("location", "locations", modelAndView);

		if(location!=null) {
			// If location was found, we convert to GenericEntity
			GenericEntity entity = new GenericEntity(location.getLocationPK(), location.getLocationName(), location.getLocationDescription());
			modelAndView.addObject("entity", entity);
			modelAndView.setViewName("entity/show-entity");
		}
		else {

			modelAndView.addObject("message", "No locations were found for " + request.getParameter("name"));
			modelAndView.setViewName("entity/show-entity");
		}

		return modelAndView;
	}

	// UPDATE

	@CrossOrigin
	@RequestMapping("/location/edit")
	public ModelAndView editLocations(@RequestParam int pk) {

		ModelAndView modelAndView = new ModelAndView();
		// Getting location by Id
		Location location = locationService.getLocationById(pk);
		// Converting location to GenericEntity
		GenericEntity ogEntity = new GenericEntity(location.getLocationPK(), location.getLocationName(), location.getLocationDescription());
		// Creating a second store for the same entity
		GenericEntity entity = new GenericEntity(ogEntity.getEntityPK(), ogEntity.getEntityName(), ogEntity.getEntityDescription());

		// Adding the list of location names
		modelAndView.addObject("entities", getLocations());
		// Adding all possible links
		modelAndView.addObject("entity", entity);
		modelAndView = scottishMythologyService.setUpLinks("location", "locations", modelAndView);
		modelAndView.addObject("ogEntity", ogEntity);
		modelAndView.setViewName("entity/edit-entity");


		return modelAndView;
	}

	@CrossOrigin
	@RequestMapping("/location/save-edit")
	public ModelAndView saveEdittedLocation(@Valid @ModelAttribute("entity")GenericEntity entity, BindingResult results){


		ModelAndView modelAndView = new ModelAndView();

		if(results.hasErrors()) {
			return new ModelAndView("entity/edit-entity", "entity", entity);
		}

		String message = null;

		if(entity != null) {
			// Getting the original location by PK
			Location location = locationService.getLocationById(entity.getEntityPK());
			// Setting the name to the new name
			location.setLocationName(entity.getEntityName());
			// Setting the desc to the new desc
			location.setLocationDescription(entity.getEntityDescription());
			if(locationService.updateLocation(location)) {

				message = location.getLocationName() + " successfully updated";
			}else {
				message = "An error occurred. Location not updated. Backend";
			}
		}
		else {
			message = "An error occurred. Location not updated. Frontend";
		}
		// Getting the new location by ID
		Location newLocation = locationService.getLocationById(entity.getEntityPK());
		// Converting to GenericEntity
		GenericEntity newEntity = new GenericEntity(newLocation.getLocationPK(), newLocation.getLocationName(), newLocation.getLocationDescription());
		modelAndView.addObject("entity", newEntity);
		modelAndView.addObject("message", message);
		// Adding all possible links to the mav
		modelAndView = scottishMythologyService.setUpLinks("location", "locations", modelAndView);

		modelAndView.setViewName("entity/entity-output");

		return modelAndView;
	}

	// DELETE

	@CrossOrigin
	@RequestMapping("/location/delete/{pk}")
	public ModelAndView deleteLocations(@PathVariable int pk) {

		ModelAndView modelAndView = new ModelAndView();
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("location", "locations", modelAndView);
		// Getting the location by PK
		Location location = locationService.getLocationById(pk);
		// Converting to GenericEntity
		GenericEntity entity = new GenericEntity(location.getLocationPK(), location.getLocationName(), location.getLocationDescription());

		if(locationService.deleteLocation(pk)) {
			modelAndView.addObject("message", location.getLocationName() + " successfully deleted from Locations.");
		}else {
			modelAndView.addObject("message", "Failed to delete " + location.getLocationName() + " from Locations.");
		}
		// Getting list of locations as GenericEntity list
		List<GenericEntity> entityList = convertToGenericEntityList(locationService.getAllLocation());

		modelAndView.addObject("entity", entity);
		modelAndView.addObject("entityList", entityList);
		modelAndView.setViewName("entity/show-all-entities");

		return modelAndView;

	}
}
