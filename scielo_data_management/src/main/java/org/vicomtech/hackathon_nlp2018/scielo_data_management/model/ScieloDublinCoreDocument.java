package org.vicomtech.hackathon_nlp2018.scielo_data_management.model;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import lombok.Data;
import lombok.ToString;

@ToString
public @Data class ScieloDublinCoreDocument {

	private Record record;
	
	public static @Data class Record{
		private Header header;
		private Metadata metadata;
		
	}
	
	public static @Data class Header{
		@JacksonXmlProperty(localName = "identifier")
		private String identifier;
		@JacksonXmlProperty(localName = "datestamp")
		private String datestamp;
		@JacksonXmlProperty(localName = "setSpec")
		private String setSpec;
		
	}

	public static @Data class Metadata{
		@JacksonXmlProperty(namespace="oai-dc",localName = "dc")
		private OaiDC oaiDC;
	}
	
	public static @Data class OaiDC{
		
		private String schemaLocation;
		
		@JacksonXmlElementWrapper(namespace="dc", localName = "creators",useWrapping = false)
		@JacksonXmlProperty(localName="creator")
		private List<String> dcCreators;
		@JacksonXmlElementWrapper(namespace="dc",localName = "subjects",useWrapping = false)
		@JacksonXmlProperty(localName="subject")
		private List<String> dcSubjects;
		@JacksonXmlElementWrapper(namespace="dc",localName = "publishers",useWrapping = false)
		@JacksonXmlProperty(localName="publisher")
		private List<String> dcPublishers;
		@JacksonXmlElementWrapper(namespace="dc",localName = "sources",useWrapping = false)
		@JacksonXmlProperty(localName="source")
		private List<String> dcSources;
		@JacksonXmlElementWrapper(namespace="dc",localName = "dates",useWrapping = false)
		@JacksonXmlProperty(localName="date")
		private List<String> dcDates;
		@JacksonXmlElementWrapper(namespace="dc",localName = "types",useWrapping = false)
		@JacksonXmlProperty(localName="type")
		private List<String> dcTypes;
		@JacksonXmlElementWrapper(namespace="dc",localName = "formats",useWrapping = false)
		@JacksonXmlProperty(localName="format")
		private List<String> dcFormats;
		@JacksonXmlElementWrapper(namespace="dc",localName = "identifiers",useWrapping = false)
		@JacksonXmlProperty(localName="identifier")
		private List<String> dcIdentifiers;
		@JacksonXmlElementWrapper(namespace="dc",localName = "languages",useWrapping = false)
		@JacksonXmlProperty(localName="language")
		private List<String> dcLanguages;
		@JacksonXmlElementWrapper(namespace="dc",localName = "titles",useWrapping = false)
		@JacksonXmlProperty(localName="title")
		private List<DCTitle> dcTitles;
		@JacksonXmlElementWrapper(namespace="dc",localName = "descriptions",useWrapping = false)
		@JacksonXmlProperty(localName="description")
		private List<String> dcDescriptions;
		@JacksonXmlElementWrapper(namespace="dc",localName = "rights",useWrapping = false)
		@JacksonXmlProperty(localName="rights")
		private List<String> dcRights;
	}
	
	public static @Data class DCTitle {

		@JacksonXmlProperty(isAttribute = true)
		private String lang;
		@JacksonXmlText
		private String value;
	}
	
	
	/*
	 * 
	 * <dc:creator><![CDATA[GonzÃ¡lez-Pumariega,Maribel]]></dc:creator>
<dc:creator><![CDATA[Fuentes-LeÃ³n,Fabiana]]></dc:creator>
<dc:creator><![CDATA[Vernhes,Marioly]]></dc:creator>
<dc:creator><![CDATA[Schuch,AndrÃ© P.]]></dc:creator>
<dc:creator><![CDATA[Martins Menck,Carlos Frederico]]></dc:creator>
<dc:creator><![CDATA[SÃ¡nchez-Lamar,Ãngel]]></dc:creator>
<dc:subject><![CDATA[extracto vegetal]]></dc:subject>
<dc:subject><![CDATA[fotoprotecciÃ³n]]></dc:subject>
<dc:subject><![CDATA[UV]]></dc:subject>
<dc:subject><![CDATA[dÃ­meros de pirimidinas]]></dc:subject>
<dc:publisher><![CDATA[Universidad de Granada]]></dc:publisher>
<dc:source><![CDATA[Ars Pharmaceutica (Internet)  v.57 n.4 2016]]></dc:source>
<dc:date>2016-12-01</dc:date>
<dc:type>journal article</dc:type>
<dc:format>text/html</dc:format>
<dc:identifier>http://scielo.isciii.es/scielo.php?script=sci_arttext&amp;pid=S2340-98942016000400006</dc:identifier>
<dc:language>es</dc:language>
<dc:title xml:lang="es"><![CDATA[El extracto acuoso de Cymbopogon citratus protege al ADN plasmÃ­dico del daÃ±o inducido por radiaciÃ³n UVC]]></dc:title>
<dc:title xml:lang="en"><![CDATA[Cymbopogon citratus aqueous extract protects plasmid DNA from UVC-induced damage]]></dc:title>
<dc:description xml:lang="es"><![CDATA[Objetivo: Evaluar el efecto protector del extracto acuoso de Cymbopogon citratus (DC) Stapf, ante el daÃ±o inducido por las radiaciones UVC. Material y MÃ©todos: Para evaluar si el extracto acuoso de C. citratus era capaz de inducir roturas de cadenas en el ADN, molÃ©culas de plÃ¡smido pBluescript SK II fueron tratadas con diferentes concentraciones del extracto (0,01 - 4,0 mg/mL), en los tiempos de exposiciÃ³n: 30, 60 y 90 min. El efecto fotoprotector fue evaluado aplicando el extracto vegetal antes, durante, y despuÃ©s de la irradiaciÃ³n del ADN plasmÃ­dico con 200 J/mÂ² de UVC. La actividad enzimÃ¡tica de T4 endonucleasa V fue empleada para detectar formaciÃ³n de CPDs. Las formas superenrollada y relajada de las molÃ©culas de plÃ¡smido fueron separadas electroforÃ©ticamente en gel de agarosa. Adicionalmente, se midiÃ³ la transmitancia del extracto acuoso a la DO de 254 nm. Resultados: Ninguna de las concentraciones evaluadas resultÃ³ genotÃ³xica con 30 min de tratamiento. Las concentraciones &#8805; 2 mg/mL indujeron roturas de cadenas a los 90 min de incubaciÃ³n. El extracto de C. citratus a concentraciones &#8805; 0,5 mg/mL protegiÃ³ al ADN frente a las radiaciones UVC. Conclusiones: En nuestras condiciones experimentales, el extracto acuoso de C. citratus protege al ADN frente a la genotoxicidad inducida por la luz UVC, previniendo la generaciÃ³n de CPDs, pero no es capaz de eliminarlas una vez formadas.]]></dc:description>
<dc:description xml:lang="en"><![CDATA[Aim: to evaluate the photoprotective effect of aqueous extract of Cymbopogon citratus (DC) Stapf against UVC-induced damage to ADN. Material and methods: In the experimental procedure, samples of plasmid pBluescript SK II solutions were exposed to C. citratus aqueous extract in 0.01-4.0 mg/mL concentrations during 30, 60 and 90 min. In order to evaluate the photoprotective effect, the vegetal extract was applied before, during and after UVC radiation at 200 J/mÂ² doses. DNA repair enzymes T4 endonuclease V was employed in order to discriminate CPDs damage. Then, supercoiled and relaxed forms of DNA were separated after electrophoretic migration in agarose gels. Also aqueous extract transmittance was measure at 254 nm OD. Results: None of the concentrations tested were genotoxic in 30 min of exposition. Concentrations &#8805; 2 mg/mL induced strand breaks at 90 min of incubation. The C. citratus extract at concentrations &#8805; 0.5 mg/mL protect DNA in front of UVC radiation. Conclusions: In our experimental conditions, C. citratus extract protects DNA from the genotoxicity induced by light UVC, preventing the CPDs generation, but is not able to eliminate DNA damage once formed.]]></dc:description>
<dc:rights>https://creativecommons.org/licenses/by-nc-nd/2.0/</dc:rights>

	 * 
	 */
	
	
}
