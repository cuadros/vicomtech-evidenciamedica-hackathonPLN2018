package org.vicomtech.hackathon_nlp2018.scielo_data_management.services.indexing;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jdom2.JDOMException;
import org.springframework.stereotype.Component;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.io.ScieloDocumentParser;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.model.ScieloDublinCoreDocument;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.model.UMLSMetadata;
import org.vicomtech.hackathon_nlp2018.scielo_data_management.services.umlstagging.UMLSServicesClient;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import ixa.kaflib.ExternalRef;
import ixa.kaflib.KAFDocument;
import ixa.kaflib.Term;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Deprecated
public @Data class ScieloContentIndexableMapCreator {

	private final UMLSServicesClient umlsServicesClient;
	
	private ScieloDocumentParser scieloDocumentParser = new ScieloDocumentParser();

	/**
	 * The objective is to build a map with all the data that we want to index
	 * 
	 * @param dublinCoreContent
	 * @param rawContent
	 * @param umlsProcessedContents
	 */
	public Map<String, Object> processAndCreateMapToIndex(String dublinCoreContent, String rawContent,
			List<String> umlsProcessedContents) {

		Map<String, Object> mapToIndex = Maps.newHashMap();

		ScieloDublinCoreDocument scieloDCDoc = scieloDocumentParser.parseDublinCoreDocument(dublinCoreContent);
		mapToIndex.put("publication_id", scieloDCDoc.getRecord().getHeader().getIdentifier());
		mapToIndex.put("journal_id", scieloDCDoc.getRecord().getHeader().getSetSpec());
		mapToIndex.put("journal_name", scieloDCDoc.getRecord().getMetadata().getOaiDC().getDcSources());
		mapToIndex.put("publisher", scieloDCDoc.getRecord().getMetadata().getOaiDC().getDcPublishers());
		mapToIndex.put("publication_title_es", scieloDCDoc.getRecord().getMetadata().getOaiDC().getDcTitles().stream().filter(x->x.getLang().equals("es")).map(x->x.getValue()).collect(Collectors.toList()));
		mapToIndex.put("publication_title_en", scieloDCDoc.getRecord().getMetadata().getOaiDC().getDcTitles().stream().filter(x->x.getLang().equals("en")).map(x->x.getValue()).collect(Collectors.toList()));
		mapToIndex.put("publication_type", scieloDCDoc.getRecord().getMetadata().getOaiDC().getDcTypes());
		mapToIndex.put("authors", scieloDCDoc.getRecord().getMetadata().getOaiDC().getDcCreators());
		mapToIndex.put("subjects", scieloDCDoc.getRecord().getMetadata().getOaiDC().getDcSubjects());
		mapToIndex.put("publication_date", parseDate(scieloDCDoc.getRecord().getMetadata().getOaiDC().getDcDates().get(0)));
		mapToIndex.put("publication_description", scieloDCDoc.getRecord().getMetadata().getOaiDC().getDcDescriptions());
		mapToIndex.put("publication_language", scieloDCDoc.getRecord().getMetadata().getOaiDC().getDcLanguages());
		mapToIndex.put("rights", scieloDCDoc.getRecord().getMetadata().getOaiDC().getDcRights());

		// raw content
		mapToIndex.put("publication_text", rawContent);

		// umls stuff
		//a set instead of a list, to avoid repetitions
		Set<String>umlsConcepts=Sets.newHashSet();
		for (String umlsContent : umlsProcessedContents) {
			log.info("Parsing NAF with umls info...");
			try {
				KAFDocument kafDocument = KAFDocument.createFromStream(new StringReader(umlsContent));
				for (Term term : kafDocument.getTerms()) {
					if(term.getExternalRefs()!=null) {
						for(ExternalRef externalRef:term.getExternalRefs()) {
							String resource=externalRef.getResource();
							String ref=externalRef.getReference(); //this is what we want, the reference
							//String source=externalRef.getSource();
							///////
							// We would like to filter the semantic_type of the concepts
							// For that we could perform a web service query here, but for simplicity we leave it out for now
							
							//this is to filter the attributes
							if(resource.toLowerCase().contains("umls")) {
								umlsConcepts.add(ref);
							}
						}
					}
				}
			} catch (IOException | JDOMException e) {
				throw new RuntimeException(e);
			}
		}		
		mapToIndex.put("umls_concepts", umlsConcepts);
		
		////////////////////////////////
		// Add umls concept names
		////////////////////////////////
		List<String>umlsConceptNames=Lists.newArrayList();
		for(String umlsConcept:umlsConcepts) {
			UMLSMetadata umlsMetadata = this.umlsServicesClient.retrieveMetadataForUMLSCode(umlsConcept);
			umlsConceptNames.add(umlsMetadata.getPreferredTerm());
		}
		mapToIndex.put("umls_concepts_names", umlsConceptNames);
		/////////////////////////////////
		return mapToIndex;
	}

	/*
	 * publicacion id journal id nombre journal (source) publisher titulo
	 * publicacion autores subjects date (la del articulo, dc:date, no la de scielo)
	 * descripcion tipo de publicacion idiomas licencia (rights)
	 * 
	 * el texto raw entero
	 * 
	 * y de los kaf del umls, los terms con external-ref (ante la duda preguntar a
	 * Naiara)
	 * 
	 * 
	 */

	private Date parseDate(String dateStr) {
		DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		try {
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
}
