import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CertificateTypeDetailComponent } from './certificate-type-detail.component';

describe('CertificateType Management Detail Component', () => {
  let comp: CertificateTypeDetailComponent;
  let fixture: ComponentFixture<CertificateTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CertificateTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ certificateType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CertificateTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CertificateTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load certificateType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.certificateType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
