import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMasterItem } from '../master-item.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../master-item.test-samples';

import { MasterItemService } from './master-item.service';

const requireRestSample: IMasterItem = {
  ...sampleWithRequiredData,
};

describe('MasterItem Service', () => {
  let service: MasterItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IMasterItem | IMasterItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MasterItemService);
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

    it('should create a MasterItem', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const masterItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(masterItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MasterItem', () => {
      const masterItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(masterItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MasterItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MasterItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MasterItem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMasterItemToCollectionIfMissing', () => {
      it('should add a MasterItem to an empty array', () => {
        const masterItem: IMasterItem = sampleWithRequiredData;
        expectedResult = service.addMasterItemToCollectionIfMissing([], masterItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(masterItem);
      });

      it('should not add a MasterItem to an array that contains it', () => {
        const masterItem: IMasterItem = sampleWithRequiredData;
        const masterItemCollection: IMasterItem[] = [
          {
            ...masterItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMasterItemToCollectionIfMissing(masterItemCollection, masterItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MasterItem to an array that doesn't contain it", () => {
        const masterItem: IMasterItem = sampleWithRequiredData;
        const masterItemCollection: IMasterItem[] = [sampleWithPartialData];
        expectedResult = service.addMasterItemToCollectionIfMissing(masterItemCollection, masterItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(masterItem);
      });

      it('should add only unique MasterItem to an array', () => {
        const masterItemArray: IMasterItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const masterItemCollection: IMasterItem[] = [sampleWithRequiredData];
        expectedResult = service.addMasterItemToCollectionIfMissing(masterItemCollection, ...masterItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const masterItem: IMasterItem = sampleWithRequiredData;
        const masterItem2: IMasterItem = sampleWithPartialData;
        expectedResult = service.addMasterItemToCollectionIfMissing([], masterItem, masterItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(masterItem);
        expect(expectedResult).toContain(masterItem2);
      });

      it('should accept null and undefined values', () => {
        const masterItem: IMasterItem = sampleWithRequiredData;
        expectedResult = service.addMasterItemToCollectionIfMissing([], null, masterItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(masterItem);
      });

      it('should return initial array if no MasterItem is added', () => {
        const masterItemCollection: IMasterItem[] = [sampleWithRequiredData];
        expectedResult = service.addMasterItemToCollectionIfMissing(masterItemCollection, undefined, null);
        expect(expectedResult).toEqual(masterItemCollection);
      });
    });

    describe('compareMasterItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMasterItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMasterItem(entity1, entity2);
        const compareResult2 = service.compareMasterItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMasterItem(entity1, entity2);
        const compareResult2 = service.compareMasterItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMasterItem(entity1, entity2);
        const compareResult2 = service.compareMasterItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
