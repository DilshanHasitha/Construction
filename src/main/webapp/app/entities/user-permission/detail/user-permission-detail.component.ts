import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserPermission } from '../user-permission.model';

@Component({
  selector: 'jhi-user-permission-detail',
  templateUrl: './user-permission-detail.component.html',
})
export class UserPermissionDetailComponent implements OnInit {
  userPermission: IUserPermission | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userPermission }) => {
      this.userPermission = userPermission;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
