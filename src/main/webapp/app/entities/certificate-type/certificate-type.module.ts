import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CertificateTypeComponent } from './list/certificate-type.component';
import { CertificateTypeDetailComponent } from './detail/certificate-type-detail.component';
import { CertificateTypeUpdateComponent } from './update/certificate-type-update.component';
import { CertificateTypeDeleteDialogComponent } from './delete/certificate-type-delete-dialog.component';
import { CertificateTypeRoutingModule } from './route/certificate-type-routing.module';

@NgModule({
  imports: [SharedModule, CertificateTypeRoutingModule],
  declarations: [
    CertificateTypeComponent,
    CertificateTypeDetailComponent,
    CertificateTypeUpdateComponent,
    CertificateTypeDeleteDialogComponent,
  ],
})
export class CertificateTypeModule {}
