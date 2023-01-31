import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ex-user.test-samples';

import { ExUserFormService } from './ex-user-form.service';

describe('ExUser Form Service', () => {
  let service: ExUserFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExUserFormService);
  });

  describe('Service methods', () => {
    describe('createExUserFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExUserFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            login: expect.any(Object),
            userName: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            email: expect.any(Object),
            isActive: expect.any(Object),
            phone: expect.any(Object),
            brNumber: expect.any(Object),
            user: expect.any(Object),
            userRole: expect.any(Object),
            company: expect.any(Object),
          })
        );
      });

      it('passing IExUser should create a new form with FormGroup', () => {
        const formGroup = service.createExUserFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            login: expect.any(Object),
            userName: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            email: expect.any(Object),
            isActive: expect.any(Object),
            phone: expect.any(Object),
            brNumber: expect.any(Object),
            user: expect.any(Object),
            userRole: expect.any(Object),
            company: expect.any(Object),
          })
        );
      });
    });

    describe('getExUser', () => {
      it('should return NewExUser for default ExUser initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createExUserFormGroup(sampleWithNewData);

        const exUser = service.getExUser(formGroup) as any;

        expect(exUser).toMatchObject(sampleWithNewData);
      });

      it('should return NewExUser for empty ExUser initial value', () => {
        const formGroup = service.createExUserFormGroup();

        const exUser = service.getExUser(formGroup) as any;

        expect(exUser).toMatchObject({});
      });

      it('should return IExUser', () => {
        const formGroup = service.createExUserFormGroup(sampleWithRequiredData);

        const exUser = service.getExUser(formGroup) as any;

        expect(exUser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExUser should not enable id FormControl', () => {
        const formGroup = service.createExUserFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExUser should disable id FormControl', () => {
        const formGroup = service.createExUserFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
