import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { CertificateTypeFormService, CertificateTypeFormGroup } from './certificate-type-form.service';
import { ICertificateType } from '../certificate-type.model';
import { CertificateTypeService } from '../service/certificate-type.service';

@Component({
  selector: 'jhi-certificate-type-update',
  templateUrl: './certificate-type-update.component.html',
})
export class CertificateTypeUpdateComponent implements OnInit {
  isSaving = false;
  certificateType: ICertificateType | null = null;

  editForm: CertificateTypeFormGroup = this.certificateTypeFormService.createCertificateTypeFormGroup();

  constructor(
    protected certificateTypeService: CertificateTypeService,
    protected certificateTypeFormService: CertificateTypeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ certificateType }) => {
      this.certificateType = certificateType;
      if (certificateType) {
        this.updateForm(certificateType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const certificateType = this.certificateTypeFormService.getCertificateType(this.editForm);
    if (certificateType.id !== null) {
      this.subscribeToSaveResponse(this.certificateTypeService.update(certificateType));
    } else {
      this.subscribeToSaveResponse(this.certificateTypeService.create(certificateType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICertificateType>>): void {
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

  protected updateForm(certificateType: ICertificateType): void {
    this.certificateType = certificateType;
    this.certificateTypeFormService.resetForm(this.editForm, certificateType);
  }
}
