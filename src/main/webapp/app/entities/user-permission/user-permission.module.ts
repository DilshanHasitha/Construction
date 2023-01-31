import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UserPermissionComponent } from './list/user-permission.component';
import { UserPermissionDetailComponent } from './detail/user-permission-detail.component';
import { UserPermissionUpdateComponent } from './update/user-permission-update.component';
import { UserPermissionDeleteDialogComponent } from './delete/user-permission-delete-dialog.component';
import { UserPermissionRoutingModule } from './route/user-permission-routing.module';

@NgModule({
  imports: [SharedModule, UserPermissionRoutingModule],
  declarations: [
    UserPermissionComponent,
    UserPermissionDetailComponent,
    UserPermissionUpdateComponent,
    UserPermissionDeleteDialogComponent,
  ],
})
export class UserPermissionModule {}
