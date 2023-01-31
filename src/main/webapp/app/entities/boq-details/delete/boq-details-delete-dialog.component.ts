import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBOQDetails } from '../boq-details.model';
import { BOQDetailsService } from '../service/boq-details.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './boq-details-delete-dialog.component.html',
})
export class BOQDetailsDeleteDialogComponent {
  bOQDetails?: IBOQDetails;

  constructor(protected bOQDetailsService: BOQDetailsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bOQDetailsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
