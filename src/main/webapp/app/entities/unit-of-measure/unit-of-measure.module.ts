import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UnitOfMeasureComponent } from './list/unit-of-measure.component';
import { UnitOfMeasureDetailComponent } from './detail/unit-of-measure-detail.component';
import { UnitOfMeasureUpdateComponent } from './update/unit-of-measure-update.component';
import { UnitOfMeasureDeleteDialogComponent } from './delete/unit-of-measure-delete-dialog.component';
import { UnitOfMeasureRoutingModule } from './route/unit-of-measure-routing.module';

@NgModule({
  imports: [SharedModule, UnitOfMeasureRoutingModule],
  declarations: [UnitOfMeasureComponent, UnitOfMeasureDetailComponent, UnitOfMeasureUpdateComponent, UnitOfMeasureDeleteDialogComponent],
})
export class UnitOfMeasureModule {}
