import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ExUserFormService } from './ex-user-form.service';
import { ExUserService } from '../service/ex-user.service';
import { IExUser } from '../ex-user.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IUserRole } from 'app/entities/user-role/user-role.model';
import { UserRoleService } from 'app/entities/user-role/service/user-role.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';

import { ExUserUpdateComponent } from './ex-user-update.component';

describe('ExUser Management Update Component', () => {
  let comp: ExUserUpdateComponent;
  let fixture: ComponentFixture<ExUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let exUserFormService: ExUserFormService;
  let exUserService: ExUserService;
  let userService: UserService;
  let userRoleService: UserRoleService;
  let companyService: CompanyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ExUserUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ExUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    exUserFormService = TestBed.inject(ExUserFormService);
    exUserService = TestBed.inject(ExUserService);
    userService = TestBed.inject(UserService);
    userRoleService = TestBed.inject(UserRoleService);
    companyService = TestBed.inject(CompanyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const exUser: IExUser = { id: 456 };
      const user: IUser = { id: 65039 };
      exUser.user = user;

      const userCollection: IUser[] = [{ id: 6790 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ exUser });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UserRole query and add missing value', () => {
      const exUser: IExUser = { id: 456 };
      const userRole: IUserRole = { id: 48272 };
      exUser.userRole = userRole;

      const userRoleCollection: IUserRole[] = [{ id: 56027 }];
      jest.spyOn(userRoleService, 'query').mockReturnValue(of(new HttpResponse({ body: userRoleCollection })));
      const additionalUserRoles = [userRole];
      const expectedCollection: IUserRole[] = [...additionalUserRoles, ...userRoleCollection];
      jest.spyOn(userRoleService, 'addUserRoleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ exUser });
      comp.ngOnInit();

      expect(userRoleService.query).toHaveBeenCalled();
      expect(userRoleService.addUserRoleToCollectionIfMissing).toHaveBeenCalledWith(
        userRoleCollection,
        ...additionalUserRoles.map(expect.objectContaining)
      );
      expect(comp.userRolesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const exUser: IExUser = { id: 456 };
      const company: ICompany = { id: 95998 };
      exUser.company = company;

      const companyCollection: ICompany[] = [{ id: 18651 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ exUser });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining)
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const exUser: IExUser = { id: 456 };
      const user: IUser = { id: 84697 };
      exUser.user = user;
      const userRole: IUserRole = { id: 96743 };
      exUser.userRole = userRole;
      const company: ICompany = { id: 44955 };
      exUser.company = company;

      activatedRoute.data = of({ exUser });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.userRolesSharedCollection).toContain(userRole);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.exUser).toEqual(exUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExUser>>();
      const exUser = { id: 123 };
      jest.spyOn(exUserFormService, 'getExUser').mockReturnValue(exUser);
      jest.spyOn(exUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: exUser }));
      saveSubject.complete();

      // THEN
      expect(exUserFormService.getExUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(exUserService.update).toHaveBeenCalledWith(expect.objectContaining(exUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExUser>>();
      const exUser = { id: 123 };
      jest.spyOn(exUserFormService, 'getExUser').mockReturnValue({ id: null });
      jest.spyOn(exUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: exUser }));
      saveSubject.complete();

      // THEN
      expect(exUserFormService.getExUser).toHaveBeenCalled();
      expect(exUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExUser>>();
      const exUser = { id: 123 };
      jest.spyOn(exUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(exUserService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUserRole', () => {
      it('Should forward to userRoleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userRoleService, 'compareUserRole');
        comp.compareUserRole(entity, entity2);
        expect(userRoleService.compareUserRole).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCompany', () => {
      it('Should forward to companyService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(companyService, 'compareCompany');
        comp.compareCompany(entity, entity2);
        expect(companyService.compareCompany).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
