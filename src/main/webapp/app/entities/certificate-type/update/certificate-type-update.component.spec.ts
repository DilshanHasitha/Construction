import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CertificateTypeFormService } from './certificate-type-form.service';
import { CertificateTypeService } from '../service/certificate-type.service';
import { ICertificateType } from '../certificate-type.model';

import { CertificateTypeUpdateComponent } from './certificate-type-update.component';

describe('CertificateType Management Update Component', () => {
  let comp: CertificateTypeUpdateComponent;
  let fixture: ComponentFixture<CertificateTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let certificateTypeFormService: CertificateTypeFormService;
  let certificateTypeService: CertificateTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CertificateTypeUpdateComponent],
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
      .overrideTemplate(CertificateTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CertificateTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    certificateTypeFormService = TestBed.inject(CertificateTypeFormService);
    certificateTypeService = TestBed.inject(CertificateTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const certificateType: ICertificateType = { id: 456 };

      activatedRoute.data = of({ certificateType });
      comp.ngOnInit();

      expect(comp.certificateType).toEqual(certificateType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICertificateType>>();
      const certificateType = { id: 123 };
      jest.spyOn(certificateTypeFormService, 'getCertificateType').mockReturnValue(certificateType);
      jest.spyOn(certificateTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ certificateType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: certificateType }));
      saveSubject.complete();

      // THEN
      expect(certificateTypeFormService.getCertificateType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(certificateTypeService.update).toHaveBeenCalledWith(expect.objectContaining(certificateType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICertificateType>>();
      const certificateType = { id: 123 };
      jest.spyOn(certificateTypeFormService, 'getCertificateType').mockReturnValue({ id: null });
      jest.spyOn(certificateTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ certificateType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: certificateType }));
      saveSubject.complete();

      // THEN
      expect(certificateTypeFormService.getCertificateType).toHaveBeenCalled();
      expect(certificateTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICertificateType>>();
      const certificateType = { id: 123 };
      jest.spyOn(certificateTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ certificateType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(certificateTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
