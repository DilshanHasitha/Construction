import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UserPermissionComponent } from '../list/user-permission.component';
import { UserPermissionDetailComponent } from '../detail/user-permission-detail.component';
import { UserPermissionUpdateComponent } from '../update/user-permission-update.component';
import { UserPermissionRoutingResolveService } from './user-permission-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const userPermissionRoute: Routes = [
  {
    path: '',
    component: UserPermissionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserPermissionDetailComponent,
    resolve: {
      userPermission: UserPermissionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserPermissionUpdateComponent,
    resolve: {
      userPermission: UserPermissionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserPermissionUpdateComponent,
    resolve: {
      userPermission: UserPermissionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(userPermissionRoute)],
  exports: [RouterModule],
})
export class UserPermissionRoutingModule {}
