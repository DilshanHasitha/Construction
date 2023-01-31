import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IExUser } from '../ex-user.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../ex-user.test-samples';

import { ExUserService } from './ex-user.service';

const requireRestSample: IExUser = {
  ...sampleWithRequiredData,
};

describe('ExUser Service', () => {
  let service: ExUserService;
  let httpMock: HttpTestingController;
  let expectedResult: IExUser | IExUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ExUserService);
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

    it('should create a ExUser', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const exUser = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(exUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExUser', () => {
      const exUser = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(exUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExUser', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExUser', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ExUser', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addExUserToCollectionIfMissing', () => {
      it('should add a ExUser to an empty array', () => {
        const exUser: IExUser = sampleWithRequiredData;
        expectedResult = service.addExUserToCollectionIfMissing([], exUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(exUser);
      });

      it('should not add a ExUser to an array that contains it', () => {
        const exUser: IExUser = sampleWithRequiredData;
        const exUserCollection: IExUser[] = [
          {
            ...exUser,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExUserToCollectionIfMissing(exUserCollection, exUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExUser to an array that doesn't contain it", () => {
        const exUser: IExUser = sampleWithRequiredData;
        const exUserCollection: IExUser[] = [sampleWithPartialData];
        expectedResult = service.addExUserToCollectionIfMissing(exUserCollection, exUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(exUser);
      });

      it('should add only unique ExUser to an array', () => {
        const exUserArray: IExUser[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const exUserCollection: IExUser[] = [sampleWithRequiredData];
        expectedResult = service.addExUserToCollectionIfMissing(exUserCollection, ...exUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const exUser: IExUser = sampleWithRequiredData;
        const exUser2: IExUser = sampleWithPartialData;
        expectedResult = service.addExUserToCollectionIfMissing([], exUser, exUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(exUser);
        expect(expectedResult).toContain(exUser2);
      });

      it('should accept null and undefined values', () => {
        const exUser: IExUser = sampleWithRequiredData;
        expectedResult = service.addExUserToCollectionIfMissing([], null, exUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(exUser);
      });

      it('should return initial array if no ExUser is added', () => {
        const exUserCollection: IExUser[] = [sampleWithRequiredData];
        expectedResult = service.addExUserToCollectionIfMissing(exUserCollection, undefined, null);
        expect(expectedResult).toEqual(exUserCollection);
      });
    });

    describe('compareExUser', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExUser(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareExUser(entity1, entity2);
        const compareResult2 = service.compareExUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareExUser(entity1, entity2);
        const compareResult2 = service.compareExUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareExUser(entity1, entity2);
        const compareResult2 = service.compareExUser(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
