import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../bo-qs.test-samples';

import { BOQsFormService } from './bo-qs-form.service';

describe('BOQs Form Service', () => {
  let service: BOQsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BOQsFormService);
  });

  describe('Service methods', () => {
    describe('createBOQsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBOQsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            isActive: expect.any(Object),
            constructors: expect.any(Object),
            boqDetails: expect.any(Object),
          })
        );
      });

      it('passing IBOQs should create a new form with FormGroup', () => {
        const formGroup = service.createBOQsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            isActive: expect.any(Object),
            constructors: expect.any(Object),
            boqDetails: expect.any(Object),
          })
        );
      });
    });

    describe('getBOQs', () => {
      it('should return NewBOQs for default BOQs initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBOQsFormGroup(sampleWithNewData);

        const bOQs = service.getBOQs(formGroup) as any;

        expect(bOQs).toMatchObject(sampleWithNewData);
      });

      it('should return NewBOQs for empty BOQs initial value', () => {
        const formGroup = service.createBOQsFormGroup();

        const bOQs = service.getBOQs(formGroup) as any;

        expect(bOQs).toMatchObject({});
      });

      it('should return IBOQs', () => {
        const formGroup = service.createBOQsFormGroup(sampleWithRequiredData);

        const bOQs = service.getBOQs(formGroup) as any;

        expect(bOQs).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBOQs should not enable id FormControl', () => {
        const formGroup = service.createBOQsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBOQs should disable id FormControl', () => {
        const formGroup = service.createBOQsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
