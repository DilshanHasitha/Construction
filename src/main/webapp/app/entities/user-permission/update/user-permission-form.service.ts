import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUserPermission, NewUserPermission } from '../user-permission.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserPermission for edit and NewUserPermissionFormGroupInput for create.
 */
type UserPermissionFormGroupInput = IUserPermission | PartialWithRequiredKeyOf<NewUserPermission>;

type UserPermissionFormDefaults = Pick<NewUserPermission, 'id' | 'userRoles'>;

type UserPermissionFormGroupContent = {
  id: FormControl<IUserPermission['id'] | NewUserPermission['id']>;
  action: FormControl<IUserPermission['action']>;
  document: FormControl<IUserPermission['document']>;
  description: FormControl<IUserPermission['description']>;
  userRoles: FormControl<IUserPermission['userRoles']>;
};

export type UserPermissionFormGroup = FormGroup<UserPermissionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserPermissionFormService {
  createUserPermissionFormGroup(userPermission: UserPermissionFormGroupInput = { id: null }): UserPermissionFormGroup {
    const userPermissionRawValue = {
      ...this.getFormDefaults(),
      ...userPermission,
    };
    return new FormGroup<UserPermissionFormGroupContent>({
      id: new FormControl(
        { value: userPermissionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      action: new FormControl(userPermissionRawValue.action, {
        validators: [Validators.required],
      }),
      document: new FormControl(userPermissionRawValue.document, {
        validators: [Validators.required],
      }),
      description: new FormControl(userPermissionRawValue.description, {
        validators: [Validators.required],
      }),
      userRoles: new FormControl(userPermissionRawValue.userRoles ?? []),
    });
  }

  getUserPermission(form: UserPermissionFormGroup): IUserPermission | NewUserPermission {
    return form.getRawValue() as IUserPermission | NewUserPermission;
  }

  resetForm(form: UserPermissionFormGroup, userPermission: UserPermissionFormGroupInput): void {
    const userPermissionRawValue = { ...this.getFormDefaults(), ...userPermission };
    form.reset(
      {
        ...userPermissionRawValue,
        id: { value: userPermissionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): UserPermissionFormDefaults {
    return {
      id: null,
      userRoles: [],
    };
  }
}
