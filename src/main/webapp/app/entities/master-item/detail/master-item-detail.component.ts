import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMasterItem } from '../master-item.model';

@Component({
  selector: 'jhi-master-item-detail',
  templateUrl: './master-item-detail.component.html',
})
export class MasterItemDetailComponent implements OnInit {
  masterItem: IMasterItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ masterItem }) => {
      this.masterItem = masterItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
