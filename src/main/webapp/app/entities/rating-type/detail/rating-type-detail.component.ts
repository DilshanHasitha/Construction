import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRatingType } from '../rating-type.model';

@Component({
  selector: 'jhi-rating-type-detail',
  templateUrl: './rating-type-detail.component.html',
})
export class RatingTypeDetailComponent implements OnInit {
  ratingType: IRatingType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ratingType }) => {
      this.ratingType = ratingType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
