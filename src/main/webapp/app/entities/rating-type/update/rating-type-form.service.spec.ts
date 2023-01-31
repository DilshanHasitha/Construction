import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../rating-type.test-samples';

import { RatingTypeFormService } from './rating-type-form.service';

describe('RatingType Form Service', () => {
  let service: RatingTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RatingTypeFormService);
  });

  describe('Service methods', () => {
    describe('createRatingTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRatingTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          })
        );
      });

      it('passing IRatingType should create a new form with FormGroup', () => {
        const formGroup = service.createRatingTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          })
        );
      });
    });

    describe('getRatingType', () => {
      it('should return NewRatingType for default RatingType initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRatingTypeFormGroup(sampleWithNewData);

        const ratingType = service.getRatingType(formGroup) as any;

        expect(ratingType).toMatchObject(sampleWithNewData);
      });

      it('should return NewRatingType for empty RatingType initial value', () => {
        const formGroup = service.createRatingTypeFormGroup();

        const ratingType = service.getRatingType(formGroup) as any;

        expect(ratingType).toMatchObject({});
      });

      it('should return IRatingType', () => {
        const formGroup = service.createRatingTypeFormGroup(sampleWithRequiredData);

        const ratingType = service.getRatingType(formGroup) as any;

        expect(ratingType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRatingType should not enable id FormControl', () => {
        const formGroup = service.createRatingTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRatingType should disable id FormControl', () => {
        const formGroup = service.createRatingTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
