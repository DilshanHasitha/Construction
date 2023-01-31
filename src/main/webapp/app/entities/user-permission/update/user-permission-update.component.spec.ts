import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UserPermissionFormService } from './user-permission-form.service';
import { UserPermissionService } from '../service/user-permission.service';
import { IUserPermission } from '../user-permission.model';

import { UserPermissionUpdateComponent } from './user-permission-update.component';

describe('UserPermission Management Update Component', () => {
  let comp: UserPermissionUpdateComponent;
  let fixture: ComponentFixture<UserPermissionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userPermissionFormService: UserPermissionFormService;
  let userPermissionService: UserPermissionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UserPermissionUpdateComponent],
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
      .overrideTemplate(UserPermissionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserPermissionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userPermissionFormService = TestBed.inject(UserPermissionFormService);
    userPermissionService = TestBed.inject(UserPermissionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const userPermission: IUserPermission = { id: 456 };

      activatedRoute.data = of({ userPermission });
      comp.ngOnInit();

      expect(comp.userPermission).toEqual(userPermission);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserPermission>>();
      const userPermission = { id: 123 };
      jest.spyOn(userPermissionFormService, 'getUserPermission').mockReturnValue(userPermission);
      jest.spyOn(userPermissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userPermission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userPermission }));
      saveSubject.complete();

      // THEN
      expect(userPermissionFormService.getUserPermission).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userPermissionService.update).toHaveBeenCalledWith(expect.objectContaining(userPermission));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserPermission>>();
      const userPermission = { id: 123 };
      jest.spyOn(userPermissionFormService, 'getUserPermission').mockReturnValue({ id: null });
      jest.spyOn(userPermissionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userPermission: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userPermission }));
      saveSubject.complete();

      // THEN
      expect(userPermissionFormService.getUserPermission).toHaveBeenCalled();
      expect(userPermissionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserPermission>>();
      const userPermission = { id: 123 };
      jest.spyOn(userPermissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userPermission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userPermissionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
