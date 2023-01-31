import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUnitOfMeasure } from '../unit-of-measure.model';

@Component({
  selector: 'jhi-unit-of-measure-detail',
  templateUrl: './unit-of-measure-detail.component.html',
})
export class UnitOfMeasureDetailComponent implements OnInit {
  unitOfMeasure: IUnitOfMeasure | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ unitOfMeasure }) => {
      this.unitOfMeasure = unitOfMeasure;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
