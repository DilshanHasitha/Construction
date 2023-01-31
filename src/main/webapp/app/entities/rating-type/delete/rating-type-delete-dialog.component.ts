import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRatingType } from '../rating-type.model';
import { RatingTypeService } from '../service/rating-type.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './rating-type-delete-dialog.component.html',
})
export class RatingTypeDeleteDialogComponent {
  ratingType?: IRatingType;

  constructor(protected ratingTypeService: RatingTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ratingTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
