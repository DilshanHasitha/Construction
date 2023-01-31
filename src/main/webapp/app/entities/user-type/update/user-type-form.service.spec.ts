import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../user-type.test-samples';

import { UserTypeFormService } from './user-type-form.service';

describe('UserType Form Service', () => {
  let service: UserTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserTypeFormService);
  });

  describe('Service methods', () => {
    describe('createUserTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            userRole: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });

      it('passing IUserType should create a new form with FormGroup', () => {
        const formGroup = service.createUserTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            userRole: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });
    });

    describe('getUserType', () => {
      it('should return NewUserType for default UserType initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createUserTypeFormGroup(sampleWithNewData);

        const userType = service.getUserType(formGroup) as any;

        expect(userType).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserType for empty UserType initial value', () => {
        const formGroup = service.createUserTypeFormGroup();

        const userType = service.getUserType(formGroup) as any;

        expect(userType).toMatchObject({});
      });

      it('should return IUserType', () => {
        const formGroup = service.createUserTypeFormGroup(sampleWithRequiredData);

        const userType = service.getUserType(formGroup) as any;

        expect(userType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserType should not enable id FormControl', () => {
        const formGroup = service.createUserTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserType should disable id FormControl', () => {
        const formGroup = service.createUserTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
