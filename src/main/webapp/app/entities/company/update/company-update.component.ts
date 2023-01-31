import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CompanyFormService, CompanyFormGroup } from './company-form.service';
import { ICompany } from '../company.model';
import { CompanyService } from '../service/company.service';
import { IUserType } from 'app/entities/user-type/user-type.model';
import { UserTypeService } from 'app/entities/user-type/service/user-type.service';

@Component({
  selector: 'jhi-company-update',
  templateUrl: './company-update.component.html',
})
export class CompanyUpdateComponent implements OnInit {
  isSaving = false;
  company: ICompany | null = null;

  userTypesSharedCollection: IUserType[] = [];

  editForm: CompanyFormGroup = this.companyFormService.createCompanyFormGroup();

  constructor(
    protected companyService: CompanyService,
    protected companyFormService: CompanyFormService,
    protected userTypeService: UserTypeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUserType = (o1: IUserType | null, o2: IUserType | null): boolean => this.userTypeService.compareUserType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ company }) => {
      this.company = company;
      if (company) {
        this.updateForm(company);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const company = this.companyFormService.getCompany(this.editForm);
    if (company.id !== null) {
      this.subscribeToSaveResponse(this.companyService.update(company));
    } else {
      this.subscribeToSaveResponse(this.companyService.create(company));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompany>>): void {
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

  protected updateForm(company: ICompany): void {
    this.company = company;
    this.companyFormService.resetForm(this.editForm, company);

    this.userTypesSharedCollection = this.userTypeService.addUserTypeToCollectionIfMissing<IUserType>(
      this.userTypesSharedCollection,
      company.userType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userTypeService
      .query()
      .pipe(map((res: HttpResponse<IUserType[]>) => res.body ?? []))
      .pipe(
        map((userTypes: IUserType[]) => this.userTypeService.addUserTypeToCollectionIfMissing<IUserType>(userTypes, this.company?.userType))
      )
      .subscribe((userTypes: IUserType[]) => (this.userTypesSharedCollection = userTypes));
  }
}
