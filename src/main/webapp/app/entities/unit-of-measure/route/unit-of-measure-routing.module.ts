import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UnitOfMeasureComponent } from '../list/unit-of-measure.component';
import { UnitOfMeasureDetailComponent } from '../detail/unit-of-measure-detail.component';
import { UnitOfMeasureUpdateComponent } from '../update/unit-of-measure-update.component';
import { UnitOfMeasureRoutingResolveService } from './unit-of-measure-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const unitOfMeasureRoute: Routes = [
  {
    path: '',
    component: UnitOfMeasureComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UnitOfMeasureDetailComponent,
    resolve: {
      unitOfMeasure: UnitOfMeasureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UnitOfMeasureUpdateComponent,
    resolve: {
      unitOfMeasure: UnitOfMeasureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UnitOfMeasureUpdateComponent,
    resolve: {
      unitOfMeasure: UnitOfMeasureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(unitOfMeasureRoute)],
  exports: [RouterModule],
})
export class UnitOfMeasureRoutingModule {}
