import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../master-item.test-samples';

import { MasterItemFormService } from './master-item-form.service';

describe('MasterItem Form Service', () => {
  let service: MasterItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MasterItemFormService);
  });

  describe('Service methods', () => {
    describe('createMasterItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMasterItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
            exUser: expect.any(Object),
          })
        );
      });

      it('passing IMasterItem should create a new form with FormGroup', () => {
        const formGroup = service.createMasterItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
            exUser: expect.any(Object),
          })
        );
      });
    });

    describe('getMasterItem', () => {
      it('should return NewMasterItem for default MasterItem initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMasterItemFormGroup(sampleWithNewData);

        const masterItem = service.getMasterItem(formGroup) as any;

        expect(masterItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewMasterItem for empty MasterItem initial value', () => {
        const formGroup = service.createMasterItemFormGroup();

        const masterItem = service.getMasterItem(formGroup) as any;

        expect(masterItem).toMatchObject({});
      });

      it('should return IMasterItem', () => {
        const formGroup = service.createMasterItemFormGroup(sampleWithRequiredData);

        const masterItem = service.getMasterItem(formGroup) as any;

        expect(masterItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMasterItem should not enable id FormControl', () => {
        const formGroup = service.createMasterItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMasterItem should disable id FormControl', () => {
        const formGroup = service.createMasterItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
