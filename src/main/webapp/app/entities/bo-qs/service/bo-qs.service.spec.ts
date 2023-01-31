import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBOQs } from '../bo-qs.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../bo-qs.test-samples';

import { BOQsService } from './bo-qs.service';

const requireRestSample: IBOQs = {
  ...sampleWithRequiredData,
};

describe('BOQs Service', () => {
  let service: BOQsService;
  let httpMock: HttpTestingController;
  let expectedResult: IBOQs | IBOQs[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BOQsService);
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

    it('should create a BOQs', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const bOQs = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(bOQs).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BOQs', () => {
      const bOQs = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(bOQs).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BOQs', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BOQs', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BOQs', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBOQsToCollectionIfMissing', () => {
      it('should add a BOQs to an empty array', () => {
        const bOQs: IBOQs = sampleWithRequiredData;
        expectedResult = service.addBOQsToCollectionIfMissing([], bOQs);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bOQs);
      });

      it('should not add a BOQs to an array that contains it', () => {
        const bOQs: IBOQs = sampleWithRequiredData;
        const bOQsCollection: IBOQs[] = [
          {
            ...bOQs,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBOQsToCollectionIfMissing(bOQsCollection, bOQs);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BOQs to an array that doesn't contain it", () => {
        const bOQs: IBOQs = sampleWithRequiredData;
        const bOQsCollection: IBOQs[] = [sampleWithPartialData];
        expectedResult = service.addBOQsToCollectionIfMissing(bOQsCollection, bOQs);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bOQs);
      });

      it('should add only unique BOQs to an array', () => {
        const bOQsArray: IBOQs[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bOQsCollection: IBOQs[] = [sampleWithRequiredData];
        expectedResult = service.addBOQsToCollectionIfMissing(bOQsCollection, ...bOQsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bOQs: IBOQs = sampleWithRequiredData;
        const bOQs2: IBOQs = sampleWithPartialData;
        expectedResult = service.addBOQsToCollectionIfMissing([], bOQs, bOQs2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bOQs);
        expect(expectedResult).toContain(bOQs2);
      });

      it('should accept null and undefined values', () => {
        const bOQs: IBOQs = sampleWithRequiredData;
        expectedResult = service.addBOQsToCollectionIfMissing([], null, bOQs, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bOQs);
      });

      it('should return initial array if no BOQs is added', () => {
        const bOQsCollection: IBOQs[] = [sampleWithRequiredData];
        expectedResult = service.addBOQsToCollectionIfMissing(bOQsCollection, undefined, null);
        expect(expectedResult).toEqual(bOQsCollection);
      });
    });

    describe('compareBOQs', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBOQs(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBOQs(entity1, entity2);
        const compareResult2 = service.compareBOQs(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBOQs(entity1, entity2);
        const compareResult2 = service.compareBOQs(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBOQs(entity1, entity2);
        const compareResult2 = service.compareBOQs(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
