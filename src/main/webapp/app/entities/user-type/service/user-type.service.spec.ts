import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUserType } from '../user-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../user-type.test-samples';

import { UserTypeService } from './user-type.service';

const requireRestSample: IUserType = {
  ...sampleWithRequiredData,
};

describe('UserType Service', () => {
  let service: UserTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IUserType | IUserType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UserTypeService);
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

    it('should create a UserType', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const userType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(userType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserType', () => {
      const userType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(userType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UserType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUserTypeToCollectionIfMissing', () => {
      it('should add a UserType to an empty array', () => {
        const userType: IUserType = sampleWithRequiredData;
        expectedResult = service.addUserTypeToCollectionIfMissing([], userType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userType);
      });

      it('should not add a UserType to an array that contains it', () => {
        const userType: IUserType = sampleWithRequiredData;
        const userTypeCollection: IUserType[] = [
          {
            ...userType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUserTypeToCollectionIfMissing(userTypeCollection, userType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserType to an array that doesn't contain it", () => {
        const userType: IUserType = sampleWithRequiredData;
        const userTypeCollection: IUserType[] = [sampleWithPartialData];
        expectedResult = service.addUserTypeToCollectionIfMissing(userTypeCollection, userType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userType);
      });

      it('should add only unique UserType to an array', () => {
        const userTypeArray: IUserType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userTypeCollection: IUserType[] = [sampleWithRequiredData];
        expectedResult = service.addUserTypeToCollectionIfMissing(userTypeCollection, ...userTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userType: IUserType = sampleWithRequiredData;
        const userType2: IUserType = sampleWithPartialData;
        expectedResult = service.addUserTypeToCollectionIfMissing([], userType, userType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userType);
        expect(expectedResult).toContain(userType2);
      });

      it('should accept null and undefined values', () => {
        const userType: IUserType = sampleWithRequiredData;
        expectedResult = service.addUserTypeToCollectionIfMissing([], null, userType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userType);
      });

      it('should return initial array if no UserType is added', () => {
        const userTypeCollection: IUserType[] = [sampleWithRequiredData];
        expectedResult = service.addUserTypeToCollectionIfMissing(userTypeCollection, undefined, null);
        expect(expectedResult).toEqual(userTypeCollection);
      });
    });

    describe('compareUserType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUserType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUserType(entity1, entity2);
        const compareResult2 = service.compareUserType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUserType(entity1, entity2);
        const compareResult2 = service.compareUserType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUserType(entity1, entity2);
        const compareResult2 = service.compareUserType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
