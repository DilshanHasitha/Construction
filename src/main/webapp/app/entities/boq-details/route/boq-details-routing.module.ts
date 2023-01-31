import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BOQDetailsComponent } from '../list/boq-details.component';
import { BOQDetailsDetailComponent } from '../detail/boq-details-detail.component';
import { BOQDetailsUpdateComponent } from '../update/boq-details-update.component';
import { BOQDetailsRoutingResolveService } from './boq-details-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const bOQDetailsRoute: Routes = [
  {
    path: '',
    component: BOQDetailsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BOQDetailsDetailComponent,
    resolve: {
      bOQDetails: BOQDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BOQDetailsUpdateComponent,
    resolve: {
      bOQDetails: BOQDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BOQDetailsUpdateComponent,
    resolve: {
      bOQDetails: BOQDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bOQDetailsRoute)],
  exports: [RouterModule],
})
export class BOQDetailsRoutingModule {}
