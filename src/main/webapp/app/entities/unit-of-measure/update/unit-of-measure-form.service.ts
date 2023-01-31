import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUnitOfMeasure, NewUnitOfMeasure } from '../unit-of-measure.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUnitOfMeasure for edit and NewUnitOfMeasureFormGroupInput for create.
 */
type UnitOfMeasureFormGroupInput = IUnitOfMeasure | PartialWithRequiredKeyOf<NewUnitOfMeasure>;

type UnitOfMeasureFormDefaults = Pick<NewUnitOfMeasure, 'id' | 'isActive'>;

type UnitOfMeasureFormGroupContent = {
  id: FormControl<IUnitOfMeasure['id'] | NewUnitOfMeasure['id']>;
  unitOfMeasureCode: FormControl<IUnitOfMeasure['unitOfMeasureCode']>;
  unitOfMeasureDescription: FormControl<IUnitOfMeasure['unitOfMeasureDescription']>;
  isActive: FormControl<IUnitOfMeasure['isActive']>;
};

export type UnitOfMeasureFormGroup = FormGroup<UnitOfMeasureFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UnitOfMeasureFormService {
  createUnitOfMeasureFormGroup(unitOfMeasure: UnitOfMeasureFormGroupInput = { id: null }): UnitOfMeasureFormGroup {
    const unitOfMeasureRawValue = {
      ...this.getFormDefaults(),
      ...unitOfMeasure,
    };
    return new FormGroup<UnitOfMeasureFormGroupContent>({
      id: new FormControl(
        { value: unitOfMeasureRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      unitOfMeasureCode: new FormControl(unitOfMeasureRawValue.unitOfMeasureCode, {
        validators: [Validators.required],
      }),
      unitOfMeasureDescription: new FormControl(unitOfMeasureRawValue.unitOfMeasureDescription, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(unitOfMeasureRawValue.isActive),
    });
  }

  getUnitOfMeasure(form: UnitOfMeasureFormGroup): IUnitOfMeasure | NewUnitOfMeasure {
    return form.getRawValue() as IUnitOfMeasure | NewUnitOfMeasure;
  }

  resetForm(form: UnitOfMeasureFormGroup, unitOfMeasure: UnitOfMeasureFormGroupInput): void {
    const unitOfMeasureRawValue = { ...this.getFormDefaults(), ...unitOfMeasure };
    form.reset(
      {
        ...unitOfMeasureRawValue,
        id: { value: unitOfMeasureRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): UnitOfMeasureFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
