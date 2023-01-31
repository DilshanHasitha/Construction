import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { UserTypeFormService, UserTypeFormGroup } from './user-type-form.service';
import { IUserType } from '../user-type.model';
import { UserTypeService } from '../service/user-type.service';

@Component({
  selector: 'jhi-user-type-update',
  templateUrl: './user-type-update.component.html',
})
export class UserTypeUpdateComponent implements OnInit {
  isSaving = false;
  userType: IUserType | null = null;

  editForm: UserTypeFormGroup = this.userTypeFormService.createUserTypeFormGroup();

  constructor(
    protected userTypeService: UserTypeService,
    protected userTypeFormService: UserTypeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userType }) => {
      this.userType = userType;
      if (userType) {
        this.updateForm(userType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userType = this.userTypeFormService.getUserType(this.editForm);
    if (userType.id !== null) {
      this.subscribeToSaveResponse(this.userTypeService.update(userType));
    } else {
      this.subscribeToSaveResponse(this.userTypeService.create(userType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserType>>): void {
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

  protected updateForm(userType: IUserType): void {
    this.userType = userType;
    this.userTypeFormService.resetForm(this.editForm, userType);
  }
}
