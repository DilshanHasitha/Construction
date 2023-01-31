import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BOQDetailsFormService, BOQDetailsFormGroup } from './boq-details-form.service';
import { IBOQDetails } from '../boq-details.model';
import { BOQDetailsService } from '../service/boq-details.service';
import { IMasterItem } from 'app/entities/master-item/master-item.model';
import { MasterItemService } from 'app/entities/master-item/service/master-item.service';
import { IUnitOfMeasure } from 'app/entities/unit-of-measure/unit-of-measure.model';
import { UnitOfMeasureService } from 'app/entities/unit-of-measure/service/unit-of-measure.service';

@Component({
  selector: 'jhi-boq-details-update',
  templateUrl: './boq-details-update.component.html',
})
export class BOQDetailsUpdateComponent implements OnInit {
  isSaving = false;
  bOQDetails: IBOQDetails | null = null;

  masterItemsSharedCollection: IMasterItem[] = [];
  unitOfMeasuresSharedCollection: IUnitOfMeasure[] = [];

  editForm: BOQDetailsFormGroup = this.bOQDetailsFormService.createBOQDetailsFormGroup();

  constructor(
    protected bOQDetailsService: BOQDetailsService,
    protected bOQDetailsFormService: BOQDetailsFormService,
    protected masterItemService: MasterItemService,
    protected unitOfMeasureService: UnitOfMeasureService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareMasterItem = (o1: IMasterItem | null, o2: IMasterItem | null): boolean => this.masterItemService.compareMasterItem(o1, o2);

  compareUnitOfMeasure = (o1: IUnitOfMeasure | null, o2: IUnitOfMeasure | null): boolean =>
    this.unitOfMeasureService.compareUnitOfMeasure(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bOQDetails }) => {
      this.bOQDetails = bOQDetails;
      if (bOQDetails) {
        this.updateForm(bOQDetails);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bOQDetails = this.bOQDetailsFormService.getBOQDetails(this.editForm);
    if (bOQDetails.id !== null) {
      this.subscribeToSaveResponse(this.bOQDetailsService.update(bOQDetails));
    } else {
      this.subscribeToSaveResponse(this.bOQDetailsService.create(bOQDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBOQDetails>>): void {
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

  protected updateForm(bOQDetails: IBOQDetails): void {
    this.bOQDetails = bOQDetails;
    this.bOQDetailsFormService.resetForm(this.editForm, bOQDetails);

    this.masterItemsSharedCollection = this.masterItemService.addMasterItemToCollectionIfMissing<IMasterItem>(
      this.masterItemsSharedCollection,
      bOQDetails.item
    );
    this.unitOfMeasuresSharedCollection = this.unitOfMeasureService.addUnitOfMeasureToCollectionIfMissing<IUnitOfMeasure>(
      this.unitOfMeasuresSharedCollection,
      bOQDetails.per,
      bOQDetails.unit
    );
  }

  protected loadRelationshipsOptions(): void {
    this.masterItemService
      .query()
      .pipe(map((res: HttpResponse<IMasterItem[]>) => res.body ?? []))
      .pipe(
        map((masterItems: IMasterItem[]) =>
          this.masterItemService.addMasterItemToCollectionIfMissing<IMasterItem>(masterItems, this.bOQDetails?.item)
        )
      )
      .subscribe((masterItems: IMasterItem[]) => (this.masterItemsSharedCollection = masterItems));

    this.unitOfMeasureService
      .query()
      .pipe(map((res: HttpResponse<IUnitOfMeasure[]>) => res.body ?? []))
      .pipe(
        map((unitOfMeasures: IUnitOfMeasure[]) =>
          this.unitOfMeasureService.addUnitOfMeasureToCollectionIfMissing<IUnitOfMeasure>(
            unitOfMeasures,
            this.bOQDetails?.per,
            this.bOQDetails?.unit
          )
        )
      )
      .subscribe((unitOfMeasures: IUnitOfMeasure[]) => (this.unitOfMeasuresSharedCollection = unitOfMeasures));
  }
}
