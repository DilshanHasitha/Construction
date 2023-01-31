import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../boq-details.test-samples';

import { BOQDetailsFormService } from './boq-details-form.service';

describe('BOQDetails Form Service', () => {
  let service: BOQDetailsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BOQDetailsFormService);
  });

  describe('Service methods', () => {
    describe('createBOQDetailsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBOQDetailsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            orderPlacedOn: expect.any(Object),
            qty: expect.any(Object),
            isActive: expect.any(Object),
            item: expect.any(Object),
            per: expect.any(Object),
            unit: expect.any(Object),
            boqs: expect.any(Object),
          })
        );
      });

      it('passing IBOQDetails should create a new form with FormGroup', () => {
        const formGroup = service.createBOQDetailsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            orderPlacedOn: expect.any(Object),
            qty: expect.any(Object),
            isActive: expect.any(Object),
            item: expect.any(Object),
            per: expect.any(Object),
            unit: expect.any(Object),
            boqs: expect.any(Object),
          })
        );
      });
    });

    describe('getBOQDetails', () => {
      it('should return NewBOQDetails for default BOQDetails initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBOQDetailsFormGroup(sampleWithNewData);

        const bOQDetails = service.getBOQDetails(formGroup) as any;

        expect(bOQDetails).toMatchObject(sampleWithNewData);
      });

      it('should return NewBOQDetails for empty BOQDetails initial value', () => {
        const formGroup = service.createBOQDetailsFormGroup();

        const bOQDetails = service.getBOQDetails(formGroup) as any;

        expect(bOQDetails).toMatchObject({});
      });

      it('should return IBOQDetails', () => {
        const formGroup = service.createBOQDetailsFormGroup(sampleWithRequiredData);

        const bOQDetails = service.getBOQDetails(formGroup) as any;

        expect(bOQDetails).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBOQDetails should not enable id FormControl', () => {
        const formGroup = service.createBOQDetailsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBOQDetails should disable id FormControl', () => {
        const formGroup = service.createBOQDetailsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
