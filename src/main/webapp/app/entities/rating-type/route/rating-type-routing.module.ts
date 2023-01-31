import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RatingTypeComponent } from '../list/rating-type.component';
import { RatingTypeDetailComponent } from '../detail/rating-type-detail.component';
import { RatingTypeUpdateComponent } from '../update/rating-type-update.component';
import { RatingTypeRoutingResolveService } from './rating-type-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const ratingTypeRoute: Routes = [
  {
    path: '',
    component: RatingTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RatingTypeDetailComponent,
    resolve: {
      ratingType: RatingTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RatingTypeUpdateComponent,
    resolve: {
      ratingType: RatingTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RatingTypeUpdateComponent,
    resolve: {
      ratingType: RatingTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ratingTypeRoute)],
  exports: [RouterModule],
})
export class RatingTypeRoutingModule {}
