import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RatingTypeFormService } from './rating-type-form.service';
import { RatingTypeService } from '../service/rating-type.service';
import { IRatingType } from '../rating-type.model';

import { RatingTypeUpdateComponent } from './rating-type-update.component';

describe('RatingType Management Update Component', () => {
  let comp: RatingTypeUpdateComponent;
  let fixture: ComponentFixture<RatingTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ratingTypeFormService: RatingTypeFormService;
  let ratingTypeService: RatingTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RatingTypeUpdateComponent],
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
      .overrideTemplate(RatingTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RatingTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ratingTypeFormService = TestBed.inject(RatingTypeFormService);
    ratingTypeService = TestBed.inject(RatingTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const ratingType: IRatingType = { id: 456 };

      activatedRoute.data = of({ ratingType });
      comp.ngOnInit();

      expect(comp.ratingType).toEqual(ratingType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRatingType>>();
      const ratingType = { id: 123 };
      jest.spyOn(ratingTypeFormService, 'getRatingType').mockReturnValue(ratingType);
      jest.spyOn(ratingTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ratingType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ratingType }));
      saveSubject.complete();

      // THEN
      expect(ratingTypeFormService.getRatingType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ratingTypeService.update).toHaveBeenCalledWith(expect.objectContaining(ratingType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRatingType>>();
      const ratingType = { id: 123 };
      jest.spyOn(ratingTypeFormService, 'getRatingType').mockReturnValue({ id: null });
      jest.spyOn(ratingTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ratingType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ratingType }));
      saveSubject.complete();

      // THEN
      expect(ratingTypeFormService.getRatingType).toHaveBeenCalled();
      expect(ratingTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRatingType>>();
      const ratingType = { id: 123 };
      jest.spyOn(ratingTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ratingType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ratingTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
