import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrderDetailsFormService } from './order-details-form.service';
import { OrderDetailsService } from '../service/order-details.service';
import { IOrderDetails } from '../order-details.model';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';

import { OrderDetailsUpdateComponent } from './order-details-update.component';

describe('OrderDetails Management Update Component', () => {
  let comp: OrderDetailsUpdateComponent;
  let fixture: ComponentFixture<OrderDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orderDetailsFormService: OrderDetailsFormService;
  let orderDetailsService: OrderDetailsService;
  let itemService: ItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OrderDetailsUpdateComponent],
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
      .overrideTemplate(OrderDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrderDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderDetailsFormService = TestBed.inject(OrderDetailsFormService);
    orderDetailsService = TestBed.inject(OrderDetailsService);
    itemService = TestBed.inject(ItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Item query and add missing value', () => {
      const orderDetails: IOrderDetails = { id: 456 };
      const item: IItem = { id: 79024 };
      orderDetails.item = item;

      const itemCollection: IItem[] = [{ id: 70061 }];
      jest.spyOn(itemService, 'query').mockReturnValue(of(new HttpResponse({ body: itemCollection })));
      const additionalItems = [item];
      const expectedCollection: IItem[] = [...additionalItems, ...itemCollection];
      jest.spyOn(itemService, 'addItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderDetails });
      comp.ngOnInit();

      expect(itemService.query).toHaveBeenCalled();
      expect(itemService.addItemToCollectionIfMissing).toHaveBeenCalledWith(
        itemCollection,
        ...additionalItems.map(expect.objectContaining)
      );
      expect(comp.itemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const orderDetails: IOrderDetails = { id: 456 };
      const item: IItem = { id: 86017 };
      orderDetails.item = item;

      activatedRoute.data = of({ orderDetails });
      comp.ngOnInit();

      expect(comp.itemsSharedCollection).toContain(item);
      expect(comp.orderDetails).toEqual(orderDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrderDetails>>();
      const orderDetails = { id: 123 };
      jest.spyOn(orderDetailsFormService, 'getOrderDetails').mockReturnValue(orderDetails);
      jest.spyOn(orderDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderDetails }));
      saveSubject.complete();

      // THEN
      expect(orderDetailsFormService.getOrderDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(orderDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrderDetails>>();
      const orderDetails = { id: 123 };
      jest.spyOn(orderDetailsFormService, 'getOrderDetails').mockReturnValue({ id: null });
      jest.spyOn(orderDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderDetails }));
      saveSubject.complete();

      // THEN
      expect(orderDetailsFormService.getOrderDetails).toHaveBeenCalled();
      expect(orderDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrderDetails>>();
      const orderDetails = { id: 123 };
      jest.spyOn(orderDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderDetailsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareItem', () => {
      it('Should forward to itemService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(itemService, 'compareItem');
        comp.compareItem(entity, entity2);
        expect(itemService.compareItem).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
