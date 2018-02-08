package org.vicomtech.hackathon_nlp2018.scielo_data_management.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

public @Data class UMLSMetadata {

	@JsonProperty("CUI")
	private String cui;
	private String preferredTerm;
	private List<String>definitions;
	private List<String>semanticTypes;
	
}
