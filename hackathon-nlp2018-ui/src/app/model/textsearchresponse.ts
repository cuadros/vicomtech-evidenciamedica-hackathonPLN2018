export class TextSearchResponse {

  text: string;
  pageNumber: number;
  pageSize: number;
  totalHits: number;
  results: RetrievedArticleInfo[];
  requestedUmlsCodes: string[];

}

export class RetrievedArticleInfo {

  position: number;
  identifier: string;
  titleES: string;
  titleEN: string;
  authors: string[];
  journal: string;
}
