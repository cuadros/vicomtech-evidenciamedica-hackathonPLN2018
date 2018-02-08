package org.vicomtech.hackathon_nlp2018.scielo_data_management.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.model.ScieloDublinCoreDocument;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScieloDocumentParserTest {

	private String content;
	private String content2;
	
	@Before
	public void before() {
		String path=this.getClass().getClassLoader().getResource("S2340-98942016000400006.xml").getPath();
		String path2=this.getClass().getClassLoader().getResource("S2340-98942015000400006.xml").getPath();
		try {
			this.content=FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);
			this.content2=FileUtils.readFileToString(new File(path2), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
		public void testParseDublinCoreDocument() throws Exception {
			ScieloDocumentParser parser = new ScieloDocumentParser();
			ScieloDublinCoreDocument scieloDoc=parser.parseDublinCoreDocument(content);
			log.info(parser.print(scieloDoc));
			ScieloDublinCoreDocument scieloDoc2=parser.parseDublinCoreDocument(content2);
			log.info(parser.print(scieloDoc2));
		}

}
