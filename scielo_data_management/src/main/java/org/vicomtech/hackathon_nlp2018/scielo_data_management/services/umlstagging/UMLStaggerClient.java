package org.vicomtech.hackathon_nlp2018.scielo_data_management.services.umlstagging;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UMLStaggerClient {
	
	private String server = "http://dnlp:8090/mapper/umls?format=list&disamb=first";
	private RestTemplate rest;
	private HttpHeaders headers;

	public UMLStaggerClient() {
		rest = new RestTemplate();
	    headers = new HttpHeaders();
	    headers.add("Content-Type", "text/plain; charset=utf-8");
	    headers.add("Accept", "text/plain");
	}
	
	public String post(String text) {   
		HttpEntity<String> requestEntity = new HttpEntity<String>(text, headers);
		ResponseEntity<String> responseEntity = rest.exchange(server, HttpMethod.POST, requestEntity, String.class);
		return responseEntity.getBody();
	}
		
	public static void main(String[] args){
		UMLStaggerClient c = new UMLStaggerClient();
		String r = c.post("Me duele la rodilla derecha y soy asmática");
		System.err.println(r);
	}

}
