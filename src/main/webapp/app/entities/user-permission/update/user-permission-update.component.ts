import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { UserPermissionFormService, UserPermissionFormGroup } from './user-permission-form.service';
import { IUserPermission } from '../user-permission.model';
import { UserPermissionService } from '../service/user-permission.service';

@Component({
  selector: 'jhi-user-permission-update',
  templateUrl: './user-permission-update.component.html',
})
export class UserPermissionUpdateComponent implements OnInit {
  isSaving = false;
  userPermission: IUserPermission | null = null;

  editForm: UserPermissionFormGroup = this.userPermissionFormService.createUserPermissionFormGroup();

  constructor(
    protected userPermissionService: UserPermissionService,
    protected userPermissionFormService: UserPermissionFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userPermission }) => {
      this.userPermission = userPermission;
      if (userPermission) {
        this.updateForm(userPermission);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userPermission = this.userPermissionFormService.getUserPermission(this.editForm);
    if (userPermission.id !== null) {
      this.subscribeToSaveResponse(this.userPermissionService.update(userPermission));
    } else {
      this.subscribeToSaveResponse(this.userPermissionService.create(userPermission));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserPermission>>): void {
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

  protected updateForm(userPermission: IUserPermission): void {
    this.userPermission = userPermission;
    this.userPermissionFormService.resetForm(this.editForm, userPermission);
  }
}
