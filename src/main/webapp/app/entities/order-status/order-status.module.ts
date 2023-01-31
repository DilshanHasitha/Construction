import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OrderStatusComponent } from './list/order-status.component';
import { OrderStatusDetailComponent } from './detail/order-status-detail.component';
import { OrderStatusUpdateComponent } from './update/order-status-update.component';
import { OrderStatusDeleteDialogComponent } from './delete/order-status-delete-dialog.component';
import { OrderStatusRoutingModule } from './route/order-status-routing.module';

@NgModule({
  imports: [SharedModule, OrderStatusRoutingModule],
  declarations: [OrderStatusComponent, OrderStatusDetailComponent, OrderStatusUpdateComponent, OrderStatusDeleteDialogComponent],
})
export class OrderStatusModule {}
