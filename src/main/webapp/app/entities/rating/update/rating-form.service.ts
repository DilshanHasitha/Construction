import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRating, NewRating } from '../rating.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRating for edit and NewRatingFormGroupInput for create.
 */
type RatingFormGroupInput = IRating | PartialWithRequiredKeyOf<NewRating>;

type RatingFormDefaults = Pick<NewRating, 'id' | 'exUsers'>;

type RatingFormGroupContent = {
  id: FormControl<IRating['id'] | NewRating['id']>;
  name: FormControl<IRating['name']>;
  rateValue: FormControl<IRating['rateValue']>;
  ratingType: FormControl<IRating['ratingType']>;
  exUsers: FormControl<IRating['exUsers']>;
};

export type RatingFormGroup = FormGroup<RatingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RatingFormService {
  createRatingFormGroup(rating: RatingFormGroupInput = { id: null }): RatingFormGroup {
    const ratingRawValue = {
      ...this.getFormDefaults(),
      ...rating,
    };
    return new FormGroup<RatingFormGroupContent>({
      id: new FormControl(
        { value: ratingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(ratingRawValue.name),
      rateValue: new FormControl(ratingRawValue.rateValue),
      ratingType: new FormControl(ratingRawValue.ratingType),
      exUsers: new FormControl(ratingRawValue.exUsers ?? []),
    });
  }

  getRating(form: RatingFormGroup): IRating | NewRating {
    return form.getRawValue() as IRating | NewRating;
  }

  resetForm(form: RatingFormGroup, rating: RatingFormGroupInput): void {
    const ratingRawValue = { ...this.getFormDefaults(), ...rating };
    form.reset(
      {
        ...ratingRawValue,
        id: { value: ratingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RatingFormDefaults {
    return {
      id: null,
      exUsers: [],
    };
  }
}
