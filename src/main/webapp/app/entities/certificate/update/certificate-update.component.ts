import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CertificateFormService, CertificateFormGroup } from './certificate-form.service';
import { ICertificate } from '../certificate.model';
import { CertificateService } from '../service/certificate.service';
import { ICertificateType } from 'app/entities/certificate-type/certificate-type.model';
import { CertificateTypeService } from 'app/entities/certificate-type/service/certificate-type.service';

@Component({
  selector: 'jhi-certificate-update',
  templateUrl: './certificate-update.component.html',
})
export class CertificateUpdateComponent implements OnInit {
  isSaving = false;
  certificate: ICertificate | null = null;

  certificateTypesSharedCollection: ICertificateType[] = [];

  editForm: CertificateFormGroup = this.certificateFormService.createCertificateFormGroup();

  constructor(
    protected certificateService: CertificateService,
    protected certificateFormService: CertificateFormService,
    protected certificateTypeService: CertificateTypeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCertificateType = (o1: ICertificateType | null, o2: ICertificateType | null): boolean =>
    this.certificateTypeService.compareCertificateType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ certificate }) => {
      this.certificate = certificate;
      if (certificate) {
        this.updateForm(certificate);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const certificate = this.certificateFormService.getCertificate(this.editForm);
    if (certificate.id !== null) {
      this.subscribeToSaveResponse(this.certificateService.update(certificate));
    } else {
      this.subscribeToSaveResponse(this.certificateService.create(certificate));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICertificate>>): void {
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

  protected updateForm(certificate: ICertificate): void {
    this.certificate = certificate;
    this.certificateFormService.resetForm(this.editForm, certificate);

    this.certificateTypesSharedCollection = this.certificateTypeService.addCertificateTypeToCollectionIfMissing<ICertificateType>(
      this.certificateTypesSharedCollection,
      certificate.certificateType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.certificateTypeService
      .query()
      .pipe(map((res: HttpResponse<ICertificateType[]>) => res.body ?? []))
      .pipe(
        map((certificateTypes: ICertificateType[]) =>
          this.certificateTypeService.addCertificateTypeToCollectionIfMissing<ICertificateType>(
            certificateTypes,
            this.certificate?.certificateType
          )
        )
      )
      .subscribe((certificateTypes: ICertificateType[]) => (this.certificateTypesSharedCollection = certificateTypes));
  }
}
