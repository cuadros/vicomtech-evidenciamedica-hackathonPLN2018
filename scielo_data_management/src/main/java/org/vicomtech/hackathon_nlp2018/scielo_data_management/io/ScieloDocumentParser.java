package org.vicomtech.hackathon_nlp2018.scielo_data_management.io;

import org.vicomtech.hackathon_nlp2018.scielo_data_management.model.ScieloDublinCoreDocument;

import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class ScieloDocumentParser {

	public ScieloDublinCoreDocument parseDublinCoreDocument(String content) {
		try {
			XmlMapper xmlMapper = new XmlMapper();
			//xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			ScieloDublinCoreDocument scieloDoc = xmlMapper.readValue(content, ScieloDublinCoreDocument.class);
			return scieloDoc;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	public String print(ScieloDublinCoreDocument scieloDoc) {
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
		try {
			return xmlMapper.writeValueAsString(scieloDoc);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

}
