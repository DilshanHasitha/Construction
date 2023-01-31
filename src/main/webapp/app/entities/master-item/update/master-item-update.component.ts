import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { MasterItemFormService, MasterItemFormGroup } from './master-item-form.service';
import { IMasterItem } from '../master-item.model';
import { MasterItemService } from '../service/master-item.service';
import { IExUser } from 'app/entities/ex-user/ex-user.model';
import { ExUserService } from 'app/entities/ex-user/service/ex-user.service';

@Component({
  selector: 'jhi-master-item-update',
  templateUrl: './master-item-update.component.html',
})
export class MasterItemUpdateComponent implements OnInit {
  isSaving = false;
  masterItem: IMasterItem | null = null;

  exUsersSharedCollection: IExUser[] = [];

  editForm: MasterItemFormGroup = this.masterItemFormService.createMasterItemFormGroup();

  constructor(
    protected masterItemService: MasterItemService,
    protected masterItemFormService: MasterItemFormService,
    protected exUserService: ExUserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareExUser = (o1: IExUser | null, o2: IExUser | null): boolean => this.exUserService.compareExUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ masterItem }) => {
      this.masterItem = masterItem;
      if (masterItem) {
        this.updateForm(masterItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const masterItem = this.masterItemFormService.getMasterItem(this.editForm);
    if (masterItem.id !== null) {
      this.subscribeToSaveResponse(this.masterItemService.update(masterItem));
    } else {
      this.subscribeToSaveResponse(this.masterItemService.create(masterItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMasterItem>>): void {
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

  protected updateForm(masterItem: IMasterItem): void {
    this.masterItem = masterItem;
    this.masterItemFormService.resetForm(this.editForm, masterItem);

    this.exUsersSharedCollection = this.exUserService.addExUserToCollectionIfMissing<IExUser>(
      this.exUsersSharedCollection,
      masterItem.exUser
    );
  }

  protected loadRelationshipsOptions(): void {
    this.exUserService
      .query()
      .pipe(map((res: HttpResponse<IExUser[]>) => res.body ?? []))
      .pipe(map((exUsers: IExUser[]) => this.exUserService.addExUserToCollectionIfMissing<IExUser>(exUsers, this.masterItem?.exUser)))
      .subscribe((exUsers: IExUser[]) => (this.exUsersSharedCollection = exUsers));
  }
}
