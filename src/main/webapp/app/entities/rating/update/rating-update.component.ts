import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { RatingFormService, RatingFormGroup } from './rating-form.service';
import { IRating } from '../rating.model';
import { RatingService } from '../service/rating.service';
import { IRatingType } from 'app/entities/rating-type/rating-type.model';
import { RatingTypeService } from 'app/entities/rating-type/service/rating-type.service';

@Component({
  selector: 'jhi-rating-update',
  templateUrl: './rating-update.component.html',
})
export class RatingUpdateComponent implements OnInit {
  isSaving = false;
  rating: IRating | null = null;

  ratingTypesSharedCollection: IRatingType[] = [];

  editForm: RatingFormGroup = this.ratingFormService.createRatingFormGroup();

  constructor(
    protected ratingService: RatingService,
    protected ratingFormService: RatingFormService,
    protected ratingTypeService: RatingTypeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareRatingType = (o1: IRatingType | null, o2: IRatingType | null): boolean => this.ratingTypeService.compareRatingType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rating }) => {
      this.rating = rating;
      if (rating) {
        this.updateForm(rating);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rating = this.ratingFormService.getRating(this.editForm);
    if (rating.id !== null) {
      this.subscribeToSaveResponse(this.ratingService.update(rating));
    } else {
      this.subscribeToSaveResponse(this.ratingService.create(rating));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRating>>): void {
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

  protected updateForm(rating: IRating): void {
    this.rating = rating;
    this.ratingFormService.resetForm(this.editForm, rating);

    this.ratingTypesSharedCollection = this.ratingTypeService.addRatingTypeToCollectionIfMissing<IRatingType>(
      this.ratingTypesSharedCollection,
      rating.ratingType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ratingTypeService
      .query()
      .pipe(map((res: HttpResponse<IRatingType[]>) => res.body ?? []))
      .pipe(
        map((ratingTypes: IRatingType[]) =>
          this.ratingTypeService.addRatingTypeToCollectionIfMissing<IRatingType>(ratingTypes, this.rating?.ratingType)
        )
      )
      .subscribe((ratingTypes: IRatingType[]) => (this.ratingTypesSharedCollection = ratingTypes));
  }
}
