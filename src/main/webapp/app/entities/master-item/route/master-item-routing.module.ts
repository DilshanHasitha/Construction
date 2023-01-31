import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MasterItemComponent } from '../list/master-item.component';
import { MasterItemDetailComponent } from '../detail/master-item-detail.component';
import { MasterItemUpdateComponent } from '../update/master-item-update.component';
import { MasterItemRoutingResolveService } from './master-item-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const masterItemRoute: Routes = [
  {
    path: '',
    component: MasterItemComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MasterItemDetailComponent,
    resolve: {
      masterItem: MasterItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MasterItemUpdateComponent,
    resolve: {
      masterItem: MasterItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MasterItemUpdateComponent,
    resolve: {
      masterItem: MasterItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(masterItemRoute)],
  exports: [RouterModule],
})
export class MasterItemRoutingModule {}
