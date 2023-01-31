import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BOQsComponent } from './list/bo-qs.component';
import { BOQsDetailComponent } from './detail/bo-qs-detail.component';
import { BOQsUpdateComponent } from './update/bo-qs-update.component';
import { BOQsDeleteDialogComponent } from './delete/bo-qs-delete-dialog.component';
import { BOQsRoutingModule } from './route/bo-qs-routing.module';

@NgModule({
  imports: [SharedModule, BOQsRoutingModule],
  declarations: [BOQsComponent, BOQsDetailComponent, BOQsUpdateComponent, BOQsDeleteDialogComponent],
})
export class BOQsModule {}
