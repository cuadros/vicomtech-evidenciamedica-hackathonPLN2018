package org.vicomtech.hackathon_nlp2018.scielo_data_management.services.search;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.action.search.SearchRequest;
//import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ScieloQueryBuilder {

	public SearchRequest buildScieloSearchRequest(String indexName,String textToSearch,String commaSeparatedUMLSCodes,int pageNumber,int pageSize) {
		
		SearchRequest searchRequest = new SearchRequest(indexName); 
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
		////////////////////////////////////////////////////////////////////
//		BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
//		List<String>umlsCodes=splitCodes(commaSeparatedUMLSCodes);
//		//if there is text, or if there are no umls codes
//		if(textToSearch.trim().length()>0 || commaSeparatedUMLSCodes.trim().length()==0){
//			booleanQuery.should(QueryBuilders.matchQuery("publication_text", textToSearch));
//		}
//		for(String umlsCode:umlsCodes) {
//			log.info("Adding umls code to query: {}",umlsCode);
//			//this is not working, it filters nothing...
//			booleanQuery.should(QueryBuilders.matchQuery("umls_concepts", umlsCode));
//		}
		
		///////////
		//REMOVE THIS; JUST A TEST
		//searchSourceBuilder.query(QueryBuilders.termQuery("umls_concepts", "C0032863"));
		///////////
		/////////////////////////////////////////////////////////////////////
		String builtQuery=buildQuery(textToSearch, splitCodes(commaSeparatedUMLSCodes));
		QueryStringQueryBuilder query = QueryBuilders.queryStringQuery(builtQuery);
		/////////////////////////////////////////////////////////////////////
		log.info("Built query: {}",builtQuery);
		searchSourceBuilder.query(query);
		searchSourceBuilder.from(pageNumber*pageSize);
		searchSourceBuilder.size(pageSize);
		searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
		searchRequest.source(searchSourceBuilder);
		
		log.info(searchRequest.toString());
		
		return searchRequest;
	}
	
	private String buildQuery(String text,List<String>umlsCodes) {
		StringBuilder sb=new StringBuilder();
		sb.append("publication_text:").append(text.replace("\"", ""));//.append("^2");
		if(!umlsCodes.isEmpty()) {
			sb.append(" (");
		}
		for(String umlsCode:umlsCodes) {
			if(umlsCodes.indexOf(umlsCode)==0) {
				sb.append("umls_concepts:").append(umlsCode);
			}else {
				sb.append(" AND umls_concepts:").append(umlsCode);
			}			
		}
		if(!umlsCodes.isEmpty()) {
			sb.append(")").append("^2");
		}
		return sb.toString();
	}

	public List<String> splitCodes(String commaSeparatedUMLSCodes) {
		return Arrays.asList(commaSeparatedUMLSCodes.trim().split(",")).stream().filter(x->x.trim().length()>0).collect(Collectors.toList());
	}
	
}
