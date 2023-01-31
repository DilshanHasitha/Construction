import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserPermission } from '../user-permission.model';
import { UserPermissionService } from '../service/user-permission.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './user-permission-delete-dialog.component.html',
})
export class UserPermissionDeleteDialogComponent {
  userPermission?: IUserPermission;

  constructor(protected userPermissionService: UserPermissionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userPermissionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
