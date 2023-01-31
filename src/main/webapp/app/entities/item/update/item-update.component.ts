import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ItemFormService, ItemFormGroup } from './item-form.service';
import { IItem } from '../item.model';
import { ItemService } from '../service/item.service';
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

@Component({
  selector: 'jhi-item-update',
  templateUrl: './item-update.component.html',
})
export class ItemUpdateComponent implements OnInit {
  isSaving = false;
  item: IItem | null = null;

  masterItemsSharedCollection: IMasterItem[] = [];
  unitOfMeasuresSharedCollection: IUnitOfMeasure[] = [];
  exUsersSharedCollection: IExUser[] = [];
  ratingsSharedCollection: IRating[] = [];
  certificatesSharedCollection: ICertificate[] = [];

  editForm: ItemFormGroup = this.itemFormService.createItemFormGroup();

  constructor(
    protected itemService: ItemService,
    protected itemFormService: ItemFormService,
    protected masterItemService: MasterItemService,
    protected unitOfMeasureService: UnitOfMeasureService,
    protected exUserService: ExUserService,
    protected ratingService: RatingService,
    protected certificateService: CertificateService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareMasterItem = (o1: IMasterItem | null, o2: IMasterItem | null): boolean => this.masterItemService.compareMasterItem(o1, o2);

  compareUnitOfMeasure = (o1: IUnitOfMeasure | null, o2: IUnitOfMeasure | null): boolean =>
    this.unitOfMeasureService.compareUnitOfMeasure(o1, o2);

  compareExUser = (o1: IExUser | null, o2: IExUser | null): boolean => this.exUserService.compareExUser(o1, o2);

  compareRating = (o1: IRating | null, o2: IRating | null): boolean => this.ratingService.compareRating(o1, o2);

  compareCertificate = (o1: ICertificate | null, o2: ICertificate | null): boolean => this.certificateService.compareCertificate(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ item }) => {
      this.item = item;
      if (item) {
        this.updateForm(item);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const item = this.itemFormService.getItem(this.editForm);
    if (item.id !== null) {
      this.subscribeToSaveResponse(this.itemService.update(item));
    } else {
      this.subscribeToSaveResponse(this.itemService.create(item));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItem>>): void {
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

  protected updateForm(item: IItem): void {
    this.item = item;
    this.itemFormService.resetForm(this.editForm, item);

    this.masterItemsSharedCollection = this.masterItemService.addMasterItemToCollectionIfMissing<IMasterItem>(
      this.masterItemsSharedCollection,
      item.masterItem
    );
    this.unitOfMeasuresSharedCollection = this.unitOfMeasureService.addUnitOfMeasureToCollectionIfMissing<IUnitOfMeasure>(
      this.unitOfMeasuresSharedCollection,
      item.unit
    );
    this.exUsersSharedCollection = this.exUserService.addExUserToCollectionIfMissing<IExUser>(this.exUsersSharedCollection, item.exUser);
    this.ratingsSharedCollection = this.ratingService.addRatingToCollectionIfMissing<IRating>(
      this.ratingsSharedCollection,
      ...(item.ratings ?? [])
    );
    this.certificatesSharedCollection = this.certificateService.addCertificateToCollectionIfMissing<ICertificate>(
      this.certificatesSharedCollection,
      ...(item.certificates ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.masterItemService
      .query()
      .pipe(map((res: HttpResponse<IMasterItem[]>) => res.body ?? []))
      .pipe(
        map((masterItems: IMasterItem[]) =>
          this.masterItemService.addMasterItemToCollectionIfMissing<IMasterItem>(masterItems, this.item?.masterItem)
        )
      )
      .subscribe((masterItems: IMasterItem[]) => (this.masterItemsSharedCollection = masterItems));

    this.unitOfMeasureService
      .query()
      .pipe(map((res: HttpResponse<IUnitOfMeasure[]>) => res.body ?? []))
      .pipe(
        map((unitOfMeasures: IUnitOfMeasure[]) =>
          this.unitOfMeasureService.addUnitOfMeasureToCollectionIfMissing<IUnitOfMeasure>(unitOfMeasures, this.item?.unit)
        )
      )
      .subscribe((unitOfMeasures: IUnitOfMeasure[]) => (this.unitOfMeasuresSharedCollection = unitOfMeasures));

    this.exUserService
      .query()
      .pipe(map((res: HttpResponse<IExUser[]>) => res.body ?? []))
      .pipe(map((exUsers: IExUser[]) => this.exUserService.addExUserToCollectionIfMissing<IExUser>(exUsers, this.item?.exUser)))
      .subscribe((exUsers: IExUser[]) => (this.exUsersSharedCollection = exUsers));

    this.ratingService
      .query()
      .pipe(map((res: HttpResponse<IRating[]>) => res.body ?? []))
      .pipe(map((ratings: IRating[]) => this.ratingService.addRatingToCollectionIfMissing<IRating>(ratings, ...(this.item?.ratings ?? []))))
      .subscribe((ratings: IRating[]) => (this.ratingsSharedCollection = ratings));

    this.certificateService
      .query()
      .pipe(map((res: HttpResponse<ICertificate[]>) => res.body ?? []))
      .pipe(
        map((certificates: ICertificate[]) =>
          this.certificateService.addCertificateToCollectionIfMissing<ICertificate>(certificates, ...(this.item?.certificates ?? []))
        )
      )
      .subscribe((certificates: ICertificate[]) => (this.certificatesSharedCollection = certificates));
  }
}
