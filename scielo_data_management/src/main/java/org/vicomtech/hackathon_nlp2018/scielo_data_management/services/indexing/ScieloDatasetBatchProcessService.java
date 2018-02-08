package org.vicomtech.hackathon_nlp2018.scielo_data_management.services.indexing;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * The objective of this class is to fully process (and thus, index) the Scielo
 * Dataset It includes the reading/parsing each document (dublinCore metadata,
 * text and umls tagging) Index all the stuff into ElasticSearch
 * 
 * @author agarciap
 *
 */
@Slf4j
@Component
public @Data class ScieloDatasetBatchProcessService {
	
	//private String indexName;
	private final ScieloContentIndexableMapCreatorFromNaiaraTSVs scieloContentIndexableMapCreator;
	private final ScieloDocumentIndexer scieloDocumentIndexer;

	public void readProcessAndIndexFiles(String indexName, String dublinCoreFolder, String rawTextsFolder, String umlsProcessedFolder) {

		File[] dublinCoreSubfolders = new File(dublinCoreFolder).listFiles();
		for (File dublinCoreSubfolder : dublinCoreSubfolders) {
			// these are the folders with numeric name, e.g. 0211-5735
			String subfolderName = dublinCoreSubfolder.getName();
			String rawTextSubfolderPath = FilenameUtils.concat(rawTextsFolder, subfolderName);
			String umlsProcessedSubfolderPath = FilenameUtils.concat(umlsProcessedFolder, subfolderName);

			File[] dublinCoreFiles = dublinCoreSubfolder.listFiles();
			for (File dcFile : dublinCoreFiles) {
				try {
				String filename = dcFile.getName();
				// the part that identifies the fiile accros dublincore, raw and umls_processes
				String commonFilenamePart = filename.substring(0, filename.indexOf("."));
				processFilesAcross(indexName,dublinCoreSubfolder.getAbsolutePath(), rawTextSubfolderPath,
						umlsProcessedSubfolderPath, commonFilenamePart);
				}catch(Exception e) {
					log.error("Some error processing a file: {}",e.getMessage());
				}
			}

		}

	}

	private void processFilesAcross(String indexName, String dcSubfolderPath, String rawTextSubfolderPath,
			String umlsProcessedSubfolderPath, String commonFilenamePart) {
		String dcFileContent = readDCFile(dcSubfolderPath, commonFilenamePart);
		String rawTextConent = readRawTextFile(rawTextSubfolderPath, commonFilenamePart);
		List<String> umlsProcessedFilePartsContent = readUmlsProcessedFiles(umlsProcessedSubfolderPath,
				commonFilenamePart);
		
		Map<String, Object> map = this.scieloContentIndexableMapCreator.processAndCreateMapToIndex(dcFileContent, rawTextConent, umlsProcessedFilePartsContent);
		this.scieloDocumentIndexer.indexDocument(map, indexName);
	}

	private List<String> readUmlsProcessedFiles(String umlsProcessedSubfolderPath, String commonFilenamePart) {
		String completeFilenameForFull = commonFilenamePart + ".full.tsv";
		String completeFilenameForTitle = commonFilenamePart + ".title.tsv";
		String completeFilenameForAbstract = commonFilenamePart + ".abstract.tsv";
		String fullPathForFull = FilenameUtils.concat(umlsProcessedSubfolderPath, completeFilenameForFull);
		String fullPathForTitle = FilenameUtils.concat(umlsProcessedSubfolderPath, completeFilenameForTitle);
		String fullPathForAbstract = FilenameUtils.concat(umlsProcessedSubfolderPath, completeFilenameForAbstract);
		// the contents (abstract + title + full), the order should not be significant
		List<String> resultingContents = Lists.newArrayList();
		for (String path : new String[] { fullPathForAbstract, fullPathForTitle, fullPathForFull }) {
			try {
				resultingContents.add(FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8));
			} catch (Exception e) {
				log.debug("File {} does not exist (it may happen, no problem)", path);
			}
		}
		return resultingContents;
	}

	private String readRawTextFile(String rawTextSubfolderPath, String commonFilenamePart) {
		String completeFilename = commonFilenamePart + ".txt";
		String fullPath = FilenameUtils.concat(rawTextSubfolderPath, completeFilename);
		try {
			return FileUtils.readFileToString(new File(fullPath), StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String readDCFile(String dcSubfolderPath, String commonFilenamePart) {
		String completeFilename = commonFilenamePart + ".xml";
		String fullPath = FilenameUtils.concat(dcSubfolderPath, completeFilename);
		try {
			return FileUtils.readFileToString(new File(fullPath), StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
