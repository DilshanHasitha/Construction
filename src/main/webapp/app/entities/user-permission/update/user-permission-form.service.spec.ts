import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../user-permission.test-samples';

import { UserPermissionFormService } from './user-permission-form.service';

describe('UserPermission Form Service', () => {
  let service: UserPermissionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserPermissionFormService);
  });

  describe('Service methods', () => {
    describe('createUserPermissionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserPermissionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            action: expect.any(Object),
            document: expect.any(Object),
            description: expect.any(Object),
            userRoles: expect.any(Object),
          })
        );
      });

      it('passing IUserPermission should create a new form with FormGroup', () => {
        const formGroup = service.createUserPermissionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            action: expect.any(Object),
            document: expect.any(Object),
            description: expect.any(Object),
            userRoles: expect.any(Object),
          })
        );
      });
    });

    describe('getUserPermission', () => {
      it('should return NewUserPermission for default UserPermission initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createUserPermissionFormGroup(sampleWithNewData);

        const userPermission = service.getUserPermission(formGroup) as any;

        expect(userPermission).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserPermission for empty UserPermission initial value', () => {
        const formGroup = service.createUserPermissionFormGroup();

        const userPermission = service.getUserPermission(formGroup) as any;

        expect(userPermission).toMatchObject({});
      });

      it('should return IUserPermission', () => {
        const formGroup = service.createUserPermissionFormGroup(sampleWithRequiredData);

        const userPermission = service.getUserPermission(formGroup) as any;

        expect(userPermission).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserPermission should not enable id FormControl', () => {
        const formGroup = service.createUserPermissionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserPermission should disable id FormControl', () => {
        const formGroup = service.createUserPermissionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
