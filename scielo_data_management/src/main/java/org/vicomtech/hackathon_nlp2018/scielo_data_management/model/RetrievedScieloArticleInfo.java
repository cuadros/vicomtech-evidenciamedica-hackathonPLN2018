package org.vicomtech.hackathon_nlp2018.scielo_data_management.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public @Data class RetrievedScieloArticleInfo {

	private int position;
	private String identifier;
	private String titleES;
	private String titleEN;
	private List<String>authors;
	private String journal;
	private Set<String>umlsConcepts;
	
	public RetrievedScieloArticleInfo(Map<String, Object> sourceAsMap) {
		@SuppressWarnings("unchecked")
		List<String> documentTitlesES = (List<String>) sourceAsMap.get("publication_title_es");
		this.setTitleES(StringUtils.join(documentTitlesES," || "));
		@SuppressWarnings("unchecked")
		List<String> documentTitlesEN = (List<String>) sourceAsMap.get("publication_title_en");
		this.setTitleEN(StringUtils.join(documentTitlesEN," || "));
		@SuppressWarnings("unchecked")
		List<String>documentAuthors=(List<String>) sourceAsMap.get("authors");
		this.setAuthors(documentAuthors);
		@SuppressWarnings("unchecked")
		List<String>documentJournals=(List<String>) sourceAsMap.get("journal_name");
		this.setJournal(StringUtils.join(documentJournals," || "));
		@SuppressWarnings("unchecked")
		List<String>umlsConcepts=(List<String>) sourceAsMap.get("umls_concepts");
		this.umlsConcepts=Sets.newHashSet(umlsConcepts);
		
		this.identifier=(String) sourceAsMap.get("publication_id");
	}
	
}
