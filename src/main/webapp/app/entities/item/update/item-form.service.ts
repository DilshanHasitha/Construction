import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IItem, NewItem } from '../item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IItem for edit and NewItemFormGroupInput for create.
 */
type ItemFormGroupInput = IItem | PartialWithRequiredKeyOf<NewItem>;

type ItemFormDefaults = Pick<NewItem, 'id' | 'isActive' | 'ratings' | 'certificates'>;

type ItemFormGroupContent = {
  id: FormControl<IItem['id'] | NewItem['id']>;
  itemPrice: FormControl<IItem['itemPrice']>;
  itemCost: FormControl<IItem['itemCost']>;
  bannerText: FormControl<IItem['bannerText']>;
  specialPrice: FormControl<IItem['specialPrice']>;
  isActive: FormControl<IItem['isActive']>;
  minQTY: FormControl<IItem['minQTY']>;
  maxQTY: FormControl<IItem['maxQTY']>;
  steps: FormControl<IItem['steps']>;
  longDescription: FormControl<IItem['longDescription']>;
  leadTime: FormControl<IItem['leadTime']>;
  reorderQty: FormControl<IItem['reorderQty']>;
  itemBarcode: FormControl<IItem['itemBarcode']>;
  masterItem: FormControl<IItem['masterItem']>;
  unit: FormControl<IItem['unit']>;
  exUser: FormControl<IItem['exUser']>;
  ratings: FormControl<IItem['ratings']>;
  certificates: FormControl<IItem['certificates']>;
};

export type ItemFormGroup = FormGroup<ItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ItemFormService {
  createItemFormGroup(item: ItemFormGroupInput = { id: null }): ItemFormGroup {
    const itemRawValue = {
      ...this.getFormDefaults(),
      ...item,
    };
    return new FormGroup<ItemFormGroupContent>({
      id: new FormControl(
        { value: itemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      itemPrice: new FormControl(itemRawValue.itemPrice),
      itemCost: new FormControl(itemRawValue.itemCost),
      bannerText: new FormControl(itemRawValue.bannerText),
      specialPrice: new FormControl(itemRawValue.specialPrice),
      isActive: new FormControl(itemRawValue.isActive),
      minQTY: new FormControl(itemRawValue.minQTY),
      maxQTY: new FormControl(itemRawValue.maxQTY),
      steps: new FormControl(itemRawValue.steps),
      longDescription: new FormControl(itemRawValue.longDescription),
      leadTime: new FormControl(itemRawValue.leadTime),
      reorderQty: new FormControl(itemRawValue.reorderQty),
      itemBarcode: new FormControl(itemRawValue.itemBarcode),
      masterItem: new FormControl(itemRawValue.masterItem),
      unit: new FormControl(itemRawValue.unit),
      exUser: new FormControl(itemRawValue.exUser),
      ratings: new FormControl(itemRawValue.ratings ?? []),
      certificates: new FormControl(itemRawValue.certificates ?? []),
    });
  }

  getItem(form: ItemFormGroup): IItem | NewItem {
    return form.getRawValue() as IItem | NewItem;
  }

  resetForm(form: ItemFormGroup, item: ItemFormGroupInput): void {
    const itemRawValue = { ...this.getFormDefaults(), ...item };
    form.reset(
      {
        ...itemRawValue,
        id: { value: itemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ItemFormDefaults {
    return {
      id: null,
      isActive: false,
      ratings: [],
      certificates: [],
    };
  }
}
