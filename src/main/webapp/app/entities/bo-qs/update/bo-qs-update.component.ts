import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BOQsFormService, BOQsFormGroup } from './bo-qs-form.service';
import { IBOQs } from '../bo-qs.model';
import { BOQsService } from '../service/bo-qs.service';
import { IExUser } from 'app/entities/ex-user/ex-user.model';
import { ExUserService } from 'app/entities/ex-user/service/ex-user.service';
import { IBOQDetails } from 'app/entities/boq-details/boq-details.model';
import { BOQDetailsService } from 'app/entities/boq-details/service/boq-details.service';

@Component({
  selector: 'jhi-bo-qs-update',
  templateUrl: './bo-qs-update.component.html',
})
export class BOQsUpdateComponent implements OnInit {
  isSaving = false;
  bOQs: IBOQs | null = null;

  exUsersSharedCollection: IExUser[] = [];
  bOQDetailsSharedCollection: IBOQDetails[] = [];

  editForm: BOQsFormGroup = this.bOQsFormService.createBOQsFormGroup();

  constructor(
    protected bOQsService: BOQsService,
    protected bOQsFormService: BOQsFormService,
    protected exUserService: ExUserService,
    protected bOQDetailsService: BOQDetailsService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareExUser = (o1: IExUser | null, o2: IExUser | null): boolean => this.exUserService.compareExUser(o1, o2);

  compareBOQDetails = (o1: IBOQDetails | null, o2: IBOQDetails | null): boolean => this.bOQDetailsService.compareBOQDetails(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bOQs }) => {
      this.bOQs = bOQs;
      if (bOQs) {
        this.updateForm(bOQs);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bOQs = this.bOQsFormService.getBOQs(this.editForm);
    if (bOQs.id !== null) {
      this.subscribeToSaveResponse(this.bOQsService.update(bOQs));
    } else {
      this.subscribeToSaveResponse(this.bOQsService.create(bOQs));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBOQs>>): void {
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

  protected updateForm(bOQs: IBOQs): void {
    this.bOQs = bOQs;
    this.bOQsFormService.resetForm(this.editForm, bOQs);

    this.exUsersSharedCollection = this.exUserService.addExUserToCollectionIfMissing<IExUser>(
      this.exUsersSharedCollection,
      bOQs.constructors
    );
    this.bOQDetailsSharedCollection = this.bOQDetailsService.addBOQDetailsToCollectionIfMissing<IBOQDetails>(
      this.bOQDetailsSharedCollection,
      ...(bOQs.boqDetails ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.exUserService
      .query()
      .pipe(map((res: HttpResponse<IExUser[]>) => res.body ?? []))
      .pipe(map((exUsers: IExUser[]) => this.exUserService.addExUserToCollectionIfMissing<IExUser>(exUsers, this.bOQs?.constructors)))
      .subscribe((exUsers: IExUser[]) => (this.exUsersSharedCollection = exUsers));

    this.bOQDetailsService
      .query()
      .pipe(map((res: HttpResponse<IBOQDetails[]>) => res.body ?? []))
      .pipe(
        map((bOQDetails: IBOQDetails[]) =>
          this.bOQDetailsService.addBOQDetailsToCollectionIfMissing<IBOQDetails>(bOQDetails, ...(this.bOQs?.boqDetails ?? []))
        )
      )
      .subscribe((bOQDetails: IBOQDetails[]) => (this.bOQDetailsSharedCollection = bOQDetails));
  }
}
