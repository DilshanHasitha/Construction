import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../unit-of-measure.test-samples';

import { UnitOfMeasureFormService } from './unit-of-measure-form.service';

describe('UnitOfMeasure Form Service', () => {
  let service: UnitOfMeasureFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UnitOfMeasureFormService);
  });

  describe('Service methods', () => {
    describe('createUnitOfMeasureFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUnitOfMeasureFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            unitOfMeasureCode: expect.any(Object),
            unitOfMeasureDescription: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });

      it('passing IUnitOfMeasure should create a new form with FormGroup', () => {
        const formGroup = service.createUnitOfMeasureFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            unitOfMeasureCode: expect.any(Object),
            unitOfMeasureDescription: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });
    });

    describe('getUnitOfMeasure', () => {
      it('should return NewUnitOfMeasure for default UnitOfMeasure initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createUnitOfMeasureFormGroup(sampleWithNewData);

        const unitOfMeasure = service.getUnitOfMeasure(formGroup) as any;

        expect(unitOfMeasure).toMatchObject(sampleWithNewData);
      });

      it('should return NewUnitOfMeasure for empty UnitOfMeasure initial value', () => {
        const formGroup = service.createUnitOfMeasureFormGroup();

        const unitOfMeasure = service.getUnitOfMeasure(formGroup) as any;

        expect(unitOfMeasure).toMatchObject({});
      });

      it('should return IUnitOfMeasure', () => {
        const formGroup = service.createUnitOfMeasureFormGroup(sampleWithRequiredData);

        const unitOfMeasure = service.getUnitOfMeasure(formGroup) as any;

        expect(unitOfMeasure).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUnitOfMeasure should not enable id FormControl', () => {
        const formGroup = service.createUnitOfMeasureFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUnitOfMeasure should disable id FormControl', () => {
        const formGroup = service.createUnitOfMeasureFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
