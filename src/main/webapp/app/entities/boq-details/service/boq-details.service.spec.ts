import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IBOQDetails } from '../boq-details.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../boq-details.test-samples';

import { BOQDetailsService, RestBOQDetails } from './boq-details.service';

const requireRestSample: RestBOQDetails = {
  ...sampleWithRequiredData,
  orderPlacedOn: sampleWithRequiredData.orderPlacedOn?.format(DATE_FORMAT),
};

describe('BOQDetails Service', () => {
  let service: BOQDetailsService;
  let httpMock: HttpTestingController;
  let expectedResult: IBOQDetails | IBOQDetails[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BOQDetailsService);
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

    it('should create a BOQDetails', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const bOQDetails = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(bOQDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BOQDetails', () => {
      const bOQDetails = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(bOQDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BOQDetails', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BOQDetails', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BOQDetails', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBOQDetailsToCollectionIfMissing', () => {
      it('should add a BOQDetails to an empty array', () => {
        const bOQDetails: IBOQDetails = sampleWithRequiredData;
        expectedResult = service.addBOQDetailsToCollectionIfMissing([], bOQDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bOQDetails);
      });

      it('should not add a BOQDetails to an array that contains it', () => {
        const bOQDetails: IBOQDetails = sampleWithRequiredData;
        const bOQDetailsCollection: IBOQDetails[] = [
          {
            ...bOQDetails,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBOQDetailsToCollectionIfMissing(bOQDetailsCollection, bOQDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BOQDetails to an array that doesn't contain it", () => {
        const bOQDetails: IBOQDetails = sampleWithRequiredData;
        const bOQDetailsCollection: IBOQDetails[] = [sampleWithPartialData];
        expectedResult = service.addBOQDetailsToCollectionIfMissing(bOQDetailsCollection, bOQDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bOQDetails);
      });

      it('should add only unique BOQDetails to an array', () => {
        const bOQDetailsArray: IBOQDetails[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bOQDetailsCollection: IBOQDetails[] = [sampleWithRequiredData];
        expectedResult = service.addBOQDetailsToCollectionIfMissing(bOQDetailsCollection, ...bOQDetailsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bOQDetails: IBOQDetails = sampleWithRequiredData;
        const bOQDetails2: IBOQDetails = sampleWithPartialData;
        expectedResult = service.addBOQDetailsToCollectionIfMissing([], bOQDetails, bOQDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bOQDetails);
        expect(expectedResult).toContain(bOQDetails2);
      });

      it('should accept null and undefined values', () => {
        const bOQDetails: IBOQDetails = sampleWithRequiredData;
        expectedResult = service.addBOQDetailsToCollectionIfMissing([], null, bOQDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bOQDetails);
      });

      it('should return initial array if no BOQDetails is added', () => {
        const bOQDetailsCollection: IBOQDetails[] = [sampleWithRequiredData];
        expectedResult = service.addBOQDetailsToCollectionIfMissing(bOQDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(bOQDetailsCollection);
      });
    });

    describe('compareBOQDetails', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBOQDetails(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBOQDetails(entity1, entity2);
        const compareResult2 = service.compareBOQDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBOQDetails(entity1, entity2);
        const compareResult2 = service.compareBOQDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBOQDetails(entity1, entity2);
        const compareResult2 = service.compareBOQDetails(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
