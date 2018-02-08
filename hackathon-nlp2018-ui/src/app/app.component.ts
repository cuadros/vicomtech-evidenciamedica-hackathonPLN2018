import {ApiServicesService} from './shared/api-services.service';
import {TextSearchResponse} from './model/textsearchresponse';
import {UmlsMetadata} from './model/umlsmetadata';
import {CapitalizePipe} from './pipes/capitalize.pipe';
import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {catchError, map, tap} from 'rxjs/operators';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {


  private luceneQuery = '';
  //  private piechartSrcTemplate = 'http://deepnlp:5601/app/kibana#/visualize/create?embed=true' +
  //  '&type=pie&indexPattern=5bffd380-0bec-11e8-8ca4-074fff1e7b4d' +
  //  '&_g=()' +
  //  '&_a=(filters:!()' +
  //  ',linked:!f,' +
  //  'query:(language:lucene,query:\'$$LUCENE_QUERY$$\')' +
  //  ',uiState:()' +
  //  ',vis:(aggs:!((enabled:!t,id:\'1\',params:()' +
  //  ',schema:metric,type:count)' +
  //  ',(enabled:!t,id:\'2\',params:(field:subjects.keyword,order:desc,orderBy:\'1\',size:10),schema:segment,type:terms))' +
  //  ',params:(addLegend:!t,addTooltip:!t,isDonut:!t,labels:(last_level:!t,show:!f,truncate:100,values:!t)' +
  // ',legendPosition:right,type:pie)' +
  //  ',title:\'New+Visualization\',type:pie))';
  private piechartSrcTemplate = 'http://deepnlp:5601/app/kibana#/visualize/edit/bef19920-0c0e-11e8-8ca4-074fff1e7b4d?embed=true' +
  '&_g=()&_a=(filters:!(),linked:!f,query:(language:lucene,query:\'$$LUCENE_QUERY$$\')' +
  ',uiState:(),vis:(aggs:!((enabled:!t,id:\'1\',params:()' +
  ',schema:metric,type:count),(enabled:!t,id:\'2\',params:(customLabel:\'Palabras+clave+del+art%C3%ADculo\'' +
  ',field:subjects.keyword,order:desc,orderBy:\'1\',size:15),schema:segment,type:terms))' +
  ',params:(addLegend:!t,addTooltip:!t,isDonut:!t,labels:(last_level:!t,show:!t,truncate:100,values:!t)' +
  ',legendPosition:right,type:pie),title:rosquilla_scielo7,type:pie))';
  piechartSrc: string;

  private datehistogramSrcTemplate = 'http://deepnlp:5601/app/kibana#/visualize/edit/b3ed9250-0bfe-11e8-8ca4-074fff1e7b4d?embed=true' +
  '&_g=()' +
  '&_a=(filters:!(),linked:!f,query:(language:lucene,query:\'$$LUCENE_QUERY$$\')' +
  ',uiState:(),' +
  'vis:(aggs:!((enabled:!t,id:\'1\',params:(),schema:metric,type:count),' +
  '(enabled:!t,id:\'2\',params:(customInterval:\'2h\',extended_bounds:(),' +
  'field:publication_date,interval:w,min_doc_count:1),schema:segment,type:date_histogram)),' +
  'params:(addLegend:!t,addTimeMarker:!f,addTooltip:!t,categoryAxes:!((id:CategoryAxis-1,labels:(show:!t,truncate:100),' +
  'position:bottom,scale:(type:linear),show:!t,style:(),title:(),type:category)),' +
  'grid:(categoryLines:!f,style:(color:%23eee))' +
  ',legendPosition:right,seriesParams:!((data:(id:\'1\',label:Count)' +
  ',drawLinesBetweenPoints:!t,mode:normal,show:true,showCircles:!t,type:line,valueAxis:ValueAxis-1))' +
  ',times:!(),type:line,valueAxes:!((id:ValueAxis-1,labels:(filter:!f,rotate:0,show:!t,truncate:100)' +
  ',name:LeftAxis-1,position:left,scale:(mode:normal,type:linear),show:!t,style:(),title:(text:Count),type:value)))' +
  ',title:histograma_fechas,type:line))';
  datehistogramSrc: string;

  //  private tagcloudSrcTemplate = 'http://deepnlp:5601/app/kibana#/visualize/edit/a0b4c8f0-0bec-11e8-8ca4-074fff1e7b4d?embed=true' +
  //  '&_g=()&_a=(filters:!(),linked:!f,query:(language:lucene,query:\'$$LUCENE_QUERY$$\')' +
  //  ',uiState:(),vis:(aggs:!((enabled:!t,id:\'1\',params:(),schema:metric,type:count)' +
  //  ',(enabled:!t,id:\'2\',params:(field:umls_concepts_names.keyword,order:desc,orderBy:\'1\',size:30)' +
  //  ',schema:segment,type:terms)),params:(maxFontSize:72,minFontSize:18,orientation:single,scale:linear)' +
  //  ',title:tagcloud_scielo7,type:tagcloud))';
  private tagcloudSrcTemplate = 'http://deepnlp:5601/app/kibana#/visualize/edit/55b3d280-0c08-11e8-8ca4-074fff1e7b4d?embed=true' +
  '&_g=()&_a=(filters:!(),linked:!f,query:(language:lucene,query:\'$$LUCENE_QUERY$$\')' +
  ',uiState:(vis:(defaultColors:(\'0+-+2\':\'rgb(247,252,245)\',\'2+-+3\':\'rgb(199,233,192)\',\'3+-+5\':\'rgb(116,196,118)\'' +
  ',\'5+-+6\':\'rgb(35,139,69)\'))),vis:(aggs:!((enabled:!t,id:\'1\',params:(),schema:metric,type:count)' +
  ',(enabled:!t,id:\'2\',params:(exclude:\'(.*selenio.*)%7C(.*Poder.*)%7C(.*Cartas.*)%7C(.*NOS.*)\',' +
  'field:umls_concepts_names.keyword,order:desc,orderBy:\'1\',size:5)' +
  ',schema:segment,type:terms),(enabled:!t,id:\'3\',params:(field:authors.keyword,order:desc,orderBy:\'1\',size:5)' +
  ',schema:group,type:terms)),params:(addLegend:!t,addTooltip:!t,colorSchema:Greens,colorsNumber:4,colorsRange:!()' +
  ',enableHover:!f,invertColors:!f,legendPosition:right,percentageMode:!f,setColorRange:!f,times:!()' +
  ',type:heatmap,valueAxes:!((id:ValueAxis-1,labels:(color:%23555,rotate:0,show:!f)' +
  ',scale:(defaultYExtents:!f,type:linear),show:!f,type:value))),title:heatmap_scielo7,type:heatmap))';
  tagcloudSrc: string;


  private BLACKLISTED_SEMANTIC_TYPES = [
    'Qualitative Concept',
    'Quantitative Concept',
    'Functional Concept',
    'Spatial Concept',
    'Temporal Concept'];

  private BLACKLISTED_UMLS_CODES = ['C1272751', 'C0557651'];

  constructor(private apiService: ApiServicesService, public sanitizer: DomSanitizer) {}

  title = 'app';
  textSearchResponse: TextSearchResponse = null;
  detectedUMLCodesMetadata: UmlsMetadata[] = [];
  selectedUMLSConceptMetadata: UmlsMetadata;
  lastPage: number;
  pageNumbers: number[];
  activeVis = 1;

  ngOnInit(): void {
    // throw new Error('Method not implemented.');
    this.piechartSrc = this.piechartSrcTemplate.replace('$$LUCENE_QUERY$$', this.luceneQuery);
    this.datehistogramSrc = this.datehistogramSrcTemplate.replace('$$LUCENE_QUERY$$', this.luceneQuery);
    this.tagcloudSrc = this.tagcloudSrcTemplate.replace('$$LUCENE_QUERY$$', this.luceneQuery);
  }

  public executeSearch(searchText: string): void {

    this.apiService.getArticles(searchText).subscribe(x => this.processTextSearchResponse(x));
  }

  private processTextSearchResponse(textSearchResponse: TextSearchResponse, refreshUMLSCodesSelector: boolean = true) {
    // to prevent undesired codes selector update
    if (refreshUMLSCodesSelector) {
      // full assignment (including the requested umls codes to be update)
      this.textSearchResponse = textSearchResponse;
      this.luceneQuery = '';
      this.piechartSrc = this.piechartSrcTemplate.replace('$$LUCENE_QUERY$$', this.luceneQuery);
      this.datehistogramSrc = this.datehistogramSrcTemplate.replace('$$LUCENE_QUERY$$', this.luceneQuery);
      this.tagcloudSrc = this.tagcloudSrcTemplate.replace('$$LUCENE_QUERY$$', this.luceneQuery);
    } else {
      // use aux var to prevent the requested umls codes update)
      const auxResponse = textSearchResponse;
      auxResponse.requestedUmlsCodes = this.textSearchResponse.requestedUmlsCodes;
      auxResponse.text = this.textSearchResponse.text;
      this.textSearchResponse = auxResponse;
    }


    // umls selector
    if (refreshUMLSCodesSelector) {
      this.selectedUMLSConceptMetadata = null;
      this.detectedUMLCodesMetadata = [];
      for (const umlsCode of this.textSearchResponse.requestedUmlsCodes) {
        this.obtainUMLSMedatada(umlsCode);
      }
    }
    // pagination (quite ugly, can you improve it..?)
    this.lastPage = Math.floor(this.textSearchResponse.totalHits / this.textSearchResponse.pageSize);
    this.pageNumbers = Array(this.lastPage).fill(0).map((x, i) => i)
      .slice(Math.max(this.textSearchResponse.pageNumber - 5, 0),
      Math.min(this.textSearchResponse.pageNumber + 5, this.lastPage)); // [0,1,2,3,4]
    console.log('Page numbers: ' + this.pageNumbers);
  }

  public servePage(pageNum: number) {
    if (this.textSearchResponse != null) {
      this.apiService.getArticles(this.textSearchResponse.text, pageNum).subscribe(x => this.processTextSearchResponse(x));
    }
  }

  public obtainUMLSMedatada(umlsCode: string) {
    this.apiService.getUMLSMetadata(umlsCode).subscribe(x => this.detectedUMLCodesMetadata.push(x));
  }

  public selectUMLSMetadata(umlsMetadata: UmlsMetadata) {
    // if it is blacklisted do nothing
    if (this.isBlackListed(umlsMetadata)) {
      console.log('Black listed item ' + JSON.stringify(umlsMetadata));
      return;
    }
    if (this.selectedUMLSConceptMetadata === umlsMetadata) {
      // already selected, thus deselect
      this.selectedUMLSConceptMetadata = null;
      this.luceneQuery = '';
      // repeat the initial full-search with the text of the previous search
      this.apiService.getArticles(this.textSearchResponse.text).subscribe(x => this.processTextSearchResponse(x, false));
    } else {
      this.selectedUMLSConceptMetadata = umlsMetadata;
      // apart from the selection change, a new search only with the code
      this.apiService.getArticlesWithCodes(
        '', this.selectedUMLSConceptMetadata.CUI).subscribe(x => this.processTextSearchResponse(x, false));
      this.luceneQuery = 'umls_concepts:' + this.selectedUMLSConceptMetadata.CUI;
    }
    this.piechartSrc = this.piechartSrcTemplate.replace('$$LUCENE_QUERY$$', this.luceneQuery);
    this.datehistogramSrc = this.datehistogramSrcTemplate.replace('$$LUCENE_QUERY$$', this.luceneQuery);
    this.tagcloudSrc = this.tagcloudSrcTemplate.replace('$$LUCENE_QUERY$$', this.luceneQuery);
  }

  public isBlackListed(umlsMetadata: UmlsMetadata): boolean {
    for (const semType of umlsMetadata.semanticTypes) {
      if (this.BLACKLISTED_SEMANTIC_TYPES.includes(semType)) {
        return true;
      }
    }
    if (this.BLACKLISTED_UMLS_CODES.includes(umlsMetadata.CUI)) {
      return true;
    }
    return false;
  }

  public retrieveColorForSemanticType(semanticType: string): string {
    switch (semanticType) {
      case 'Disease or Syndrome':
        return 'DarkRed';
      case 'Functional Concept':
        return 'grey';
      case 'Finding':
        return 'orange';
      case 'Population Group':
        return 'purple';
      case 'Mental or Behavioral Dysfunction':
        return 'DarkRed';
    }

  }

}

