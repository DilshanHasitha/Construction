import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUnitOfMeasure } from '../unit-of-measure.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../unit-of-measure.test-samples';

import { UnitOfMeasureService } from './unit-of-measure.service';

const requireRestSample: IUnitOfMeasure = {
  ...sampleWithRequiredData,
};

describe('UnitOfMeasure Service', () => {
  let service: UnitOfMeasureService;
  let httpMock: HttpTestingController;
  let expectedResult: IUnitOfMeasure | IUnitOfMeasure[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UnitOfMeasureService);
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

    it('should create a UnitOfMeasure', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const unitOfMeasure = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(unitOfMeasure).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UnitOfMeasure', () => {
      const unitOfMeasure = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(unitOfMeasure).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UnitOfMeasure', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UnitOfMeasure', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UnitOfMeasure', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUnitOfMeasureToCollectionIfMissing', () => {
      it('should add a UnitOfMeasure to an empty array', () => {
        const unitOfMeasure: IUnitOfMeasure = sampleWithRequiredData;
        expectedResult = service.addUnitOfMeasureToCollectionIfMissing([], unitOfMeasure);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(unitOfMeasure);
      });

      it('should not add a UnitOfMeasure to an array that contains it', () => {
        const unitOfMeasure: IUnitOfMeasure = sampleWithRequiredData;
        const unitOfMeasureCollection: IUnitOfMeasure[] = [
          {
            ...unitOfMeasure,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUnitOfMeasureToCollectionIfMissing(unitOfMeasureCollection, unitOfMeasure);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UnitOfMeasure to an array that doesn't contain it", () => {
        const unitOfMeasure: IUnitOfMeasure = sampleWithRequiredData;
        const unitOfMeasureCollection: IUnitOfMeasure[] = [sampleWithPartialData];
        expectedResult = service.addUnitOfMeasureToCollectionIfMissing(unitOfMeasureCollection, unitOfMeasure);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(unitOfMeasure);
      });

      it('should add only unique UnitOfMeasure to an array', () => {
        const unitOfMeasureArray: IUnitOfMeasure[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const unitOfMeasureCollection: IUnitOfMeasure[] = [sampleWithRequiredData];
        expectedResult = service.addUnitOfMeasureToCollectionIfMissing(unitOfMeasureCollection, ...unitOfMeasureArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const unitOfMeasure: IUnitOfMeasure = sampleWithRequiredData;
        const unitOfMeasure2: IUnitOfMeasure = sampleWithPartialData;
        expectedResult = service.addUnitOfMeasureToCollectionIfMissing([], unitOfMeasure, unitOfMeasure2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(unitOfMeasure);
        expect(expectedResult).toContain(unitOfMeasure2);
      });

      it('should accept null and undefined values', () => {
        const unitOfMeasure: IUnitOfMeasure = sampleWithRequiredData;
        expectedResult = service.addUnitOfMeasureToCollectionIfMissing([], null, unitOfMeasure, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(unitOfMeasure);
      });

      it('should return initial array if no UnitOfMeasure is added', () => {
        const unitOfMeasureCollection: IUnitOfMeasure[] = [sampleWithRequiredData];
        expectedResult = service.addUnitOfMeasureToCollectionIfMissing(unitOfMeasureCollection, undefined, null);
        expect(expectedResult).toEqual(unitOfMeasureCollection);
      });
    });

    describe('compareUnitOfMeasure', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUnitOfMeasure(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUnitOfMeasure(entity1, entity2);
        const compareResult2 = service.compareUnitOfMeasure(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUnitOfMeasure(entity1, entity2);
        const compareResult2 = service.compareUnitOfMeasure(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUnitOfMeasure(entity1, entity2);
        const compareResult2 = service.compareUnitOfMeasure(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
