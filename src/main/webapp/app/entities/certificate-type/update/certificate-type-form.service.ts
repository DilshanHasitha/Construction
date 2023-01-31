import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICertificateType, NewCertificateType } from '../certificate-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICertificateType for edit and NewCertificateTypeFormGroupInput for create.
 */
type CertificateTypeFormGroupInput = ICertificateType | PartialWithRequiredKeyOf<NewCertificateType>;

type CertificateTypeFormDefaults = Pick<NewCertificateType, 'id' | 'isActive'>;

type CertificateTypeFormGroupContent = {
  id: FormControl<ICertificateType['id'] | NewCertificateType['id']>;
  code: FormControl<ICertificateType['code']>;
  name: FormControl<ICertificateType['name']>;
  isActive: FormControl<ICertificateType['isActive']>;
};

export type CertificateTypeFormGroup = FormGroup<CertificateTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CertificateTypeFormService {
  createCertificateTypeFormGroup(certificateType: CertificateTypeFormGroupInput = { id: null }): CertificateTypeFormGroup {
    const certificateTypeRawValue = {
      ...this.getFormDefaults(),
      ...certificateType,
    };
    return new FormGroup<CertificateTypeFormGroupContent>({
      id: new FormControl(
        { value: certificateTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(certificateTypeRawValue.code, {
        validators: [Validators.required],
      }),
      name: new FormControl(certificateTypeRawValue.name),
      isActive: new FormControl(certificateTypeRawValue.isActive),
    });
  }

  getCertificateType(form: CertificateTypeFormGroup): ICertificateType | NewCertificateType {
    return form.getRawValue() as ICertificateType | NewCertificateType;
  }

  resetForm(form: CertificateTypeFormGroup, certificateType: CertificateTypeFormGroupInput): void {
    const certificateTypeRawValue = { ...this.getFormDefaults(), ...certificateType };
    form.reset(
      {
        ...certificateTypeRawValue,
        id: { value: certificateTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CertificateTypeFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
