import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMasterItem, NewMasterItem } from '../master-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMasterItem for edit and NewMasterItemFormGroupInput for create.
 */
type MasterItemFormGroupInput = IMasterItem | PartialWithRequiredKeyOf<NewMasterItem>;

type MasterItemFormDefaults = Pick<NewMasterItem, 'id' | 'isActive'>;

type MasterItemFormGroupContent = {
  id: FormControl<IMasterItem['id'] | NewMasterItem['id']>;
  code: FormControl<IMasterItem['code']>;
  name: FormControl<IMasterItem['name']>;
  description: FormControl<IMasterItem['description']>;
  isActive: FormControl<IMasterItem['isActive']>;
  exUser: FormControl<IMasterItem['exUser']>;
};

export type MasterItemFormGroup = FormGroup<MasterItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MasterItemFormService {
  createMasterItemFormGroup(masterItem: MasterItemFormGroupInput = { id: null }): MasterItemFormGroup {
    const masterItemRawValue = {
      ...this.getFormDefaults(),
      ...masterItem,
    };
    return new FormGroup<MasterItemFormGroupContent>({
      id: new FormControl(
        { value: masterItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(masterItemRawValue.code, {
        validators: [Validators.required],
      }),
      name: new FormControl(masterItemRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(masterItemRawValue.description),
      isActive: new FormControl(masterItemRawValue.isActive),
      exUser: new FormControl(masterItemRawValue.exUser),
    });
  }

  getMasterItem(form: MasterItemFormGroup): IMasterItem | NewMasterItem {
    return form.getRawValue() as IMasterItem | NewMasterItem;
  }

  resetForm(form: MasterItemFormGroup, masterItem: MasterItemFormGroupInput): void {
    const masterItemRawValue = { ...this.getFormDefaults(), ...masterItem };
    form.reset(
      {
        ...masterItemRawValue,
        id: { value: masterItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MasterItemFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
