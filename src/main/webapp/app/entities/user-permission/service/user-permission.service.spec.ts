import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUserPermission } from '../user-permission.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../user-permission.test-samples';

import { UserPermissionService } from './user-permission.service';

const requireRestSample: IUserPermission = {
  ...sampleWithRequiredData,
};

describe('UserPermission Service', () => {
  let service: UserPermissionService;
  let httpMock: HttpTestingController;
  let expectedResult: IUserPermission | IUserPermission[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UserPermissionService);
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

    it('should create a UserPermission', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const userPermission = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(userPermission).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserPermission', () => {
      const userPermission = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(userPermission).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserPermission', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserPermission', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UserPermission', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUserPermissionToCollectionIfMissing', () => {
      it('should add a UserPermission to an empty array', () => {
        const userPermission: IUserPermission = sampleWithRequiredData;
        expectedResult = service.addUserPermissionToCollectionIfMissing([], userPermission);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userPermission);
      });

      it('should not add a UserPermission to an array that contains it', () => {
        const userPermission: IUserPermission = sampleWithRequiredData;
        const userPermissionCollection: IUserPermission[] = [
          {
            ...userPermission,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUserPermissionToCollectionIfMissing(userPermissionCollection, userPermission);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserPermission to an array that doesn't contain it", () => {
        const userPermission: IUserPermission = sampleWithRequiredData;
        const userPermissionCollection: IUserPermission[] = [sampleWithPartialData];
        expectedResult = service.addUserPermissionToCollectionIfMissing(userPermissionCollection, userPermission);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userPermission);
      });

      it('should add only unique UserPermission to an array', () => {
        const userPermissionArray: IUserPermission[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userPermissionCollection: IUserPermission[] = [sampleWithRequiredData];
        expectedResult = service.addUserPermissionToCollectionIfMissing(userPermissionCollection, ...userPermissionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userPermission: IUserPermission = sampleWithRequiredData;
        const userPermission2: IUserPermission = sampleWithPartialData;
        expectedResult = service.addUserPermissionToCollectionIfMissing([], userPermission, userPermission2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userPermission);
        expect(expectedResult).toContain(userPermission2);
      });

      it('should accept null and undefined values', () => {
        const userPermission: IUserPermission = sampleWithRequiredData;
        expectedResult = service.addUserPermissionToCollectionIfMissing([], null, userPermission, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userPermission);
      });

      it('should return initial array if no UserPermission is added', () => {
        const userPermissionCollection: IUserPermission[] = [sampleWithRequiredData];
        expectedResult = service.addUserPermissionToCollectionIfMissing(userPermissionCollection, undefined, null);
        expect(expectedResult).toEqual(userPermissionCollection);
      });
    });

    describe('compareUserPermission', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUserPermission(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUserPermission(entity1, entity2);
        const compareResult2 = service.compareUserPermission(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUserPermission(entity1, entity2);
        const compareResult2 = service.compareUserPermission(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUserPermission(entity1, entity2);
        const compareResult2 = service.compareUserPermission(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
