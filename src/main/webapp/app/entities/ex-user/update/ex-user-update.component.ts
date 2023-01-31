import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ExUserFormService, ExUserFormGroup } from './ex-user-form.service';
import { IExUser } from '../ex-user.model';
import { ExUserService } from '../service/ex-user.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IUserRole } from 'app/entities/user-role/user-role.model';
import { UserRoleService } from 'app/entities/user-role/service/user-role.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';

@Component({
  selector: 'jhi-ex-user-update',
  templateUrl: './ex-user-update.component.html',
})
export class ExUserUpdateComponent implements OnInit {
  isSaving = false;
  exUser: IExUser | null = null;

  usersSharedCollection: IUser[] = [];
  userRolesSharedCollection: IUserRole[] = [];
  companiesSharedCollection: ICompany[] = [];

  editForm: ExUserFormGroup = this.exUserFormService.createExUserFormGroup();

  constructor(
    protected exUserService: ExUserService,
    protected exUserFormService: ExUserFormService,
    protected userService: UserService,
    protected userRoleService: UserRoleService,
    protected companyService: CompanyService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareUserRole = (o1: IUserRole | null, o2: IUserRole | null): boolean => this.userRoleService.compareUserRole(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exUser }) => {
      this.exUser = exUser;
      if (exUser) {
        this.updateForm(exUser);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const exUser = this.exUserFormService.getExUser(this.editForm);
    if (exUser.id !== null) {
      this.subscribeToSaveResponse(this.exUserService.update(exUser));
    } else {
      this.subscribeToSaveResponse(this.exUserService.create(exUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExUser>>): void {
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

  protected updateForm(exUser: IExUser): void {
    this.exUser = exUser;
    this.exUserFormService.resetForm(this.editForm, exUser);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, exUser.user);
    this.userRolesSharedCollection = this.userRoleService.addUserRoleToCollectionIfMissing<IUserRole>(
      this.userRolesSharedCollection,
      exUser.userRole
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      exUser.company
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.exUser?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.userRoleService
      .query()
      .pipe(map((res: HttpResponse<IUserRole[]>) => res.body ?? []))
      .pipe(
        map((userRoles: IUserRole[]) => this.userRoleService.addUserRoleToCollectionIfMissing<IUserRole>(userRoles, this.exUser?.userRole))
      )
      .subscribe((userRoles: IUserRole[]) => (this.userRolesSharedCollection = userRoles));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.exUser?.company)))
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));
  }
}
