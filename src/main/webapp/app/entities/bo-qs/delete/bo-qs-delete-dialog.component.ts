import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBOQs } from '../bo-qs.model';
import { BOQsService } from '../service/bo-qs.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './bo-qs-delete-dialog.component.html',
})
export class BOQsDeleteDialogComponent {
  bOQs?: IBOQs;

  constructor(protected bOQsService: BOQsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bOQsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
