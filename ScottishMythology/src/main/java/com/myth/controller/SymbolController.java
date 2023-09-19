package com.myth.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.myth.entity.Ability;
import com.myth.entity.Being;
import com.myth.entity.Symbol;
import com.myth.entity.Symbol;
import com.myth.entity.composite.GenericEntity;
import com.myth.entity.junction.BeingAbility;
import com.myth.entity.junction.BeingSymbolism;
import com.myth.entity.junction.KeyBeingAbility;
import com.myth.entity.junction.KeyBeingSymbolism;
import com.myth.service.BeingService;
import com.myth.service.ScottishMythologyService;
import com.myth.service.junction.BeingSymbolismService;
import com.myth.service.micro.SymbolService;

@Controller
public class SymbolController {

	@Autowired
	private SymbolService symbolService;
	@Autowired
	private ScottishMythologyService scottishMythologyService;
	@Autowired
	private BeingService beingService;
	@Autowired
	private BeingSymbolismService beingSymbolismService;

	// Model Attributes

	@ModelAttribute("symbolism")
	public List<String> getSymbolism() {
		// Getting all symbolism
		List<Symbol> symbolism = symbolService.getAllSymbol(); 
		// Transforming the list of symbolism into a list of symbol names
		return symbolism.stream()
				.map(symbol -> symbol.getSymbolName())
				.collect(Collectors.toList());
	}

	// A function to convert a list of Symbolism into a list of GenericEntities
	public List<GenericEntity> convertToGenericEntityList(List<Symbol> symbolList){

		List<GenericEntity> entityList = new ArrayList<GenericEntity>();
		// For each symbol in the list, pass the values into the GenericEntity constructor and add to the entity list
		for(Symbol symbol: symbolList) {
			GenericEntity entity = new GenericEntity(symbol.getSymbolPK(), symbol.getSymbolName());
			entityList.add(entity);
		}

		return entityList;
	}


	// CREATE

	@CrossOrigin
	@RequestMapping("/symbol/create")
	public ModelAndView createAnSymbol() {

		ModelAndView modelAndView = new ModelAndView();
		// Creating GenericEntity object
		GenericEntity entity = new GenericEntity(0, "");
		// Adding it to the mav
		modelAndView.addObject("entity", entity);
		// Getting the list of symbol names and adding to to the mav as "entities"
		modelAndView.addObject("entities", getSymbolism());
		// Calling a function to set up all possible links required for the template
		modelAndView = scottishMythologyService.setUpLinks("symbol", "symbolism", modelAndView);

		modelAndView.setViewName("entity/create-entity");

		return modelAndView;

	}


	@CrossOrigin
	@RequestMapping("/symbol/save-new")
	public ModelAndView saveSymbol(@Valid @ModelAttribute("entity")GenericEntity entity, BindingResult results) {


		ModelAndView modelAndView = new ModelAndView();
		// If the input has errors it wont be submitted
		if(results.hasErrors()) {
			return new ModelAndView("entity/create-entity", "entity", entity);
		}

		String message = null;

		try {
			if(entity != null) {
				// If we have Entity we convert it back into Symbol
				Symbol symbol = new Symbol();
				symbol.setSymbolName(entity.getEntityName());
				// Then call the service to create it
				if(symbolService.createSymbol(symbol) !=null) {
					message = symbol.getSymbolName() + " successfully added to the database.";

					Symbol createdSymbol = symbolService.getSymbolByName(symbol.getSymbolName());

					entity = new GenericEntity(createdSymbol.getSymbolPK(), createdSymbol.getSymbolName());
				}
				else {
					message = "An error occurred. Symbol not added to the database.";
				}
			}
			else {
				message = "An error occurred. Symbol not added to the database.";
			}
		}
		catch(Exception e){
			message = "An error occurred. Symbol not added to the database.";

		}

		// Adding the message
		modelAndView.addObject("message", message);
		// Adding all possible necessary links
		modelAndView = scottishMythologyService.setUpLinks("symbol", "symbolism", modelAndView);
		modelAndView.addObject("entity", entity);
		modelAndView.setViewName("entity/entity-output");

		return modelAndView;
	}


	// READ
	// Functions as the main page, showing all entities and offering a search bar for individual entities at the top
	@CrossOrigin
	@RequestMapping("/symbol/symbolism")
	public ModelAndView showAllSymbolism() {

		ModelAndView modelAndView = new ModelAndView();

		// Getting all symbolism and converting the list to a list of GenericEntities
		List<GenericEntity> entityList = convertToGenericEntityList(symbolService.getAllSymbol());

		modelAndView.addObject("entityList", entityList);
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("symbol", "symbolism", modelAndView);

		modelAndView.setViewName("entity/show-all-entities");

		return modelAndView;
	}

	@CrossOrigin
	@RequestMapping("/symbol/search")
	public ModelAndView searchSymbolismByName(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView();
		// Getting symbol by name
		Symbol symbol = symbolService.getSymbolByName(request.getParameter("name"));
		// Setting up all possible links
		modelAndView = scottishMythologyService.setUpLinks("symbol", "symbolism", modelAndView);

		if(symbol!=null) {
			// If symbol was found, we convert to GenericEntity
			GenericEntity entity = new GenericEntity(symbol.getSymbolPK(), symbol.getSymbolName());
			modelAndView.addObject("entity", entity);
			modelAndView.setViewName("entity/show-entity");
		}
		else {

			Being being = beingService.getBeingByName(request.getParameter("name"));

			if(being !=null) {

				int id = being.getBeingPK();
				List<BeingSymbolism> beingSymbols = beingSymbolismService.getBeingSymbolismByBeingId(id);
				List<Symbol> symbolList = new ArrayList<Symbol>();

				for(BeingSymbolism beingSymbol: beingSymbols) {

					KeyBeingSymbolism key = beingSymbol.getId();
					int symbolKey = key.getSymbolPK();

					Symbol foundSymbol = symbolService.getSymbolById(symbolKey);

					symbolList.add(foundSymbol);
				}

				List<GenericEntity> entityList = convertToGenericEntityList(symbolList);

				modelAndView.addObject("entityList", entityList);
				modelAndView.addObject("message", "Symbol results for " + request.getParameter("name"));
				modelAndView.setViewName("entity/show-entity");
				return modelAndView;
			}
			else {

				modelAndView.addObject("message", "No symbols were found for " + request.getParameter("name"));
				modelAndView.setViewName("entity/show-entity");
			}
		}

		return modelAndView;
	}

	// UPDATE

	@CrossOrigin
	@RequestMapping("/symbol/edit")
	public ModelAndView editSymbolism(@RequestParam int pk) {

		ModelAndView modelAndView = new ModelAndView();
		// Getting symbol by Id
		Symbol symbol = symbolService.getSymbolById(pk);
		// Converting symbol to GenericEntity
		GenericEntity ogEntity = new GenericEntity(symbol.getSymbolPK(), symbol.getSymbolName());
		// Creating a second store for the same entity
		GenericEntity entity = new GenericEntity(ogEntity.getEntityPK(), ogEntity.getEntityName());

		// Adding the list of symbol names
		modelAndView.addObject("entities", getSymbolism());

		modelAndView.addObject("entity", entity);
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("symbol", "symbolism", modelAndView);
		modelAndView.addObject("ogEntity", ogEntity);
		modelAndView.setViewName("entity/edit-entity");


		return modelAndView;
	}

	@CrossOrigin
	@RequestMapping("/symbol/save-edit")
	public ModelAndView saveEdittedSymbol(@Valid @ModelAttribute("entity")GenericEntity entity, BindingResult results){


		ModelAndView modelAndView = new ModelAndView();

		if(results.hasErrors()) {
			return new ModelAndView("entity/edit-entity", "entity", entity);
		}

		String message = null;

		if(entity != null) {
			// Getting the original symbol by PK
			Symbol symbol = symbolService.getSymbolById(entity.getEntityPK());
			// Setting the name to the new name
			symbol.setSymbolName(entity.getEntityName());

			if(symbolService.updateSymbol(symbol)) {

				message = symbol.getSymbolName() + " successfully updated";

			}else {
				message = "An error occurred. Symbol not updated. Backend";
			}
		}
		else {
			message = "An error occurred. Symbol not updated. Frontend";
		}

		// Getting the new symbol by ID
		Symbol newSymbol = symbolService.getSymbolById(entity.getEntityPK());
		// Converting to GenericEntity
		GenericEntity newEntity = new GenericEntity(newSymbol.getSymbolPK(), newSymbol.getSymbolName());
		modelAndView.addObject("entity", newEntity);
		modelAndView.addObject("message", message);
		// Adding all possible links to the mav
		modelAndView = scottishMythologyService.setUpLinks("symbol", "symbolism", modelAndView);

		modelAndView.setViewName("entity/entity-output");

		return modelAndView;
	}

	// DELETE

	@CrossOrigin
	@RequestMapping("/symbol/delete/{pk}")
	public ModelAndView deleteSymbolism(@PathVariable int pk) {

		ModelAndView modelAndView = new ModelAndView();
		// Adding all possible links
		modelAndView = scottishMythologyService.setUpLinks("symbol", "symbolism", modelAndView);
		// Getting the symbol by PK
		Symbol symbol = symbolService.getSymbolById(pk);
		// Converting to GenericEntity
		GenericEntity entity = new GenericEntity(symbol.getSymbolPK(), symbol.getSymbolName());

		if(symbolService.deleteSymbol(pk)) {
			modelAndView.addObject("message", symbol.getSymbolName() + " successfully deleted from Symbolism.");
		}else {
			modelAndView.addObject("message", "Failed to delete " + symbol.getSymbolName() + " from Symbolism.");
		}

		// Getting list of symbolism as GenericEntity list
		List<GenericEntity> entityList = convertToGenericEntityList(symbolService.getAllSymbol());

		modelAndView.addObject("entity", entity);
		modelAndView.addObject("entityList", entityList);
		modelAndView.setViewName("entity/show-all-entities");

		return modelAndView;
	}

	@PostMapping("symbol/add")
	public ResponseEntity<Map<String, Boolean>> sneakyAddSymbol(@RequestBody Symbol symbol) {
		Map<String, Boolean> response = new HashMap<>();
		try {
			if(symbolService.createSymbol(symbol) !=null) {
				response.put("success", true);
				return ResponseEntity.ok(response);
			}
			else {
				response.put("success", false);
				return ResponseEntity.badRequest().body(response);
			}
		} catch (Exception e) {
			response.put("success", false);
			return ResponseEntity.badRequest().body(response);
		}
	}

}
