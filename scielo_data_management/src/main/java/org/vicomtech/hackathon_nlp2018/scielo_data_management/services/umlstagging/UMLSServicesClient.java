package org.vicomtech.hackathon_nlp2018.scielo_data_management.services.umlstagging;

import java.net.URI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.model.UMLSMetadata;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UMLSServicesClient {

	private String server = "http://dnlp:8095/umls/search/cui/overview";
	private RestTemplate rest;
	private HttpHeaders headers;

	public UMLSServicesClient() {
		rest = new RestTemplate();
	    headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json; charset=utf-8");
	    headers.add("Accept", "application/json");
	}
	
	public UMLSMetadata retrieveMetadataForUMLSCode(String umlsCode) {   
		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		URI uri = UriComponentsBuilder.fromHttpUrl(server).queryParam("includeLanguage", "spa").queryParam("cui", umlsCode).build().toUri();
		log.info("Making call for umls metadata with code {}, to uri: {}",umlsCode,uri.toString());
		ResponseEntity<UMLSMetadata> responseEntity = rest.exchange(uri, HttpMethod.POST, requestEntity, UMLSMetadata.class);
		return responseEntity.getBody();
	}
	
}
