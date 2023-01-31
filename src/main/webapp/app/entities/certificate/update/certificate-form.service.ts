import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICertificate, NewCertificate } from '../certificate.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICertificate for edit and NewCertificateFormGroupInput for create.
 */
type CertificateFormGroupInput = ICertificate | PartialWithRequiredKeyOf<NewCertificate>;

type CertificateFormDefaults = Pick<NewCertificate, 'id' | 'items'>;

type CertificateFormGroupContent = {
  id: FormControl<ICertificate['id'] | NewCertificate['id']>;
  imgUrl: FormControl<ICertificate['imgUrl']>;
  description: FormControl<ICertificate['description']>;
  certificateType: FormControl<ICertificate['certificateType']>;
  items: FormControl<ICertificate['items']>;
};

export type CertificateFormGroup = FormGroup<CertificateFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CertificateFormService {
  createCertificateFormGroup(certificate: CertificateFormGroupInput = { id: null }): CertificateFormGroup {
    const certificateRawValue = {
      ...this.getFormDefaults(),
      ...certificate,
    };
    return new FormGroup<CertificateFormGroupContent>({
      id: new FormControl(
        { value: certificateRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      imgUrl: new FormControl(certificateRawValue.imgUrl),
      description: new FormControl(certificateRawValue.description),
      certificateType: new FormControl(certificateRawValue.certificateType),
      items: new FormControl(certificateRawValue.items ?? []),
    });
  }

  getCertificate(form: CertificateFormGroup): ICertificate | NewCertificate {
    return form.getRawValue() as ICertificate | NewCertificate;
  }

  resetForm(form: CertificateFormGroup, certificate: CertificateFormGroupInput): void {
    const certificateRawValue = { ...this.getFormDefaults(), ...certificate };
    form.reset(
      {
        ...certificateRawValue,
        id: { value: certificateRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CertificateFormDefaults {
    return {
      id: null,
      items: [],
    };
  }
}
