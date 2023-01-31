import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ExUserComponent } from './list/ex-user.component';
import { ExUserDetailComponent } from './detail/ex-user-detail.component';
import { ExUserUpdateComponent } from './update/ex-user-update.component';
import { ExUserDeleteDialogComponent } from './delete/ex-user-delete-dialog.component';
import { ExUserRoutingModule } from './route/ex-user-routing.module';

@NgModule({
  imports: [SharedModule, ExUserRoutingModule],
  declarations: [ExUserComponent, ExUserDetailComponent, ExUserUpdateComponent, ExUserDeleteDialogComponent],
})
export class ExUserModule {}
