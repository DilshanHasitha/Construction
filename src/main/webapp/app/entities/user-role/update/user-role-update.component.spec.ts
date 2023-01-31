import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UserRoleFormService } from './user-role-form.service';
import { UserRoleService } from '../service/user-role.service';
import { IUserRole } from '../user-role.model';
import { IUserPermission } from 'app/entities/user-permission/user-permission.model';
import { UserPermissionService } from 'app/entities/user-permission/service/user-permission.service';

import { UserRoleUpdateComponent } from './user-role-update.component';

describe('UserRole Management Update Component', () => {
  let comp: UserRoleUpdateComponent;
  let fixture: ComponentFixture<UserRoleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userRoleFormService: UserRoleFormService;
  let userRoleService: UserRoleService;
  let userPermissionService: UserPermissionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UserRoleUpdateComponent],
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
      .overrideTemplate(UserRoleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserRoleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userRoleFormService = TestBed.inject(UserRoleFormService);
    userRoleService = TestBed.inject(UserRoleService);
    userPermissionService = TestBed.inject(UserPermissionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call UserPermission query and add missing value', () => {
      const userRole: IUserRole = { id: 456 };
      const userPermissions: IUserPermission[] = [{ id: 66028 }];
      userRole.userPermissions = userPermissions;

      const userPermissionCollection: IUserPermission[] = [{ id: 90315 }];
      jest.spyOn(userPermissionService, 'query').mockReturnValue(of(new HttpResponse({ body: userPermissionCollection })));
      const additionalUserPermissions = [...userPermissions];
      const expectedCollection: IUserPermission[] = [...additionalUserPermissions, ...userPermissionCollection];
      jest.spyOn(userPermissionService, 'addUserPermissionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userRole });
      comp.ngOnInit();

      expect(userPermissionService.query).toHaveBeenCalled();
      expect(userPermissionService.addUserPermissionToCollectionIfMissing).toHaveBeenCalledWith(
        userPermissionCollection,
        ...additionalUserPermissions.map(expect.objectContaining)
      );
      expect(comp.userPermissionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userRole: IUserRole = { id: 456 };
      const userPermission: IUserPermission = { id: 61816 };
      userRole.userPermissions = [userPermission];

      activatedRoute.data = of({ userRole });
      comp.ngOnInit();

      expect(comp.userPermissionsSharedCollection).toContain(userPermission);
      expect(comp.userRole).toEqual(userRole);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserRole>>();
      const userRole = { id: 123 };
      jest.spyOn(userRoleFormService, 'getUserRole').mockReturnValue(userRole);
      jest.spyOn(userRoleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userRole }));
      saveSubject.complete();

      // THEN
      expect(userRoleFormService.getUserRole).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userRoleService.update).toHaveBeenCalledWith(expect.objectContaining(userRole));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserRole>>();
      const userRole = { id: 123 };
      jest.spyOn(userRoleFormService, 'getUserRole').mockReturnValue({ id: null });
      jest.spyOn(userRoleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userRole: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userRole }));
      saveSubject.complete();

      // THEN
      expect(userRoleFormService.getUserRole).toHaveBeenCalled();
      expect(userRoleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserRole>>();
      const userRole = { id: 123 };
      jest.spyOn(userRoleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userRoleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUserPermission', () => {
      it('Should forward to userPermissionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userPermissionService, 'compareUserPermission');
        comp.compareUserPermission(entity, entity2);
        expect(userPermissionService.compareUserPermission).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
