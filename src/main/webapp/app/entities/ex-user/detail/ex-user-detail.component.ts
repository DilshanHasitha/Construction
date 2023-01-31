import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExUser } from '../ex-user.model';

@Component({
  selector: 'jhi-ex-user-detail',
  templateUrl: './ex-user-detail.component.html',
})
export class ExUserDetailComponent implements OnInit {
  exUser: IExUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exUser }) => {
      this.exUser = exUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
