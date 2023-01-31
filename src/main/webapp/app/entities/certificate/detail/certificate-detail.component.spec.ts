import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CertificateDetailComponent } from './certificate-detail.component';

describe('Certificate Management Detail Component', () => {
  let comp: CertificateDetailComponent;
  let fixture: ComponentFixture<CertificateDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CertificateDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ certificate: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CertificateDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CertificateDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load certificate on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.certificate).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
