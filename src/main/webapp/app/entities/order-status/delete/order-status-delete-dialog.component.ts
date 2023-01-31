import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrderStatus } from '../order-status.model';
import { OrderStatusService } from '../service/order-status.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './order-status-delete-dialog.component.html',
})
export class OrderStatusDeleteDialogComponent {
  orderStatus?: IOrderStatus;

  constructor(protected orderStatusService: OrderStatusService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orderStatusService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
