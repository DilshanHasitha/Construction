import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBOQs, NewBOQs } from '../bo-qs.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBOQs for edit and NewBOQsFormGroupInput for create.
 */
type BOQsFormGroupInput = IBOQs | PartialWithRequiredKeyOf<NewBOQs>;

type BOQsFormDefaults = Pick<NewBOQs, 'id' | 'isActive' | 'boqDetails'>;

type BOQsFormGroupContent = {
  id: FormControl<IBOQs['id'] | NewBOQs['id']>;
  code: FormControl<IBOQs['code']>;
  isActive: FormControl<IBOQs['isActive']>;
  constructors: FormControl<IBOQs['constructors']>;
  boqDetails: FormControl<IBOQs['boqDetails']>;
};

export type BOQsFormGroup = FormGroup<BOQsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BOQsFormService {
  createBOQsFormGroup(bOQs: BOQsFormGroupInput = { id: null }): BOQsFormGroup {
    const bOQsRawValue = {
      ...this.getFormDefaults(),
      ...bOQs,
    };
    return new FormGroup<BOQsFormGroupContent>({
      id: new FormControl(
        { value: bOQsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(bOQsRawValue.code),
      isActive: new FormControl(bOQsRawValue.isActive),
      constructors: new FormControl(bOQsRawValue.constructors),
      boqDetails: new FormControl(bOQsRawValue.boqDetails ?? []),
    });
  }

  getBOQs(form: BOQsFormGroup): IBOQs | NewBOQs {
    return form.getRawValue() as IBOQs | NewBOQs;
  }

  resetForm(form: BOQsFormGroup, bOQs: BOQsFormGroupInput): void {
    const bOQsRawValue = { ...this.getFormDefaults(), ...bOQs };
    form.reset(
      {
        ...bOQsRawValue,
        id: { value: bOQsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BOQsFormDefaults {
    return {
      id: null,
      isActive: false,
      boqDetails: [],
    };
  }
}
