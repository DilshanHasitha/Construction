import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrdersFormService } from './orders-form.service';
import { OrdersService } from '../service/orders.service';
import { IOrders } from '../orders.model';
import { IExUser } from 'app/entities/ex-user/ex-user.model';
import { ExUserService } from 'app/entities/ex-user/service/ex-user.service';
import { IOrderStatus } from 'app/entities/order-status/order-status.model';
import { OrderStatusService } from 'app/entities/order-status/service/order-status.service';
import { IOrderDetails } from 'app/entities/order-details/order-details.model';
import { OrderDetailsService } from 'app/entities/order-details/service/order-details.service';

import { OrdersUpdateComponent } from './orders-update.component';

describe('Orders Management Update Component', () => {
  let comp: OrdersUpdateComponent;
  let fixture: ComponentFixture<OrdersUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ordersFormService: OrdersFormService;
  let ordersService: OrdersService;
  let exUserService: ExUserService;
  let orderStatusService: OrderStatusService;
  let orderDetailsService: OrderDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OrdersUpdateComponent],
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
      .overrideTemplate(OrdersUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrdersUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ordersFormService = TestBed.inject(OrdersFormService);
    ordersService = TestBed.inject(OrdersService);
    exUserService = TestBed.inject(ExUserService);
    orderStatusService = TestBed.inject(OrderStatusService);
    orderDetailsService = TestBed.inject(OrderDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ExUser query and add missing value', () => {
      const orders: IOrders = { id: 456 };
      const exUser: IExUser = { id: 62464 };
      orders.exUser = exUser;

      const exUserCollection: IExUser[] = [{ id: 44200 }];
      jest.spyOn(exUserService, 'query').mockReturnValue(of(new HttpResponse({ body: exUserCollection })));
      const additionalExUsers = [exUser];
      const expectedCollection: IExUser[] = [...additionalExUsers, ...exUserCollection];
      jest.spyOn(exUserService, 'addExUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orders });
      comp.ngOnInit();

      expect(exUserService.query).toHaveBeenCalled();
      expect(exUserService.addExUserToCollectionIfMissing).toHaveBeenCalledWith(
        exUserCollection,
        ...additionalExUsers.map(expect.objectContaining)
      );
      expect(comp.exUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call OrderStatus query and add missing value', () => {
      const orders: IOrders = { id: 456 };
      const orderStatus: IOrderStatus = { id: 25562 };
      orders.orderStatus = orderStatus;

      const orderStatusCollection: IOrderStatus[] = [{ id: 63100 }];
      jest.spyOn(orderStatusService, 'query').mockReturnValue(of(new HttpResponse({ body: orderStatusCollection })));
      const additionalOrderStatuses = [orderStatus];
      const expectedCollection: IOrderStatus[] = [...additionalOrderStatuses, ...orderStatusCollection];
      jest.spyOn(orderStatusService, 'addOrderStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orders });
      comp.ngOnInit();

      expect(orderStatusService.query).toHaveBeenCalled();
      expect(orderStatusService.addOrderStatusToCollectionIfMissing).toHaveBeenCalledWith(
        orderStatusCollection,
        ...additionalOrderStatuses.map(expect.objectContaining)
      );
      expect(comp.orderStatusesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call OrderDetails query and add missing value', () => {
      const orders: IOrders = { id: 456 };
      const orderDetails: IOrderDetails[] = [{ id: 28436 }];
      orders.orderDetails = orderDetails;

      const orderDetailsCollection: IOrderDetails[] = [{ id: 77064 }];
      jest.spyOn(orderDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: orderDetailsCollection })));
      const additionalOrderDetails = [...orderDetails];
      const expectedCollection: IOrderDetails[] = [...additionalOrderDetails, ...orderDetailsCollection];
      jest.spyOn(orderDetailsService, 'addOrderDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orders });
      comp.ngOnInit();

      expect(orderDetailsService.query).toHaveBeenCalled();
      expect(orderDetailsService.addOrderDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        orderDetailsCollection,
        ...additionalOrderDetails.map(expect.objectContaining)
      );
      expect(comp.orderDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const orders: IOrders = { id: 456 };
      const exUser: IExUser = { id: 63234 };
      orders.exUser = exUser;
      const orderStatus: IOrderStatus = { id: 29569 };
      orders.orderStatus = orderStatus;
      const orderDetails: IOrderDetails = { id: 75883 };
      orders.orderDetails = [orderDetails];

      activatedRoute.data = of({ orders });
      comp.ngOnInit();

      expect(comp.exUsersSharedCollection).toContain(exUser);
      expect(comp.orderStatusesSharedCollection).toContain(orderStatus);
      expect(comp.orderDetailsSharedCollection).toContain(orderDetails);
      expect(comp.orders).toEqual(orders);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrders>>();
      const orders = { id: 123 };
      jest.spyOn(ordersFormService, 'getOrders').mockReturnValue(orders);
      jest.spyOn(ordersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orders });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orders }));
      saveSubject.complete();

      // THEN
      expect(ordersFormService.getOrders).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ordersService.update).toHaveBeenCalledWith(expect.objectContaining(orders));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrders>>();
      const orders = { id: 123 };
      jest.spyOn(ordersFormService, 'getOrders').mockReturnValue({ id: null });
      jest.spyOn(ordersService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orders: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orders }));
      saveSubject.complete();

      // THEN
      expect(ordersFormService.getOrders).toHaveBeenCalled();
      expect(ordersService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrders>>();
      const orders = { id: 123 };
      jest.spyOn(ordersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orders });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ordersService.update).toHaveBeenCalled();
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

    describe('compareOrderStatus', () => {
      it('Should forward to orderStatusService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(orderStatusService, 'compareOrderStatus');
        comp.compareOrderStatus(entity, entity2);
        expect(orderStatusService.compareOrderStatus).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOrderDetails', () => {
      it('Should forward to orderDetailsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(orderDetailsService, 'compareOrderDetails');
        comp.compareOrderDetails(entity, entity2);
        expect(orderDetailsService.compareOrderDetails).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
