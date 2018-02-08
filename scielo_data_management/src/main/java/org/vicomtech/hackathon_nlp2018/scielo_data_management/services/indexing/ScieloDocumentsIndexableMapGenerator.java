package org.vicomtech.hackathon_nlp2018.scielo_data_management.services.indexing;

import java.util.Map;

import org.vicomtech.hackathon_nlp2018.scielo_data_management.model.ScieloDublinCoreDocument;

import com.google.common.collect.Maps;

public class ScieloDocumentsIndexableMapGenerator {
	
	public Map<String,Object> obtainIndexableMap(ScieloDublinCoreDocument scieloDublinCoreDocument) {
		
		Map<String,Object>mapToIndex=Maps.newHashMap();
		mapToIndex.put("creators", scieloDublinCoreDocument.getRecord().getMetadata().getOaiDC().getDcCreators());
		mapToIndex.put("subjects", scieloDublinCoreDocument.getRecord().getMetadata().getOaiDC().getDcSubjects());
		mapToIndex.put("publishers", scieloDublinCoreDocument.getRecord().getMetadata().getOaiDC().getDcPublishers());
		mapToIndex.put("type", scieloDublinCoreDocument.getRecord().getMetadata().getOaiDC().getDcTypes().get(0));
		mapToIndex.put("languages", scieloDublinCoreDocument.getRecord().getMetadata().getOaiDC().getDcLanguages());
		return mapToIndex;
	}
	
}
