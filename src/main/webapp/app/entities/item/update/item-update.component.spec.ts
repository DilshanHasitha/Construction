import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ItemFormService } from './item-form.service';
import { ItemService } from '../service/item.service';
import { IItem } from '../item.model';
import { IMasterItem } from 'app/entities/master-item/master-item.model';
import { MasterItemService } from 'app/entities/master-item/service/master-item.service';
import { IUnitOfMeasure } from 'app/entities/unit-of-measure/unit-of-measure.model';
import { UnitOfMeasureService } from 'app/entities/unit-of-measure/service/unit-of-measure.service';
import { IExUser } from 'app/entities/ex-user/ex-user.model';
import { ExUserService } from 'app/entities/ex-user/service/ex-user.service';
import { IRating } from 'app/entities/rating/rating.model';
import { RatingService } from 'app/entities/rating/service/rating.service';
import { ICertificate } from 'app/entities/certificate/certificate.model';
import { CertificateService } from 'app/entities/certificate/service/certificate.service';

import { ItemUpdateComponent } from './item-update.component';

describe('Item Management Update Component', () => {
  let comp: ItemUpdateComponent;
  let fixture: ComponentFixture<ItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let itemFormService: ItemFormService;
  let itemService: ItemService;
  let masterItemService: MasterItemService;
  let unitOfMeasureService: UnitOfMeasureService;
  let exUserService: ExUserService;
  let ratingService: RatingService;
  let certificateService: CertificateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ItemUpdateComponent],
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
      .overrideTemplate(ItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    itemFormService = TestBed.inject(ItemFormService);
    itemService = TestBed.inject(ItemService);
    masterItemService = TestBed.inject(MasterItemService);
    unitOfMeasureService = TestBed.inject(UnitOfMeasureService);
    exUserService = TestBed.inject(ExUserService);
    ratingService = TestBed.inject(RatingService);
    certificateService = TestBed.inject(CertificateService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call MasterItem query and add missing value', () => {
      const item: IItem = { id: 456 };
      const masterItem: IMasterItem = { id: 35156 };
      item.masterItem = masterItem;

      const masterItemCollection: IMasterItem[] = [{ id: 27411 }];
      jest.spyOn(masterItemService, 'query').mockReturnValue(of(new HttpResponse({ body: masterItemCollection })));
      const additionalMasterItems = [masterItem];
      const expectedCollection: IMasterItem[] = [...additionalMasterItems, ...masterItemCollection];
      jest.spyOn(masterItemService, 'addMasterItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(masterItemService.query).toHaveBeenCalled();
      expect(masterItemService.addMasterItemToCollectionIfMissing).toHaveBeenCalledWith(
        masterItemCollection,
        ...additionalMasterItems.map(expect.objectContaining)
      );
      expect(comp.masterItemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UnitOfMeasure query and add missing value', () => {
      const item: IItem = { id: 456 };
      const unit: IUnitOfMeasure = { id: 33836 };
      item.unit = unit;

      const unitOfMeasureCollection: IUnitOfMeasure[] = [{ id: 59069 }];
      jest.spyOn(unitOfMeasureService, 'query').mockReturnValue(of(new HttpResponse({ body: unitOfMeasureCollection })));
      const additionalUnitOfMeasures = [unit];
      const expectedCollection: IUnitOfMeasure[] = [...additionalUnitOfMeasures, ...unitOfMeasureCollection];
      jest.spyOn(unitOfMeasureService, 'addUnitOfMeasureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(unitOfMeasureService.query).toHaveBeenCalled();
      expect(unitOfMeasureService.addUnitOfMeasureToCollectionIfMissing).toHaveBeenCalledWith(
        unitOfMeasureCollection,
        ...additionalUnitOfMeasures.map(expect.objectContaining)
      );
      expect(comp.unitOfMeasuresSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ExUser query and add missing value', () => {
      const item: IItem = { id: 456 };
      const exUser: IExUser = { id: 62055 };
      item.exUser = exUser;

      const exUserCollection: IExUser[] = [{ id: 74345 }];
      jest.spyOn(exUserService, 'query').mockReturnValue(of(new HttpResponse({ body: exUserCollection })));
      const additionalExUsers = [exUser];
      const expectedCollection: IExUser[] = [...additionalExUsers, ...exUserCollection];
      jest.spyOn(exUserService, 'addExUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(exUserService.query).toHaveBeenCalled();
      expect(exUserService.addExUserToCollectionIfMissing).toHaveBeenCalledWith(
        exUserCollection,
        ...additionalExUsers.map(expect.objectContaining)
      );
      expect(comp.exUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Rating query and add missing value', () => {
      const item: IItem = { id: 456 };
      const ratings: IRating[] = [{ id: 19865 }];
      item.ratings = ratings;

      const ratingCollection: IRating[] = [{ id: 59578 }];
      jest.spyOn(ratingService, 'query').mockReturnValue(of(new HttpResponse({ body: ratingCollection })));
      const additionalRatings = [...ratings];
      const expectedCollection: IRating[] = [...additionalRatings, ...ratingCollection];
      jest.spyOn(ratingService, 'addRatingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(ratingService.query).toHaveBeenCalled();
      expect(ratingService.addRatingToCollectionIfMissing).toHaveBeenCalledWith(
        ratingCollection,
        ...additionalRatings.map(expect.objectContaining)
      );
      expect(comp.ratingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Certificate query and add missing value', () => {
      const item: IItem = { id: 456 };
      const certificates: ICertificate[] = [{ id: 66965 }];
      item.certificates = certificates;

      const certificateCollection: ICertificate[] = [{ id: 78180 }];
      jest.spyOn(certificateService, 'query').mockReturnValue(of(new HttpResponse({ body: certificateCollection })));
      const additionalCertificates = [...certificates];
      const expectedCollection: ICertificate[] = [...additionalCertificates, ...certificateCollection];
      jest.spyOn(certificateService, 'addCertificateToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(certificateService.query).toHaveBeenCalled();
      expect(certificateService.addCertificateToCollectionIfMissing).toHaveBeenCalledWith(
        certificateCollection,
        ...additionalCertificates.map(expect.objectContaining)
      );
      expect(comp.certificatesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const item: IItem = { id: 456 };
      const masterItem: IMasterItem = { id: 10642 };
      item.masterItem = masterItem;
      const unit: IUnitOfMeasure = { id: 20283 };
      item.unit = unit;
      const exUser: IExUser = { id: 33188 };
      item.exUser = exUser;
      const rating: IRating = { id: 56390 };
      item.ratings = [rating];
      const certificate: ICertificate = { id: 60224 };
      item.certificates = [certificate];

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(comp.masterItemsSharedCollection).toContain(masterItem);
      expect(comp.unitOfMeasuresSharedCollection).toContain(unit);
      expect(comp.exUsersSharedCollection).toContain(exUser);
      expect(comp.ratingsSharedCollection).toContain(rating);
      expect(comp.certificatesSharedCollection).toContain(certificate);
      expect(comp.item).toEqual(item);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItem>>();
      const item = { id: 123 };
      jest.spyOn(itemFormService, 'getItem').mockReturnValue(item);
      jest.spyOn(itemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ item });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: item }));
      saveSubject.complete();

      // THEN
      expect(itemFormService.getItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(itemService.update).toHaveBeenCalledWith(expect.objectContaining(item));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItem>>();
      const item = { id: 123 };
      jest.spyOn(itemFormService, 'getItem').mockReturnValue({ id: null });
      jest.spyOn(itemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ item: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: item }));
      saveSubject.complete();

      // THEN
      expect(itemFormService.getItem).toHaveBeenCalled();
      expect(itemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItem>>();
      const item = { id: 123 };
      jest.spyOn(itemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ item });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(itemService.update).toHaveBeenCalled();
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

    describe('compareExUser', () => {
      it('Should forward to exUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(exUserService, 'compareExUser');
        comp.compareExUser(entity, entity2);
        expect(exUserService.compareExUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRating', () => {
      it('Should forward to ratingService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ratingService, 'compareRating');
        comp.compareRating(entity, entity2);
        expect(ratingService.compareRating).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCertificate', () => {
      it('Should forward to certificateService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(certificateService, 'compareCertificate');
        comp.compareCertificate(entity, entity2);
        expect(certificateService.compareCertificate).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
