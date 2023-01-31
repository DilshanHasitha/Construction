import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUnitOfMeasure } from '../unit-of-measure.model';
import { UnitOfMeasureService } from '../service/unit-of-measure.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './unit-of-measure-delete-dialog.component.html',
})
export class UnitOfMeasureDeleteDialogComponent {
  unitOfMeasure?: IUnitOfMeasure;

  constructor(protected unitOfMeasureService: UnitOfMeasureService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.unitOfMeasureService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
