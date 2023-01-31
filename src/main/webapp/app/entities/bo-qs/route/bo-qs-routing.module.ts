import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BOQsComponent } from '../list/bo-qs.component';
import { BOQsDetailComponent } from '../detail/bo-qs-detail.component';
import { BOQsUpdateComponent } from '../update/bo-qs-update.component';
import { BOQsRoutingResolveService } from './bo-qs-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const bOQsRoute: Routes = [
  {
    path: '',
    component: BOQsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BOQsDetailComponent,
    resolve: {
      bOQs: BOQsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BOQsUpdateComponent,
    resolve: {
      bOQs: BOQsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BOQsUpdateComponent,
    resolve: {
      bOQs: BOQsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bOQsRoute)],
  exports: [RouterModule],
})
export class BOQsRoutingModule {}
