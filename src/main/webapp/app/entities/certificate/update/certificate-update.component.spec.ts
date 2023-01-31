import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CertificateFormService } from './certificate-form.service';
import { CertificateService } from '../service/certificate.service';
import { ICertificate } from '../certificate.model';
import { ICertificateType } from 'app/entities/certificate-type/certificate-type.model';
import { CertificateTypeService } from 'app/entities/certificate-type/service/certificate-type.service';

import { CertificateUpdateComponent } from './certificate-update.component';

describe('Certificate Management Update Component', () => {
  let comp: CertificateUpdateComponent;
  let fixture: ComponentFixture<CertificateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let certificateFormService: CertificateFormService;
  let certificateService: CertificateService;
  let certificateTypeService: CertificateTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CertificateUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CertificateUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CertificateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    certificateFormService = TestBed.inject(CertificateFormService);
    certificateService = TestBed.inject(CertificateService);
    certificateTypeService = TestBed.inject(CertificateTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CertificateType query and add missing value', () => {
      const certificate: ICertificate = { id: 456 };
      const certificateType: ICertificateType = { id: 57215 };
      certificate.certificateType = certificateType;

      const certificateTypeCollection: ICertificateType[] = [{ id: 75566 }];
      jest.spyOn(certificateTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: certificateTypeCollection })));
      const additionalCertificateTypes = [certificateType];
      const expectedCollection: ICertificateType[] = [...additionalCertificateTypes, ...certificateTypeCollection];
      jest.spyOn(certificateTypeService, 'addCertificateTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ certificate });
      comp.ngOnInit();

      expect(certificateTypeService.query).toHaveBeenCalled();
      expect(certificateTypeService.addCertificateTypeToCollectionIfMissing).toHaveBeenCalledWith(
        certificateTypeCollection,
        ...additionalCertificateTypes.map(expect.objectContaining)
      );
      expect(comp.certificateTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const certificate: ICertificate = { id: 456 };
      const certificateType: ICertificateType = { id: 76836 };
      certificate.certificateType = certificateType;

      activatedRoute.data = of({ certificate });
      comp.ngOnInit();

      expect(comp.certificateTypesSharedCollection).toContain(certificateType);
      expect(comp.certificate).toEqual(certificate);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICertificate>>();
      const certificate = { id: 123 };
      jest.spyOn(certificateFormService, 'getCertificate').mockReturnValue(certificate);
      jest.spyOn(certificateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ certificate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: certificate }));
      saveSubject.complete();

      // THEN
      expect(certificateFormService.getCertificate).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(certificateService.update).toHaveBeenCalledWith(expect.objectContaining(certificate));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICertificate>>();
      const certificate = { id: 123 };
      jest.spyOn(certificateFormService, 'getCertificate').mockReturnValue({ id: null });
      jest.spyOn(certificateService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ certificate: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: certificate }));
      saveSubject.complete();

      // THEN
      expect(certificateFormService.getCertificate).toHaveBeenCalled();
      expect(certificateService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICertificate>>();
      const certificate = { id: 123 };
      jest.spyOn(certificateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ certificate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(certificateService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCertificateType', () => {
      it('Should forward to certificateTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(certificateTypeService, 'compareCertificateType');
        comp.compareCertificateType(entity, entity2);
        expect(certificateTypeService.compareCertificateType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
