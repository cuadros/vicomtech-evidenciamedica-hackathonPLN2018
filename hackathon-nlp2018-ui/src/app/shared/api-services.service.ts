import {TextSearchResponse} from '../model/textsearchresponse';
import {UmlsMetadata} from '../model/umlsmetadata';
import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {catchError, map, tap} from 'rxjs/operators';

@Injectable()
export class ApiServicesService {

  // private searchUrl = 'api/search';  // URL to web api
  private searchUrl = 'http://192.168.20.11:8080/hackathon-nlp-api/searching/searchText';
  private searchUrlWithUMLSCodes = 'http://192.168.20.11:8080/hackathon-nlp-api/searching/searchTextWithUMLSCodes';
  private umlsMetadataUrl = 'http://192.168.20.11:8080/hackathon-nlp-api/searching/retrieveUMLSMetadata';

  constructor(private http: HttpClient) {}


  getArticles(textToSearch: string, pageNum: number = 0, pageSize: number = 10): Observable<TextSearchResponse> {
    console.log('Received text to search: ' + textToSearch);
    const results = this.http.get<TextSearchResponse>(
      this.searchUrl + '?text-to-search=' + textToSearch + '&page-number=' + pageNum + '&page-size=' + pageSize);
    return results;
    //    const n = Math.floor(Math.random() * 6) + 1;
    //    return of(['Article ' + n, 'Article ' + Math.floor(Math.random() * 6), 'Article ' + Math.floor(Math.random() * 6)]);
  }

  //  getUMLSConcepts(textToSearch: string): Observable<string[]> {
  //    // return this.http.get<String[]>(this.searchUrl);
  //    return of(['UMLS Concept  ' + Math.floor(Math.random() * 6) + 1
  //      , 'UMLS Concept ' + Math.floor(Math.random() * 6), 'UMLS Concept ' + Math.floor(Math.random() * 6)])  //  }

  getArticlesWithCodes(textToSearch: string, umlsCodes: string, pageNum: number = 0, pageSize: number = 10)
    : Observable<TextSearchResponse> {
    console.log('Received text to search: ' + textToSearch + ' , with codes:' + umlsCodes);
    const results = this.http.get<TextSearchResponse>(
      this.searchUrlWithUMLSCodes +
      '?text-to-search=' + textToSearch + '&page-number=' + pageNum + '&page-size=' + pageSize + '&umls-codes=' + umlsCodes);
    return results;
  }

  getUMLSMetadata(umlsCode: string): Observable<UmlsMetadata> {
    console.log('Received umls code for metadata: ' + umlsCode);
    const results = this.http.get<UmlsMetadata>(
      this.umlsMetadataUrl + '?umlsCode=' + umlsCode, {});
    return results;
  }

}
