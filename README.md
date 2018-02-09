# Búsqueda de evidencia clínica en literatura científica


Este repositorio contiene el código fuente desarrollado relativo al prototipo desarrollado por [Vicomtech](http://www.vicomtech.org) para el [Segundo Hackathon de PLN](http://www.primerhackathonpln.es/) del Plan de Impulso de las Tecnologías del Lenguaje. El contenido fue desarrollado por el equipo de compuesto por:

+ Aitor García Pablos, Naiara Pérez y Montse Cuadros.

## El prototipo 

El prototipo que se ha desarrollado quiere dar una aproximación a la problemática de la búsqueda de evidencia clínica en literatura científica. Para tal propósito se ha diseñado un demostrador que permite realizar búsquedas semánticas basadas en UMLS en un conjunto de documentos científicos del corpus SCIELO. Por un lado, el demostrador espera un texto de entrada que es analizado usando un pipeline completo de procesamiento del lenguaje natural (PLN) adaptado a la medicina y se obtienen los conceptos principales. Por otro lado, los documentos de la base de datos que se usan para consultar, han sido previamente analizados con la misma herramienta con el fin de obtener sus conceptos relacionados. Una vez el usuario introduce un texto, el buscador encuentra los documentos más relevantes, los muestra ordenados por similaridad y los puede filtrar usando los conceptos del texto de entrada. Finalmente, se ofrecen varias visualizaciones cruzadas para buscar relaciones relevantes entre conceptos, revistas, autores y fechas de publicación, entre otros.

![alt text](https://github.com/cuadros/vicomtech-evidenciamedica-hackathonPLN2018/screenshot.png "Visualización del buscador de evidencia médica")

## Tecnologías NLP usadas

Las tecnologías usadas para el prototipo son una adaptación de herramientas de procesamiento de lenguaje natural al domino de la salud. El pipeline que constituye la solución está formado por un tokenizador, analizador morfosintáctico, generador de candidatos a posibles términos basado en el análisis sintáctico y en n-gramas, un buscador alimentado con la base de conocimiento UMLS y un desambiguador basado en grafos que decide cuál de los posibles candidatos a conceptos es el más probable basado en el contexto donde aparecen estos candidatos.

La tecnología de NLP, es tecnología própia de Vicomtech que se accede mediante una API. Además de poder acceder mediante un demostrador para ver el potencial solamente de la herramienta. 

Artículo donde explica su funcionamiento:

+ Biomedical term normalization of EHRs with UMLS, Naiara Perez, Montse Cuadros and German Rigau. To appear in LREC2018. 



### Demostrador herramienta NLP

http://demos-v2.vicomtech.org/umlsmapper/

## Dependencias software

+ java8, maven3, angular5.25, elasticsearch, kibana, Spring Boot

## Ejecución

Para la ejecución del demostrador: 

+ lanzar por un lado el contenido de la carpeta [scielo_data_management](https://github.com/cuadros/vicomtech-evidenciamedica-hackathonPLN2018/tree/master/scielo_data_management)

+ desplegar el frontend contenido en la carpeta [hackathon-nlp2018-ui](https://github.com/cuadros/vicomtech-evidenciamedica-hackathonPLN2018/tree/master/hackathon-nlp2018-ui)

# Información de contacto

````shell
Aitor Garcia Pablos, Naiara Perez, Montse Cuadros
Vicomtech
20009 Donostia-San Sebastián
{agarciap, nperez, mcuadros}@vicomtech.org
````
