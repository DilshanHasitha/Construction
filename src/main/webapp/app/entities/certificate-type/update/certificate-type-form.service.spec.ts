import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../certificate-type.test-samples';

import { CertificateTypeFormService } from './certificate-type-form.service';

describe('CertificateType Form Service', () => {
  let service: CertificateTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CertificateTypeFormService);
  });

  describe('Service methods', () => {
    describe('createCertificateTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCertificateTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });

      it('passing ICertificateType should create a new form with FormGroup', () => {
        const formGroup = service.createCertificateTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });
    });

    describe('getCertificateType', () => {
      it('should return NewCertificateType for default CertificateType initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCertificateTypeFormGroup(sampleWithNewData);

        const certificateType = service.getCertificateType(formGroup) as any;

        expect(certificateType).toMatchObject(sampleWithNewData);
      });

      it('should return NewCertificateType for empty CertificateType initial value', () => {
        const formGroup = service.createCertificateTypeFormGroup();

        const certificateType = service.getCertificateType(formGroup) as any;

        expect(certificateType).toMatchObject({});
      });

      it('should return ICertificateType', () => {
        const formGroup = service.createCertificateTypeFormGroup(sampleWithRequiredData);

        const certificateType = service.getCertificateType(formGroup) as any;

        expect(certificateType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICertificateType should not enable id FormControl', () => {
        const formGroup = service.createCertificateTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCertificateType should disable id FormControl', () => {
        const formGroup = service.createCertificateTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
