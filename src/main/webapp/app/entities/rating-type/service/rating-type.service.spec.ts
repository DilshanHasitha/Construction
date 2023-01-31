import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRatingType } from '../rating-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../rating-type.test-samples';

import { RatingTypeService } from './rating-type.service';

const requireRestSample: IRatingType = {
  ...sampleWithRequiredData,
};

describe('RatingType Service', () => {
  let service: RatingTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IRatingType | IRatingType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RatingTypeService);
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

    it('should create a RatingType', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const ratingType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ratingType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RatingType', () => {
      const ratingType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ratingType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RatingType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RatingType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RatingType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRatingTypeToCollectionIfMissing', () => {
      it('should add a RatingType to an empty array', () => {
        const ratingType: IRatingType = sampleWithRequiredData;
        expectedResult = service.addRatingTypeToCollectionIfMissing([], ratingType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ratingType);
      });

      it('should not add a RatingType to an array that contains it', () => {
        const ratingType: IRatingType = sampleWithRequiredData;
        const ratingTypeCollection: IRatingType[] = [
          {
            ...ratingType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRatingTypeToCollectionIfMissing(ratingTypeCollection, ratingType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RatingType to an array that doesn't contain it", () => {
        const ratingType: IRatingType = sampleWithRequiredData;
        const ratingTypeCollection: IRatingType[] = [sampleWithPartialData];
        expectedResult = service.addRatingTypeToCollectionIfMissing(ratingTypeCollection, ratingType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ratingType);
      });

      it('should add only unique RatingType to an array', () => {
        const ratingTypeArray: IRatingType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ratingTypeCollection: IRatingType[] = [sampleWithRequiredData];
        expectedResult = service.addRatingTypeToCollectionIfMissing(ratingTypeCollection, ...ratingTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ratingType: IRatingType = sampleWithRequiredData;
        const ratingType2: IRatingType = sampleWithPartialData;
        expectedResult = service.addRatingTypeToCollectionIfMissing([], ratingType, ratingType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ratingType);
        expect(expectedResult).toContain(ratingType2);
      });

      it('should accept null and undefined values', () => {
        const ratingType: IRatingType = sampleWithRequiredData;
        expectedResult = service.addRatingTypeToCollectionIfMissing([], null, ratingType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ratingType);
      });

      it('should return initial array if no RatingType is added', () => {
        const ratingTypeCollection: IRatingType[] = [sampleWithRequiredData];
        expectedResult = service.addRatingTypeToCollectionIfMissing(ratingTypeCollection, undefined, null);
        expect(expectedResult).toEqual(ratingTypeCollection);
      });
    });

    describe('compareRatingType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRatingType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRatingType(entity1, entity2);
        const compareResult2 = service.compareRatingType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRatingType(entity1, entity2);
        const compareResult2 = service.compareRatingType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRatingType(entity1, entity2);
        const compareResult2 = service.compareRatingType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
