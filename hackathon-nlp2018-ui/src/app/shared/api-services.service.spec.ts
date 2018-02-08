import { TestBed, inject } from '@angular/core/testing';

import { ApiServicesService } from './api-services.service';

describe('ApiServicesService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ApiServicesService]
    });
  });

  it('should be created', inject([ApiServicesService], (service: ApiServicesService) => {
    expect(service).toBeTruthy();
  }));
});
