import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BOQsFormService } from './bo-qs-form.service';
import { BOQsService } from '../service/bo-qs.service';
import { IBOQs } from '../bo-qs.model';
import { IExUser } from 'app/entities/ex-user/ex-user.model';
import { ExUserService } from 'app/entities/ex-user/service/ex-user.service';
import { IBOQDetails } from 'app/entities/boq-details/boq-details.model';
import { BOQDetailsService } from 'app/entities/boq-details/service/boq-details.service';

import { BOQsUpdateComponent } from './bo-qs-update.component';

describe('BOQs Management Update Component', () => {
  let comp: BOQsUpdateComponent;
  let fixture: ComponentFixture<BOQsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bOQsFormService: BOQsFormService;
  let bOQsService: BOQsService;
  let exUserService: ExUserService;
  let bOQDetailsService: BOQDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BOQsUpdateComponent],
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
      .overrideTemplate(BOQsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BOQsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bOQsFormService = TestBed.inject(BOQsFormService);
    bOQsService = TestBed.inject(BOQsService);
    exUserService = TestBed.inject(ExUserService);
    bOQDetailsService = TestBed.inject(BOQDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ExUser query and add missing value', () => {
      const bOQs: IBOQs = { id: 456 };
      const constructors: IExUser = { id: 40366 };
      bOQs.constructors = constructors;

      const exUserCollection: IExUser[] = [{ id: 40045 }];
      jest.spyOn(exUserService, 'query').mockReturnValue(of(new HttpResponse({ body: exUserCollection })));
      const additionalExUsers = [constructors];
      const expectedCollection: IExUser[] = [...additionalExUsers, ...exUserCollection];
      jest.spyOn(exUserService, 'addExUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bOQs });
      comp.ngOnInit();

      expect(exUserService.query).toHaveBeenCalled();
      expect(exUserService.addExUserToCollectionIfMissing).toHaveBeenCalledWith(
        exUserCollection,
        ...additionalExUsers.map(expect.objectContaining)
      );
      expect(comp.exUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BOQDetails query and add missing value', () => {
      const bOQs: IBOQs = { id: 456 };
      const boqDetails: IBOQDetails[] = [{ id: 16714 }];
      bOQs.boqDetails = boqDetails;

      const bOQDetailsCollection: IBOQDetails[] = [{ id: 87081 }];
      jest.spyOn(bOQDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: bOQDetailsCollection })));
      const additionalBOQDetails = [...boqDetails];
      const expectedCollection: IBOQDetails[] = [...additionalBOQDetails, ...bOQDetailsCollection];
      jest.spyOn(bOQDetailsService, 'addBOQDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bOQs });
      comp.ngOnInit();

      expect(bOQDetailsService.query).toHaveBeenCalled();
      expect(bOQDetailsService.addBOQDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        bOQDetailsCollection,
        ...additionalBOQDetails.map(expect.objectContaining)
      );
      expect(comp.bOQDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const bOQs: IBOQs = { id: 456 };
      const constructors: IExUser = { id: 65335 };
      bOQs.constructors = constructors;
      const boqDetails: IBOQDetails = { id: 91217 };
      bOQs.boqDetails = [boqDetails];

      activatedRoute.data = of({ bOQs });
      comp.ngOnInit();

      expect(comp.exUsersSharedCollection).toContain(constructors);
      expect(comp.bOQDetailsSharedCollection).toContain(boqDetails);
      expect(comp.bOQs).toEqual(bOQs);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBOQs>>();
      const bOQs = { id: 123 };
      jest.spyOn(bOQsFormService, 'getBOQs').mockReturnValue(bOQs);
      jest.spyOn(bOQsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bOQs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bOQs }));
      saveSubject.complete();

      // THEN
      expect(bOQsFormService.getBOQs).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bOQsService.update).toHaveBeenCalledWith(expect.objectContaining(bOQs));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBOQs>>();
      const bOQs = { id: 123 };
      jest.spyOn(bOQsFormService, 'getBOQs').mockReturnValue({ id: null });
      jest.spyOn(bOQsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bOQs: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bOQs }));
      saveSubject.complete();

      // THEN
      expect(bOQsFormService.getBOQs).toHaveBeenCalled();
      expect(bOQsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBOQs>>();
      const bOQs = { id: 123 };
      jest.spyOn(bOQsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bOQs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bOQsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareExUser', () => {
      it('Should forward to exUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(exUserService, 'compareExUser');
        comp.compareExUser(entity, entity2);
        expect(exUserService.compareExUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBOQDetails', () => {
      it('Should forward to bOQDetailsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(bOQDetailsService, 'compareBOQDetails');
        comp.compareBOQDetails(entity, entity2);
        expect(bOQDetailsService.compareBOQDetails).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
