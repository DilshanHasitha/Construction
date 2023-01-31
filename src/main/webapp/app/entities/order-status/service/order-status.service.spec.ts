import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrderStatus } from '../order-status.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../order-status.test-samples';

import { OrderStatusService } from './order-status.service';

const requireRestSample: IOrderStatus = {
  ...sampleWithRequiredData,
};

describe('OrderStatus Service', () => {
  let service: OrderStatusService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrderStatus | IOrderStatus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrderStatusService);
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

    it('should create a OrderStatus', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const orderStatus = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(orderStatus).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrderStatus', () => {
      const orderStatus = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(orderStatus).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrderStatus', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrderStatus', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrderStatus', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOrderStatusToCollectionIfMissing', () => {
      it('should add a OrderStatus to an empty array', () => {
        const orderStatus: IOrderStatus = sampleWithRequiredData;
        expectedResult = service.addOrderStatusToCollectionIfMissing([], orderStatus);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orderStatus);
      });

      it('should not add a OrderStatus to an array that contains it', () => {
        const orderStatus: IOrderStatus = sampleWithRequiredData;
        const orderStatusCollection: IOrderStatus[] = [
          {
            ...orderStatus,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrderStatusToCollectionIfMissing(orderStatusCollection, orderStatus);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrderStatus to an array that doesn't contain it", () => {
        const orderStatus: IOrderStatus = sampleWithRequiredData;
        const orderStatusCollection: IOrderStatus[] = [sampleWithPartialData];
        expectedResult = service.addOrderStatusToCollectionIfMissing(orderStatusCollection, orderStatus);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderStatus);
      });

      it('should add only unique OrderStatus to an array', () => {
        const orderStatusArray: IOrderStatus[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const orderStatusCollection: IOrderStatus[] = [sampleWithRequiredData];
        expectedResult = service.addOrderStatusToCollectionIfMissing(orderStatusCollection, ...orderStatusArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orderStatus: IOrderStatus = sampleWithRequiredData;
        const orderStatus2: IOrderStatus = sampleWithPartialData;
        expectedResult = service.addOrderStatusToCollectionIfMissing([], orderStatus, orderStatus2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderStatus);
        expect(expectedResult).toContain(orderStatus2);
      });

      it('should accept null and undefined values', () => {
        const orderStatus: IOrderStatus = sampleWithRequiredData;
        expectedResult = service.addOrderStatusToCollectionIfMissing([], null, orderStatus, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orderStatus);
      });

      it('should return initial array if no OrderStatus is added', () => {
        const orderStatusCollection: IOrderStatus[] = [sampleWithRequiredData];
        expectedResult = service.addOrderStatusToCollectionIfMissing(orderStatusCollection, undefined, null);
        expect(expectedResult).toEqual(orderStatusCollection);
      });
    });

    describe('compareOrderStatus', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrderStatus(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOrderStatus(entity1, entity2);
        const compareResult2 = service.compareOrderStatus(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOrderStatus(entity1, entity2);
        const compareResult2 = service.compareOrderStatus(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOrderStatus(entity1, entity2);
        const compareResult2 = service.compareOrderStatus(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
