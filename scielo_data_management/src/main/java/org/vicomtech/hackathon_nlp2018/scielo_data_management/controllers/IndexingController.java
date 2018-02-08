package org.vicomtech.hackathon_nlp2018.scielo_data_management.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.io.ScieloDocumentParser;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.model.ScieloDublinCoreDocument;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.services.indexing.ScieloDatasetBatchProcessService;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.services.indexing.ScieloDocumentsIndexableMapGenerator;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/indexing")
@Slf4j
public @Data class IndexingController {

	private final RestHighLevelClient client;
	private final ScieloDatasetBatchProcessService scieloDatasetBatchProcessService;

	@Deprecated
	@RequestMapping(value = "indexScieloDublinCoreDocument", method = RequestMethod.POST)
	public @ResponseBody String indexScieloDublinCoreDocument(@RequestBody String somethingToIndex) {
		try {
			ScieloDocumentParser scieloDocumentParser = new ScieloDocumentParser();
			ScieloDublinCoreDocument scieloDoc = scieloDocumentParser.parseDublinCoreDocument(somethingToIndex);

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

	@Deprecated
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
	
}
