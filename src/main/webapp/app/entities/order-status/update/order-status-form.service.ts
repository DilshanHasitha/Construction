import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOrderStatus, NewOrderStatus } from '../order-status.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrderStatus for edit and NewOrderStatusFormGroupInput for create.
 */
type OrderStatusFormGroupInput = IOrderStatus | PartialWithRequiredKeyOf<NewOrderStatus>;

type OrderStatusFormDefaults = Pick<NewOrderStatus, 'id' | 'isActive'>;

type OrderStatusFormGroupContent = {
  id: FormControl<IOrderStatus['id'] | NewOrderStatus['id']>;
  code: FormControl<IOrderStatus['code']>;
  description: FormControl<IOrderStatus['description']>;
  isActive: FormControl<IOrderStatus['isActive']>;
};

export type OrderStatusFormGroup = FormGroup<OrderStatusFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrderStatusFormService {
  createOrderStatusFormGroup(orderStatus: OrderStatusFormGroupInput = { id: null }): OrderStatusFormGroup {
    const orderStatusRawValue = {
      ...this.getFormDefaults(),
      ...orderStatus,
    };
    return new FormGroup<OrderStatusFormGroupContent>({
      id: new FormControl(
        { value: orderStatusRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(orderStatusRawValue.code, {
        validators: [Validators.required],
      }),
      description: new FormControl(orderStatusRawValue.description, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(orderStatusRawValue.isActive),
    });
  }

  getOrderStatus(form: OrderStatusFormGroup): IOrderStatus | NewOrderStatus {
    return form.getRawValue() as IOrderStatus | NewOrderStatus;
  }

  resetForm(form: OrderStatusFormGroup, orderStatus: OrderStatusFormGroupInput): void {
    const orderStatusRawValue = { ...this.getFormDefaults(), ...orderStatus };
    form.reset(
      {
        ...orderStatusRawValue,
        id: { value: orderStatusRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OrderStatusFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
