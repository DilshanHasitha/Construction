import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BOQDetailsComponent } from './list/boq-details.component';
import { BOQDetailsDetailComponent } from './detail/boq-details-detail.component';
import { BOQDetailsUpdateComponent } from './update/boq-details-update.component';
import { BOQDetailsDeleteDialogComponent } from './delete/boq-details-delete-dialog.component';
import { BOQDetailsRoutingModule } from './route/boq-details-routing.module';

@NgModule({
  imports: [SharedModule, BOQDetailsRoutingModule],
  declarations: [BOQDetailsComponent, BOQDetailsDetailComponent, BOQDetailsUpdateComponent, BOQDetailsDeleteDialogComponent],
})
export class BOQDetailsModule {}
