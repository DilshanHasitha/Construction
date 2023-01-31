import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { UserRoleFormService, UserRoleFormGroup } from './user-role-form.service';
import { IUserRole } from '../user-role.model';
import { UserRoleService } from '../service/user-role.service';
import { IUserPermission } from 'app/entities/user-permission/user-permission.model';
import { UserPermissionService } from 'app/entities/user-permission/service/user-permission.service';

@Component({
  selector: 'jhi-user-role-update',
  templateUrl: './user-role-update.component.html',
})
export class UserRoleUpdateComponent implements OnInit {
  isSaving = false;
  userRole: IUserRole | null = null;

  userPermissionsSharedCollection: IUserPermission[] = [];

  editForm: UserRoleFormGroup = this.userRoleFormService.createUserRoleFormGroup();

  constructor(
    protected userRoleService: UserRoleService,
    protected userRoleFormService: UserRoleFormService,
    protected userPermissionService: UserPermissionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUserPermission = (o1: IUserPermission | null, o2: IUserPermission | null): boolean =>
    this.userPermissionService.compareUserPermission(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userRole }) => {
      this.userRole = userRole;
      if (userRole) {
        this.updateForm(userRole);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userRole = this.userRoleFormService.getUserRole(this.editForm);
    if (userRole.id !== null) {
      this.subscribeToSaveResponse(this.userRoleService.update(userRole));
    } else {
      this.subscribeToSaveResponse(this.userRoleService.create(userRole));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserRole>>): void {
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

  protected updateForm(userRole: IUserRole): void {
    this.userRole = userRole;
    this.userRoleFormService.resetForm(this.editForm, userRole);

    this.userPermissionsSharedCollection = this.userPermissionService.addUserPermissionToCollectionIfMissing<IUserPermission>(
      this.userPermissionsSharedCollection,
      ...(userRole.userPermissions ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userPermissionService
      .query()
      .pipe(map((res: HttpResponse<IUserPermission[]>) => res.body ?? []))
      .pipe(
        map((userPermissions: IUserPermission[]) =>
          this.userPermissionService.addUserPermissionToCollectionIfMissing<IUserPermission>(
            userPermissions,
            ...(this.userRole?.userPermissions ?? [])
          )
        )
      )
      .subscribe((userPermissions: IUserPermission[]) => (this.userPermissionsSharedCollection = userPermissions));
  }
}
