import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBOQDetails, NewBOQDetails } from '../boq-details.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBOQDetails for edit and NewBOQDetailsFormGroupInput for create.
 */
type BOQDetailsFormGroupInput = IBOQDetails | PartialWithRequiredKeyOf<NewBOQDetails>;

type BOQDetailsFormDefaults = Pick<NewBOQDetails, 'id' | 'isActive' | 'boqs'>;

type BOQDetailsFormGroupContent = {
  id: FormControl<IBOQDetails['id'] | NewBOQDetails['id']>;
  code: FormControl<IBOQDetails['code']>;
  orderPlacedOn: FormControl<IBOQDetails['orderPlacedOn']>;
  qty: FormControl<IBOQDetails['qty']>;
  isActive: FormControl<IBOQDetails['isActive']>;
  item: FormControl<IBOQDetails['item']>;
  per: FormControl<IBOQDetails['per']>;
  unit: FormControl<IBOQDetails['unit']>;
  boqs: FormControl<IBOQDetails['boqs']>;
};

export type BOQDetailsFormGroup = FormGroup<BOQDetailsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BOQDetailsFormService {
  createBOQDetailsFormGroup(bOQDetails: BOQDetailsFormGroupInput = { id: null }): BOQDetailsFormGroup {
    const bOQDetailsRawValue = {
      ...this.getFormDefaults(),
      ...bOQDetails,
    };
    return new FormGroup<BOQDetailsFormGroupContent>({
      id: new FormControl(
        { value: bOQDetailsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(bOQDetailsRawValue.code, {
        validators: [Validators.required],
      }),
      orderPlacedOn: new FormControl(bOQDetailsRawValue.orderPlacedOn),
      qty: new FormControl(bOQDetailsRawValue.qty),
      isActive: new FormControl(bOQDetailsRawValue.isActive),
      item: new FormControl(bOQDetailsRawValue.item),
      per: new FormControl(bOQDetailsRawValue.per),
      unit: new FormControl(bOQDetailsRawValue.unit),
      boqs: new FormControl(bOQDetailsRawValue.boqs ?? []),
    });
  }

  getBOQDetails(form: BOQDetailsFormGroup): IBOQDetails | NewBOQDetails {
    return form.getRawValue() as IBOQDetails | NewBOQDetails;
  }

  resetForm(form: BOQDetailsFormGroup, bOQDetails: BOQDetailsFormGroupInput): void {
    const bOQDetailsRawValue = { ...this.getFormDefaults(), ...bOQDetails };
    form.reset(
      {
        ...bOQDetailsRawValue,
        id: { value: bOQDetailsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BOQDetailsFormDefaults {
    return {
      id: null,
      isActive: false,
      boqs: [],
    };
  }
}
