package org.vicomtech.hackathon_nlp2018.scielo_data_management.model;

import java.util.List;

import lombok.Data;

public @Data class TextSearchResponse {

	private String text;
	private int pageNumber;
	private int pageSize;
	private long totalHits;
	private List<RetrievedScieloArticleInfo>results;
	private List<String>requestedUmlsCodes;
	
}
