import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { UnitOfMeasureFormService, UnitOfMeasureFormGroup } from './unit-of-measure-form.service';
import { IUnitOfMeasure } from '../unit-of-measure.model';
import { UnitOfMeasureService } from '../service/unit-of-measure.service';

@Component({
  selector: 'jhi-unit-of-measure-update',
  templateUrl: './unit-of-measure-update.component.html',
})
export class UnitOfMeasureUpdateComponent implements OnInit {
  isSaving = false;
  unitOfMeasure: IUnitOfMeasure | null = null;

  editForm: UnitOfMeasureFormGroup = this.unitOfMeasureFormService.createUnitOfMeasureFormGroup();

  constructor(
    protected unitOfMeasureService: UnitOfMeasureService,
    protected unitOfMeasureFormService: UnitOfMeasureFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ unitOfMeasure }) => {
      this.unitOfMeasure = unitOfMeasure;
      if (unitOfMeasure) {
        this.updateForm(unitOfMeasure);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const unitOfMeasure = this.unitOfMeasureFormService.getUnitOfMeasure(this.editForm);
    if (unitOfMeasure.id !== null) {
      this.subscribeToSaveResponse(this.unitOfMeasureService.update(unitOfMeasure));
    } else {
      this.subscribeToSaveResponse(this.unitOfMeasureService.create(unitOfMeasure));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUnitOfMeasure>>): void {
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

  protected updateForm(unitOfMeasure: IUnitOfMeasure): void {
    this.unitOfMeasure = unitOfMeasure;
    this.unitOfMeasureFormService.resetForm(this.editForm, unitOfMeasure);
  }
}
