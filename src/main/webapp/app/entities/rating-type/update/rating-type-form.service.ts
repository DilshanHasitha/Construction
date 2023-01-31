import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRatingType, NewRatingType } from '../rating-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRatingType for edit and NewRatingTypeFormGroupInput for create.
 */
type RatingTypeFormGroupInput = IRatingType | PartialWithRequiredKeyOf<NewRatingType>;

type RatingTypeFormDefaults = Pick<NewRatingType, 'id'>;

type RatingTypeFormGroupContent = {
  id: FormControl<IRatingType['id'] | NewRatingType['id']>;
  name: FormControl<IRatingType['name']>;
  description: FormControl<IRatingType['description']>;
};

export type RatingTypeFormGroup = FormGroup<RatingTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RatingTypeFormService {
  createRatingTypeFormGroup(ratingType: RatingTypeFormGroupInput = { id: null }): RatingTypeFormGroup {
    const ratingTypeRawValue = {
      ...this.getFormDefaults(),
      ...ratingType,
    };
    return new FormGroup<RatingTypeFormGroupContent>({
      id: new FormControl(
        { value: ratingTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(ratingTypeRawValue.name),
      description: new FormControl(ratingTypeRawValue.description),
    });
  }

  getRatingType(form: RatingTypeFormGroup): IRatingType | NewRatingType {
    return form.getRawValue() as IRatingType | NewRatingType;
  }

  resetForm(form: RatingTypeFormGroup, ratingType: RatingTypeFormGroupInput): void {
    const ratingTypeRawValue = { ...this.getFormDefaults(), ...ratingType };
    form.reset(
      {
        ...ratingTypeRawValue,
        id: { value: ratingTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RatingTypeFormDefaults {
    return {
      id: null,
    };
  }
}
