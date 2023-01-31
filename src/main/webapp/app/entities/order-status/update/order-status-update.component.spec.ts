import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrderStatusFormService } from './order-status-form.service';
import { OrderStatusService } from '../service/order-status.service';
import { IOrderStatus } from '../order-status.model';

import { OrderStatusUpdateComponent } from './order-status-update.component';

describe('OrderStatus Management Update Component', () => {
  let comp: OrderStatusUpdateComponent;
  let fixture: ComponentFixture<OrderStatusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orderStatusFormService: OrderStatusFormService;
  let orderStatusService: OrderStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OrderStatusUpdateComponent],
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
      .overrideTemplate(OrderStatusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrderStatusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderStatusFormService = TestBed.inject(OrderStatusFormService);
    orderStatusService = TestBed.inject(OrderStatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const orderStatus: IOrderStatus = { id: 456 };

      activatedRoute.data = of({ orderStatus });
      comp.ngOnInit();

      expect(comp.orderStatus).toEqual(orderStatus);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrderStatus>>();
      const orderStatus = { id: 123 };
      jest.spyOn(orderStatusFormService, 'getOrderStatus').mockReturnValue(orderStatus);
      jest.spyOn(orderStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderStatus }));
      saveSubject.complete();

      // THEN
      expect(orderStatusFormService.getOrderStatus).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderStatusService.update).toHaveBeenCalledWith(expect.objectContaining(orderStatus));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrderStatus>>();
      const orderStatus = { id: 123 };
      jest.spyOn(orderStatusFormService, 'getOrderStatus').mockReturnValue({ id: null });
      jest.spyOn(orderStatusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderStatus: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderStatus }));
      saveSubject.complete();

      // THEN
      expect(orderStatusFormService.getOrderStatus).toHaveBeenCalled();
      expect(orderStatusService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrderStatus>>();
      const orderStatus = { id: 123 };
      jest.spyOn(orderStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderStatusService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
