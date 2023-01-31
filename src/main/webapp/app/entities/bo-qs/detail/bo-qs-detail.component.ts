import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBOQs } from '../bo-qs.model';

@Component({
  selector: 'jhi-bo-qs-detail',
  templateUrl: './bo-qs-detail.component.html',
})
export class BOQsDetailComponent implements OnInit {
  bOQs: IBOQs | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bOQs }) => {
      this.bOQs = bOQs;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
