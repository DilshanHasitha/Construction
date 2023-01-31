import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { OrderDetailsFormService, OrderDetailsFormGroup } from './order-details-form.service';
import { IOrderDetails } from '../order-details.model';
import { OrderDetailsService } from '../service/order-details.service';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';

@Component({
  selector: 'jhi-order-details-update',
  templateUrl: './order-details-update.component.html',
})
export class OrderDetailsUpdateComponent implements OnInit {
  isSaving = false;
  orderDetails: IOrderDetails | null = null;

  itemsSharedCollection: IItem[] = [];

  editForm: OrderDetailsFormGroup = this.orderDetailsFormService.createOrderDetailsFormGroup();

  constructor(
    protected orderDetailsService: OrderDetailsService,
    protected orderDetailsFormService: OrderDetailsFormService,
    protected itemService: ItemService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareItem = (o1: IItem | null, o2: IItem | null): boolean => this.itemService.compareItem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderDetails }) => {
      this.orderDetails = orderDetails;
      if (orderDetails) {
        this.updateForm(orderDetails);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orderDetails = this.orderDetailsFormService.getOrderDetails(this.editForm);
    if (orderDetails.id !== null) {
      this.subscribeToSaveResponse(this.orderDetailsService.update(orderDetails));
    } else {
      this.subscribeToSaveResponse(this.orderDetailsService.create(orderDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrderDetails>>): void {
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

  protected updateForm(orderDetails: IOrderDetails): void {
    this.orderDetails = orderDetails;
    this.orderDetailsFormService.resetForm(this.editForm, orderDetails);

    this.itemsSharedCollection = this.itemService.addItemToCollectionIfMissing<IItem>(this.itemsSharedCollection, orderDetails.item);
  }

  protected loadRelationshipsOptions(): void {
    this.itemService
      .query()
      .pipe(map((res: HttpResponse<IItem[]>) => res.body ?? []))
      .pipe(map((items: IItem[]) => this.itemService.addItemToCollectionIfMissing<IItem>(items, this.orderDetails?.item)))
      .subscribe((items: IItem[]) => (this.itemsSharedCollection = items));
  }
}
