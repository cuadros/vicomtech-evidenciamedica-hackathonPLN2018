package org.vicomtech.hackathon_nlp2018.scielo_data_management.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchClientConnectionConfig {

	@Bean
	public RestHighLevelClient elasticSearchClient(){
		RestHighLevelClient client = new RestHighLevelClient(
				RestClient.builder(new HttpHost("deepnlp", 9200, "http")));
		return client;
	}
	
}
