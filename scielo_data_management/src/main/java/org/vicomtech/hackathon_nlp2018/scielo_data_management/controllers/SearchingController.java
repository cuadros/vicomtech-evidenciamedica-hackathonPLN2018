package org.vicomtech.hackathon_nlp2018.scielo_data_management.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.model.RetrievedScieloArticleInfo;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.model.TextSearchResponse;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.model.UMLSMetadata;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.services.search.ScieloQueryBuilder;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.services.umlstagging.UMLSServicesClient;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.services.umlstagging.UMLStaggerClient;

import com.google.common.collect.Lists;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/searching")
@Slf4j
public @Data class SearchingController {

	private static final String INDEX_NAME = "scielo7";
	private final RestHighLevelClient client;
	private final ScieloQueryBuilder scieloQueryBuilder;
	private final UMLStaggerClient umlsTaggerClient;
	private final UMLSServicesClient umlsServicesClient;

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "searchText", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<TextSearchResponse> search(@RequestParam("text-to-search") String textToSearch,
			@RequestParam(name = "page-number", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "page-size", required = false, defaultValue = "10") int pageSize) {

		log.info("Received call for search with text: {}", textToSearch);
		String codes = umlsTaggerClient.post(textToSearch);
		log.info("UMLS codes returned from UMLS mapper: {}", codes);
		return searchTextWithUMLSCodes(textToSearch, codes, pageNumber, pageSize);

	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "searchTextWithUMLSCodes", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<TextSearchResponse> searchTextWithUMLSCodes(
			@RequestParam(name = "text-to-search", required = false, defaultValue = "") String textToSearch,
			@RequestParam(name = "umls-codes", defaultValue = "") @ApiParam("Comma separated UMLS codes") String commaSeparatedUMLSCodes,
			@RequestParam(name = "page-number", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "page-size", required = false, defaultValue = "10") int pageSize) {

		log.info("Search text with umls codes received: {}, umlsCodes:{}", textToSearch, commaSeparatedUMLSCodes);

		SearchRequest searchRequest = scieloQueryBuilder.buildScieloSearchRequest(INDEX_NAME, textToSearch,
				commaSeparatedUMLSCodes, pageNumber, pageSize);

		try {
			SearchResponse searchResponse = client.search(searchRequest);
			SearchHits hits = searchResponse.getHits();
			List<RetrievedScieloArticleInfo> results = Lists.newArrayList();

			SearchHit[] searchHits = hits.getHits();
			int count = 0;
			for (SearchHit hit : searchHits) {
				count++;
				// do something with the SearchHit
				Map<String, Object> sourceAsMap = hit.getSourceAsMap();
				RetrievedScieloArticleInfo retrievedArticleInfo = new RetrievedScieloArticleInfo(sourceAsMap);
				retrievedArticleInfo.setPosition(pageNumber * pageSize + count);
				results.add(retrievedArticleInfo);
			}
			results = results.stream().filter(x -> x != null).collect(Collectors.toList());

			// build response
			TextSearchResponse textSearchResponse = new TextSearchResponse();
			textSearchResponse.setText(textToSearch);
			textSearchResponse.setPageNumber(pageNumber);
			textSearchResponse.setPageSize(pageSize);
			textSearchResponse.setTotalHits(hits.getTotalHits());
			textSearchResponse.setResults(results);
			textSearchResponse.setRequestedUmlsCodes(scieloQueryBuilder.splitCodes(commaSeparatedUMLSCodes));
			return new ResponseEntity<TextSearchResponse>(textSearchResponse, HttpStatus.ACCEPTED);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "retrieveUMLSMetadata", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<UMLSMetadata> retrieveUMLSMetadata(
			@RequestParam("umlsCode")String umlsCode
			){
		log.info("Retrieving UMLS metadata...");
		UMLSMetadata umlsMetadata = this.umlsServicesClient.retrieveMetadataForUMLSCode(umlsCode);
		return new ResponseEntity<UMLSMetadata>(umlsMetadata, HttpStatus.ACCEPTED);
	}
			

}
