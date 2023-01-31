import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBOQDetails } from '../boq-details.model';

@Component({
  selector: 'jhi-boq-details-detail',
  templateUrl: './boq-details-detail.component.html',
})
export class BOQDetailsDetailComponent implements OnInit {
  bOQDetails: IBOQDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bOQDetails }) => {
      this.bOQDetails = bOQDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
