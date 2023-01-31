import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MasterItemComponent } from './list/master-item.component';
import { MasterItemDetailComponent } from './detail/master-item-detail.component';
import { MasterItemUpdateComponent } from './update/master-item-update.component';
import { MasterItemDeleteDialogComponent } from './delete/master-item-delete-dialog.component';
import { MasterItemRoutingModule } from './route/master-item-routing.module';

@NgModule({
  imports: [SharedModule, MasterItemRoutingModule],
  declarations: [MasterItemComponent, MasterItemDetailComponent, MasterItemUpdateComponent, MasterItemDeleteDialogComponent],
})
export class MasterItemModule {}
