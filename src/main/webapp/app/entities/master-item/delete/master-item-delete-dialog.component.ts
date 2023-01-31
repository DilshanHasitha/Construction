import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMasterItem } from '../master-item.model';
import { MasterItemService } from '../service/master-item.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './master-item-delete-dialog.component.html',
})
export class MasterItemDeleteDialogComponent {
  masterItem?: IMasterItem;

  constructor(protected masterItemService: MasterItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.masterItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
