import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { OrdersFormService, OrdersFormGroup } from './orders-form.service';
import { IOrders } from '../orders.model';
import { OrdersService } from '../service/orders.service';
import { IExUser } from 'app/entities/ex-user/ex-user.model';
import { ExUserService } from 'app/entities/ex-user/service/ex-user.service';
import { IOrderStatus } from 'app/entities/order-status/order-status.model';
import { OrderStatusService } from 'app/entities/order-status/service/order-status.service';
import { IOrderDetails } from 'app/entities/order-details/order-details.model';
import { OrderDetailsService } from 'app/entities/order-details/service/order-details.service';

@Component({
  selector: 'jhi-orders-update',
  templateUrl: './orders-update.component.html',
})
export class OrdersUpdateComponent implements OnInit {
  isSaving = false;
  orders: IOrders | null = null;

  exUsersSharedCollection: IExUser[] = [];
  orderStatusesSharedCollection: IOrderStatus[] = [];
  orderDetailsSharedCollection: IOrderDetails[] = [];

  editForm: OrdersFormGroup = this.ordersFormService.createOrdersFormGroup();

  constructor(
    protected ordersService: OrdersService,
    protected ordersFormService: OrdersFormService,
    protected exUserService: ExUserService,
    protected orderStatusService: OrderStatusService,
    protected orderDetailsService: OrderDetailsService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareExUser = (o1: IExUser | null, o2: IExUser | null): boolean => this.exUserService.compareExUser(o1, o2);

  compareOrderStatus = (o1: IOrderStatus | null, o2: IOrderStatus | null): boolean => this.orderStatusService.compareOrderStatus(o1, o2);

  compareOrderDetails = (o1: IOrderDetails | null, o2: IOrderDetails | null): boolean =>
    this.orderDetailsService.compareOrderDetails(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orders }) => {
      this.orders = orders;
      if (orders) {
        this.updateForm(orders);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orders = this.ordersFormService.getOrders(this.editForm);
    if (orders.id !== null) {
      this.subscribeToSaveResponse(this.ordersService.update(orders));
    } else {
      this.subscribeToSaveResponse(this.ordersService.create(orders));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrders>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(orders: IOrders): void {
    this.orders = orders;
    this.ordersFormService.resetForm(this.editForm, orders);

    this.exUsersSharedCollection = this.exUserService.addExUserToCollectionIfMissing<IExUser>(this.exUsersSharedCollection, orders.exUser);
    this.orderStatusesSharedCollection = this.orderStatusService.addOrderStatusToCollectionIfMissing<IOrderStatus>(
      this.orderStatusesSharedCollection,
      orders.orderStatus
    );
    this.orderDetailsSharedCollection = this.orderDetailsService.addOrderDetailsToCollectionIfMissing<IOrderDetails>(
      this.orderDetailsSharedCollection,
      ...(orders.orderDetails ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.exUserService
      .query()
      .pipe(map((res: HttpResponse<IExUser[]>) => res.body ?? []))
      .pipe(map((exUsers: IExUser[]) => this.exUserService.addExUserToCollectionIfMissing<IExUser>(exUsers, this.orders?.exUser)))
      .subscribe((exUsers: IExUser[]) => (this.exUsersSharedCollection = exUsers));

    this.orderStatusService
      .query()
      .pipe(map((res: HttpResponse<IOrderStatus[]>) => res.body ?? []))
      .pipe(
        map((orderStatuses: IOrderStatus[]) =>
          this.orderStatusService.addOrderStatusToCollectionIfMissing<IOrderStatus>(orderStatuses, this.orders?.orderStatus)
        )
      )
      .subscribe((orderStatuses: IOrderStatus[]) => (this.orderStatusesSharedCollection = orderStatuses));

    this.orderDetailsService
      .query()
      .pipe(map((res: HttpResponse<IOrderDetails[]>) => res.body ?? []))
      .pipe(
        map((orderDetails: IOrderDetails[]) =>
          this.orderDetailsService.addOrderDetailsToCollectionIfMissing<IOrderDetails>(orderDetails, ...(this.orders?.orderDetails ?? []))
        )
      )
      .subscribe((orderDetails: IOrderDetails[]) => (this.orderDetailsSharedCollection = orderDetails));
  }
}
