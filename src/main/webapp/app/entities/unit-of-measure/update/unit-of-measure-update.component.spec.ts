import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UnitOfMeasureFormService } from './unit-of-measure-form.service';
import { UnitOfMeasureService } from '../service/unit-of-measure.service';
import { IUnitOfMeasure } from '../unit-of-measure.model';

import { UnitOfMeasureUpdateComponent } from './unit-of-measure-update.component';

describe('UnitOfMeasure Management Update Component', () => {
  let comp: UnitOfMeasureUpdateComponent;
  let fixture: ComponentFixture<UnitOfMeasureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let unitOfMeasureFormService: UnitOfMeasureFormService;
  let unitOfMeasureService: UnitOfMeasureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UnitOfMeasureUpdateComponent],
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
      .overrideTemplate(UnitOfMeasureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UnitOfMeasureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    unitOfMeasureFormService = TestBed.inject(UnitOfMeasureFormService);
    unitOfMeasureService = TestBed.inject(UnitOfMeasureService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const unitOfMeasure: IUnitOfMeasure = { id: 456 };

      activatedRoute.data = of({ unitOfMeasure });
      comp.ngOnInit();

      expect(comp.unitOfMeasure).toEqual(unitOfMeasure);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUnitOfMeasure>>();
      const unitOfMeasure = { id: 123 };
      jest.spyOn(unitOfMeasureFormService, 'getUnitOfMeasure').mockReturnValue(unitOfMeasure);
      jest.spyOn(unitOfMeasureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ unitOfMeasure });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: unitOfMeasure }));
      saveSubject.complete();

      // THEN
      expect(unitOfMeasureFormService.getUnitOfMeasure).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(unitOfMeasureService.update).toHaveBeenCalledWith(expect.objectContaining(unitOfMeasure));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUnitOfMeasure>>();
      const unitOfMeasure = { id: 123 };
      jest.spyOn(unitOfMeasureFormService, 'getUnitOfMeasure').mockReturnValue({ id: null });
      jest.spyOn(unitOfMeasureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ unitOfMeasure: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: unitOfMeasure }));
      saveSubject.complete();

      // THEN
      expect(unitOfMeasureFormService.getUnitOfMeasure).toHaveBeenCalled();
      expect(unitOfMeasureService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUnitOfMeasure>>();
      const unitOfMeasure = { id: 123 };
      jest.spyOn(unitOfMeasureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ unitOfMeasure });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(unitOfMeasureService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
