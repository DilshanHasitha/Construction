import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { RatingTypeFormService, RatingTypeFormGroup } from './rating-type-form.service';
import { IRatingType } from '../rating-type.model';
import { RatingTypeService } from '../service/rating-type.service';

@Component({
  selector: 'jhi-rating-type-update',
  templateUrl: './rating-type-update.component.html',
})
export class RatingTypeUpdateComponent implements OnInit {
  isSaving = false;
  ratingType: IRatingType | null = null;

  editForm: RatingTypeFormGroup = this.ratingTypeFormService.createRatingTypeFormGroup();

  constructor(
    protected ratingTypeService: RatingTypeService,
    protected ratingTypeFormService: RatingTypeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ratingType }) => {
      this.ratingType = ratingType;
      if (ratingType) {
        this.updateForm(ratingType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ratingType = this.ratingTypeFormService.getRatingType(this.editForm);
    if (ratingType.id !== null) {
      this.subscribeToSaveResponse(this.ratingTypeService.update(ratingType));
    } else {
      this.subscribeToSaveResponse(this.ratingTypeService.create(ratingType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRatingType>>): void {
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

  protected updateForm(ratingType: IRatingType): void {
    this.ratingType = ratingType;
    this.ratingTypeFormService.resetForm(this.editForm, ratingType);
  }
}
