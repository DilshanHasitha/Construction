import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BOQDetailsFormService } from './boq-details-form.service';
import { BOQDetailsService } from '../service/boq-details.service';
import { IBOQDetails } from '../boq-details.model';
import { IMasterItem } from 'app/entities/master-item/master-item.model';
import { MasterItemService } from 'app/entities/master-item/service/master-item.service';
import { IUnitOfMeasure } from 'app/entities/unit-of-measure/unit-of-measure.model';
import { UnitOfMeasureService } from 'app/entities/unit-of-measure/service/unit-of-measure.service';

import { BOQDetailsUpdateComponent } from './boq-details-update.component';

describe('BOQDetails Management Update Component', () => {
  let comp: BOQDetailsUpdateComponent;
  let fixture: ComponentFixture<BOQDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bOQDetailsFormService: BOQDetailsFormService;
  let bOQDetailsService: BOQDetailsService;
  let masterItemService: MasterItemService;
  let unitOfMeasureService: UnitOfMeasureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BOQDetailsUpdateComponent],
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
      .overrideTemplate(BOQDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BOQDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bOQDetailsFormService = TestBed.inject(BOQDetailsFormService);
    bOQDetailsService = TestBed.inject(BOQDetailsService);
    masterItemService = TestBed.inject(MasterItemService);
    unitOfMeasureService = TestBed.inject(UnitOfMeasureService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call MasterItem query and add missing value', () => {
      const bOQDetails: IBOQDetails = { id: 456 };
      const item: IMasterItem = { id: 23014 };
      bOQDetails.item = item;

      const masterItemCollection: IMasterItem[] = [{ id: 49196 }];
      jest.spyOn(masterItemService, 'query').mockReturnValue(of(new HttpResponse({ body: masterItemCollection })));
      const additionalMasterItems = [item];
      const expectedCollection: IMasterItem[] = [...additionalMasterItems, ...masterItemCollection];
      jest.spyOn(masterItemService, 'addMasterItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bOQDetails });
      comp.ngOnInit();

      expect(masterItemService.query).toHaveBeenCalled();
      expect(masterItemService.addMasterItemToCollectionIfMissing).toHaveBeenCalledWith(
        masterItemCollection,
        ...additionalMasterItems.map(expect.objectContaining)
      );
      expect(comp.masterItemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UnitOfMeasure query and add missing value', () => {
      const bOQDetails: IBOQDetails = { id: 456 };
      const per: IUnitOfMeasure = { id: 7527 };
      bOQDetails.per = per;
      const unit: IUnitOfMeasure = { id: 46151 };
      bOQDetails.unit = unit;

      const unitOfMeasureCollection: IUnitOfMeasure[] = [{ id: 72215 }];
      jest.spyOn(unitOfMeasureService, 'query').mockReturnValue(of(new HttpResponse({ body: unitOfMeasureCollection })));
      const additionalUnitOfMeasures = [per, unit];
      const expectedCollection: IUnitOfMeasure[] = [...additionalUnitOfMeasures, ...unitOfMeasureCollection];
      jest.spyOn(unitOfMeasureService, 'addUnitOfMeasureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bOQDetails });
      comp.ngOnInit();

      expect(unitOfMeasureService.query).toHaveBeenCalled();
      expect(unitOfMeasureService.addUnitOfMeasureToCollectionIfMissing).toHaveBeenCalledWith(
        unitOfMeasureCollection,
        ...additionalUnitOfMeasures.map(expect.objectContaining)
      );
      expect(comp.unitOfMeasuresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const bOQDetails: IBOQDetails = { id: 456 };
      const item: IMasterItem = { id: 92890 };
      bOQDetails.item = item;
      const per: IUnitOfMeasure = { id: 67430 };
      bOQDetails.per = per;
      const unit: IUnitOfMeasure = { id: 72679 };
      bOQDetails.unit = unit;

      activatedRoute.data = of({ bOQDetails });
      comp.ngOnInit();

      expect(comp.masterItemsSharedCollection).toContain(item);
      expect(comp.unitOfMeasuresSharedCollection).toContain(per);
      expect(comp.unitOfMeasuresSharedCollection).toContain(unit);
      expect(comp.bOQDetails).toEqual(bOQDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBOQDetails>>();
      const bOQDetails = { id: 123 };
      jest.spyOn(bOQDetailsFormService, 'getBOQDetails').mockReturnValue(bOQDetails);
      jest.spyOn(bOQDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bOQDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bOQDetails }));
      saveSubject.complete();

      // THEN
      expect(bOQDetailsFormService.getBOQDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bOQDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(bOQDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBOQDetails>>();
      const bOQDetails = { id: 123 };
      jest.spyOn(bOQDetailsFormService, 'getBOQDetails').mockReturnValue({ id: null });
      jest.spyOn(bOQDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bOQDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bOQDetails }));
      saveSubject.complete();

      // THEN
      expect(bOQDetailsFormService.getBOQDetails).toHaveBeenCalled();
      expect(bOQDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBOQDetails>>();
      const bOQDetails = { id: 123 };
      jest.spyOn(bOQDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bOQDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bOQDetailsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMasterItem', () => {
      it('Should forward to masterItemService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(masterItemService, 'compareMasterItem');
        comp.compareMasterItem(entity, entity2);
        expect(masterItemService.compareMasterItem).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUnitOfMeasure', () => {
      it('Should forward to unitOfMeasureService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(unitOfMeasureService, 'compareUnitOfMeasure');
        comp.compareUnitOfMeasure(entity, entity2);
        expect(unitOfMeasureService.compareUnitOfMeasure).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
