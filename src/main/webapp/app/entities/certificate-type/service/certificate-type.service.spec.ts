import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICertificateType } from '../certificate-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../certificate-type.test-samples';

import { CertificateTypeService } from './certificate-type.service';

const requireRestSample: ICertificateType = {
  ...sampleWithRequiredData,
};

describe('CertificateType Service', () => {
  let service: CertificateTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: ICertificateType | ICertificateType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CertificateTypeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a CertificateType', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const certificateType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(certificateType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CertificateType', () => {
      const certificateType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(certificateType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CertificateType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CertificateType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CertificateType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCertificateTypeToCollectionIfMissing', () => {
      it('should add a CertificateType to an empty array', () => {
        const certificateType: ICertificateType = sampleWithRequiredData;
        expectedResult = service.addCertificateTypeToCollectionIfMissing([], certificateType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(certificateType);
      });

      it('should not add a CertificateType to an array that contains it', () => {
        const certificateType: ICertificateType = sampleWithRequiredData;
        const certificateTypeCollection: ICertificateType[] = [
          {
            ...certificateType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCertificateTypeToCollectionIfMissing(certificateTypeCollection, certificateType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CertificateType to an array that doesn't contain it", () => {
        const certificateType: ICertificateType = sampleWithRequiredData;
        const certificateTypeCollection: ICertificateType[] = [sampleWithPartialData];
        expectedResult = service.addCertificateTypeToCollectionIfMissing(certificateTypeCollection, certificateType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(certificateType);
      });

      it('should add only unique CertificateType to an array', () => {
        const certificateTypeArray: ICertificateType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const certificateTypeCollection: ICertificateType[] = [sampleWithRequiredData];
        expectedResult = service.addCertificateTypeToCollectionIfMissing(certificateTypeCollection, ...certificateTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const certificateType: ICertificateType = sampleWithRequiredData;
        const certificateType2: ICertificateType = sampleWithPartialData;
        expectedResult = service.addCertificateTypeToCollectionIfMissing([], certificateType, certificateType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(certificateType);
        expect(expectedResult).toContain(certificateType2);
      });

      it('should accept null and undefined values', () => {
        const certificateType: ICertificateType = sampleWithRequiredData;
        expectedResult = service.addCertificateTypeToCollectionIfMissing([], null, certificateType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(certificateType);
      });

      it('should return initial array if no CertificateType is added', () => {
        const certificateTypeCollection: ICertificateType[] = [sampleWithRequiredData];
        expectedResult = service.addCertificateTypeToCollectionIfMissing(certificateTypeCollection, undefined, null);
        expect(expectedResult).toEqual(certificateTypeCollection);
      });
    });

    describe('compareCertificateType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCertificateType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCertificateType(entity1, entity2);
        const compareResult2 = service.compareCertificateType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCertificateType(entity1, entity2);
        const compareResult2 = service.compareCertificateType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCertificateType(entity1, entity2);
        const compareResult2 = service.compareCertificateType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
