import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IExUser, NewExUser } from '../ex-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExUser for edit and NewExUserFormGroupInput for create.
 */
type ExUserFormGroupInput = IExUser | PartialWithRequiredKeyOf<NewExUser>;

type ExUserFormDefaults = Pick<NewExUser, 'id' | 'isActive'>;

type ExUserFormGroupContent = {
  id: FormControl<IExUser['id'] | NewExUser['id']>;
  login: FormControl<IExUser['login']>;
  userName: FormControl<IExUser['userName']>;
  firstName: FormControl<IExUser['firstName']>;
  lastName: FormControl<IExUser['lastName']>;
  email: FormControl<IExUser['email']>;
  isActive: FormControl<IExUser['isActive']>;
  phone: FormControl<IExUser['phone']>;
  brNumber: FormControl<IExUser['brNumber']>;
  user: FormControl<IExUser['user']>;
  userRole: FormControl<IExUser['userRole']>;
  company: FormControl<IExUser['company']>;
};

export type ExUserFormGroup = FormGroup<ExUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExUserFormService {
  createExUserFormGroup(exUser: ExUserFormGroupInput = { id: null }): ExUserFormGroup {
    const exUserRawValue = {
      ...this.getFormDefaults(),
      ...exUser,
    };
    return new FormGroup<ExUserFormGroupContent>({
      id: new FormControl(
        { value: exUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      login: new FormControl(exUserRawValue.login, {
        validators: [Validators.required],
      }),
      userName: new FormControl(exUserRawValue.userName, {
        validators: [Validators.required],
      }),
      firstName: new FormControl(exUserRawValue.firstName),
      lastName: new FormControl(exUserRawValue.lastName),
      email: new FormControl(exUserRawValue.email, {
        validators: [Validators.required, Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')],
      }),
      isActive: new FormControl(exUserRawValue.isActive),
      phone: new FormControl(exUserRawValue.phone),
      brNumber: new FormControl(exUserRawValue.brNumber),
      user: new FormControl(exUserRawValue.user),
      userRole: new FormControl(exUserRawValue.userRole),
      company: new FormControl(exUserRawValue.company),
    });
  }

  getExUser(form: ExUserFormGroup): IExUser | NewExUser {
    return form.getRawValue() as IExUser | NewExUser;
  }

  resetForm(form: ExUserFormGroup, exUser: ExUserFormGroupInput): void {
    const exUserRawValue = { ...this.getFormDefaults(), ...exUser };
    form.reset(
      {
        ...exUserRawValue,
        id: { value: exUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ExUserFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
