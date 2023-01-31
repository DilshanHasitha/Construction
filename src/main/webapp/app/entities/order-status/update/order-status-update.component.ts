import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { OrderStatusFormService, OrderStatusFormGroup } from './order-status-form.service';
import { IOrderStatus } from '../order-status.model';
import { OrderStatusService } from '../service/order-status.service';

@Component({
  selector: 'jhi-order-status-update',
  templateUrl: './order-status-update.component.html',
})
export class OrderStatusUpdateComponent implements OnInit {
  isSaving = false;
  orderStatus: IOrderStatus | null = null;

  editForm: OrderStatusFormGroup = this.orderStatusFormService.createOrderStatusFormGroup();

  constructor(
    protected orderStatusService: OrderStatusService,
    protected orderStatusFormService: OrderStatusFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderStatus }) => {
      this.orderStatus = orderStatus;
      if (orderStatus) {
        this.updateForm(orderStatus);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orderStatus = this.orderStatusFormService.getOrderStatus(this.editForm);
    if (orderStatus.id !== null) {
      this.subscribeToSaveResponse(this.orderStatusService.update(orderStatus));
    } else {
      this.subscribeToSaveResponse(this.orderStatusService.create(orderStatus));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrderStatus>>): void {
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

  protected updateForm(orderStatus: IOrderStatus): void {
    this.orderStatus = orderStatus;
    this.orderStatusFormService.resetForm(this.editForm, orderStatus);
  }
}
