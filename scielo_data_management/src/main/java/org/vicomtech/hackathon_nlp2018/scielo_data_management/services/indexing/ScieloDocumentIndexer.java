package org.vicomtech.hackathon_nlp2018.scielo_data_management.services.indexing;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.springframework.stereotype.Component;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.elasticsearch.ElasticSearchClientConnectionConfig;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public @Data class ScieloDocumentIndexer {

	private final ElasticSearchClientConnectionConfig clientConnectionConfig;
	
	public void indexDocument(Map<String,Object>doc,String indexName) {
		log.info("Indexing doc...");
		IndexRequest indexRequest = new IndexRequest(indexName,"doc").source(doc);
		try {
			IndexResponse indexResponse = this.clientConnectionConfig.elasticSearchClient().index(indexRequest);
			log.info("Index response: {}",indexResponse);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
