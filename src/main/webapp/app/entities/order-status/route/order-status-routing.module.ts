import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrderStatusComponent } from '../list/order-status.component';
import { OrderStatusDetailComponent } from '../detail/order-status-detail.component';
import { OrderStatusUpdateComponent } from '../update/order-status-update.component';
import { OrderStatusRoutingResolveService } from './order-status-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const orderStatusRoute: Routes = [
  {
    path: '',
    component: OrderStatusComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrderStatusDetailComponent,
    resolve: {
      orderStatus: OrderStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrderStatusUpdateComponent,
    resolve: {
      orderStatus: OrderStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrderStatusUpdateComponent,
    resolve: {
      orderStatus: OrderStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(orderStatusRoute)],
  exports: [RouterModule],
})
export class OrderStatusRoutingModule {}
