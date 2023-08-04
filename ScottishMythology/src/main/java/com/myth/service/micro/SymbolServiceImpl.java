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

import com.myth.entity.Symbol;

@Service
public class SymbolServiceImpl implements SymbolService {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	HttpHeaders headers;

	@Override
	public Symbol createSymbol(Symbol symbol) {
		
		String url = "http://localhost:7033/symbol/new-symbol/";

	    headers.setContentType(MediaType.APPLICATION_JSON);

	    HttpEntity<Symbol> requestEntity = new HttpEntity<>(symbol, headers);

	    ResponseEntity<Symbol> responseEntity = restTemplate.postForEntity(url, requestEntity, Symbol.class);

	    if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
	        return responseEntity.getBody();
	    } else {
	        return null;
	    }
	}

	@Override
	public List<Symbol> getAllSymbol() {
		
		String url = "http://localhost:7033/symbol/all-symbol";
		
	    ResponseEntity<List<Symbol>> response =
	            restTemplate.exchange(url, HttpMethod.GET, null,
	                    new ParameterizedTypeReference<List<Symbol>>() {});

	    if (response.getStatusCode() == HttpStatus.OK) {
	        return response.getBody();
	    } else {
	   
	        return null;
	    }
		
		
	}

	@Override
	public Symbol getSymbolById(int symbolPK) {
		
		String url = "http://localhost:7033/symbol/";
	    return restTemplate.getForObject(url + symbolPK, Symbol.class);
	}

	@Override
	public Symbol getSymbolByName(String symbolName) {
		
		String url = "http://localhost:7033/symbol/symbol-by-name/";
		return restTemplate.getForObject(url + symbolName, Symbol.class);
	}

	@Override
	public Boolean updateSymbol(Symbol symbol) {
		
		String url = "http://localhost:7033/symbol/update-symbol/" + symbol.getSymbolPK();
	    
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<Symbol> requestEntity = new HttpEntity<>(symbol, headers);

	    ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
	    return response.getStatusCode() == HttpStatus.OK;
	}

	@Override
	public Boolean deleteSymbol(int symbolPK) {
		
		String url = "http://localhost:7033/symbol/delete-symbol/" + symbolPK;
	    ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
	    return response.getStatusCode() == HttpStatus.OK;
	    
	}
	


}
