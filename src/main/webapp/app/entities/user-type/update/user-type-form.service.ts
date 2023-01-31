import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUserType, NewUserType } from '../user-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserType for edit and NewUserTypeFormGroupInput for create.
 */
type UserTypeFormGroupInput = IUserType | PartialWithRequiredKeyOf<NewUserType>;

type UserTypeFormDefaults = Pick<NewUserType, 'id' | 'isActive'>;

type UserTypeFormGroupContent = {
  id: FormControl<IUserType['id'] | NewUserType['id']>;
  code: FormControl<IUserType['code']>;
  userRole: FormControl<IUserType['userRole']>;
  isActive: FormControl<IUserType['isActive']>;
};

export type UserTypeFormGroup = FormGroup<UserTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserTypeFormService {
  createUserTypeFormGroup(userType: UserTypeFormGroupInput = { id: null }): UserTypeFormGroup {
    const userTypeRawValue = {
      ...this.getFormDefaults(),
      ...userType,
    };
    return new FormGroup<UserTypeFormGroupContent>({
      id: new FormControl(
        { value: userTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(userTypeRawValue.code),
      userRole: new FormControl(userTypeRawValue.userRole, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(userTypeRawValue.isActive),
    });
  }

  getUserType(form: UserTypeFormGroup): IUserType | NewUserType {
    return form.getRawValue() as IUserType | NewUserType;
  }

  resetForm(form: UserTypeFormGroup, userType: UserTypeFormGroupInput): void {
    const userTypeRawValue = { ...this.getFormDefaults(), ...userType };
    form.reset(
      {
        ...userTypeRawValue,
        id: { value: userTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): UserTypeFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
