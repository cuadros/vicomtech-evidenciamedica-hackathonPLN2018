package org.vicomtech.hackathon_nlp2018.scielo_data_management.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.io.ScieloDocumentParser;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.model.RetrievedScieloArticleInfo;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.model.ScieloDublinCoreDocument;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.model.TextSearchResponse;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.services.indexing.ScieloDatasetBatchProcessService;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.services.indexing.ScieloDocumentsIndexableMapGenerator;

import com.google.common.collect.Lists;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/main-for-test")
@Slf4j
@Deprecated
public @Data class MainCrontroller {

	private final RestHighLevelClient client;
	private final ScieloDatasetBatchProcessService scieloDatasetBatchProcessService;

	@RequestMapping(value = "indexScieloDublinCoreDocument", method = RequestMethod.POST)
	public @ResponseBody String indexScieloDublinCoreDocument(@RequestBody String somethingToIndex) {
		try {
			ScieloDocumentParser scieloDocumentParser = new ScieloDocumentParser();
			ScieloDublinCoreDocument scieloDoc = scieloDocumentParser.parseDublinCoreDocument(somethingToIndex);

			// ObjectMapper mapper = new ObjectMapper();
			// String json = mapper.writeValueAsString(scieloDoc);

			// Map<String,String>map=Maps.newHashMap();
			// map.put("id", scieloDoc.getRecord().getHeader().getIdentifier());
			// map.put("title",
			// scieloDoc.getRecord().getMetadata().getOaiDC().getDcTitles().get(0));

			// log.info("Object map: \n{}",map);

			// RestHighLevelClient client = new RestHighLevelClient(
			// RestClient.builder(new HttpHost("deepnlp", 9200, "http")));

			ScieloDocumentsIndexableMapGenerator scieloDocumentsIndexableMapGenerator = new ScieloDocumentsIndexableMapGenerator();
			Map<String, Object> map = scieloDocumentsIndexableMapGenerator.obtainIndexableMap(scieloDoc);
			IndexRequest indexRequest = new IndexRequest("scielo", "dc",
					scieloDoc.getRecord().getHeader().getIdentifier()).source(map);

			IndexResponse indexResponse = client.index(indexRequest);
			log.info("IndexResponse: {}", indexResponse);

			// client.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return "done...?";
	}

	@RequestMapping(value = "indexScieloDcDocs", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> indexScieloDcDocs(
			@RequestParam("folder-with-docs") String folderWithDocs,
			@RequestParam(value = "index-name", defaultValue = "scielo") String index,
			@RequestParam(value = "docs-type", defaultValue = "dc") String type) {
		File[] files = new File(folderWithDocs).listFiles();
		ScieloDocumentParser scieloDocumentParser = new ScieloDocumentParser();
		ScieloDocumentsIndexableMapGenerator scieloDocumentsIndexableMapGenerator = new ScieloDocumentsIndexableMapGenerator();
		BulkRequest bulkRequest = new BulkRequest();
		for (File f : files) {
			try {
				String content = FileUtils.readFileToString(f, StandardCharsets.UTF_8);
				ScieloDublinCoreDocument scieloDublinCoreDocument = scieloDocumentParser
						.parseDublinCoreDocument(content);
				Map<String, Object> map = scieloDocumentsIndexableMapGenerator
						.obtainIndexableMap(scieloDublinCoreDocument);
				IndexRequest indexRequest = new IndexRequest(index, type,
						scieloDublinCoreDocument.getRecord().getHeader().getIdentifier()).source(map);
				bulkRequest.add(indexRequest);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}
		try {
			BulkResponse bulkResponse = client.bulk(bulkRequest);
			return new ResponseEntity<String>(bulkResponse.toString(), HttpStatus.ACCEPTED);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@RequestMapping(value = "indexScieloCorpus", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> indexScieloCorpus(
			@RequestParam("index-name") String indexName,
			@RequestParam("folder-with-dublincore") String folderWithDC,
			@RequestParam("folder-with-rawtext") String folderWithRawText,
			@RequestParam("folder-with-umls") String folderWithUmls) {

		log.info("Starting index process... (long stuff probably...)");
		this.scieloDatasetBatchProcessService.readProcessAndIndexFiles(indexName,folderWithDC, folderWithRawText, folderWithUmls);

		return new ResponseEntity<String>("Process finished...", HttpStatus.ACCEPTED);
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "searchText", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<TextSearchResponse>search(
			@RequestParam("text-to-search")String textToSearch,
			@RequestParam(name="page-number",required=false,defaultValue="0")int pageNumber,
			@RequestParam(name="page-size",required=false,defaultValue="10")int pageSize){
		
		log.info("Search text received: {}",textToSearch);
		
		SearchRequest searchRequest = new SearchRequest("scielo3"); 
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.queryStringQuery(textToSearch));
		searchSourceBuilder.from(pageNumber*pageSize);
		searchSourceBuilder.size(pageSize);
		searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
		searchRequest.source(searchSourceBuilder);
		
		try {
			SearchResponse searchResponse = client.search(searchRequest);
			SearchHits hits = searchResponse.getHits();
			List<RetrievedScieloArticleInfo>results=Lists.newArrayList();
			
			SearchHit[] searchHits = hits.getHits();
			int count=0;
			for (SearchHit hit : searchHits) {
				count++;
			    // do something with the SearchHit
				Map<String, Object> sourceAsMap = hit.getSourceAsMap();
				RetrievedScieloArticleInfo retrievedArticleInfo=new RetrievedScieloArticleInfo();
				@SuppressWarnings("unchecked")
				List<String> documentTitles = (List<String>) sourceAsMap.get("publication_title");
				retrievedArticleInfo.setTitleES(StringUtils.join(documentTitles," || "));
				@SuppressWarnings("unchecked")
				List<String>documentAuthors=(List<String>) sourceAsMap.get("authors");
				retrievedArticleInfo.setAuthors(documentAuthors);
				@SuppressWarnings("unchecked")
				List<String>documentJournals=(List<String>) sourceAsMap.get("journal_name");
				retrievedArticleInfo.setJournal(StringUtils.join(documentJournals," || "));
				retrievedArticleInfo.setPosition(pageNumber*pageSize+count);
				results.add(retrievedArticleInfo);
			}
			results=results.stream().filter(x->x!=null).collect(Collectors.toList());
			
			TextSearchResponse textSearchResponse=new TextSearchResponse();
			textSearchResponse.setText(textToSearch);
			textSearchResponse.setPageNumber(pageNumber);
			textSearchResponse.setPageSize(pageSize);
			textSearchResponse.setTotalHits(hits.getTotalHits());
			textSearchResponse.setResults(results);
			
//			hits.iterator().forEachRemaining(x->results.add(x.getFields().get("publication_title").getValue().toString()));
			return new ResponseEntity<TextSearchResponse>(textSearchResponse,HttpStatus.ACCEPTED);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
}
