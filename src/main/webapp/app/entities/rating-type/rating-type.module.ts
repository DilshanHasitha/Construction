import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RatingTypeComponent } from './list/rating-type.component';
import { RatingTypeDetailComponent } from './detail/rating-type-detail.component';
import { RatingTypeUpdateComponent } from './update/rating-type-update.component';
import { RatingTypeDeleteDialogComponent } from './delete/rating-type-delete-dialog.component';
import { RatingTypeRoutingModule } from './route/rating-type-routing.module';

@NgModule({
  imports: [SharedModule, RatingTypeRoutingModule],
  declarations: [RatingTypeComponent, RatingTypeDetailComponent, RatingTypeUpdateComponent, RatingTypeDeleteDialogComponent],
})
export class RatingTypeModule {}
