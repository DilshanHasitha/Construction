import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UserTypeFormService } from './user-type-form.service';
import { UserTypeService } from '../service/user-type.service';
import { IUserType } from '../user-type.model';

import { UserTypeUpdateComponent } from './user-type-update.component';

describe('UserType Management Update Component', () => {
  let comp: UserTypeUpdateComponent;
  let fixture: ComponentFixture<UserTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userTypeFormService: UserTypeFormService;
  let userTypeService: UserTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UserTypeUpdateComponent],
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
      .overrideTemplate(UserTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userTypeFormService = TestBed.inject(UserTypeFormService);
    userTypeService = TestBed.inject(UserTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const userType: IUserType = { id: 456 };

      activatedRoute.data = of({ userType });
      comp.ngOnInit();

      expect(comp.userType).toEqual(userType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserType>>();
      const userType = { id: 123 };
      jest.spyOn(userTypeFormService, 'getUserType').mockReturnValue(userType);
      jest.spyOn(userTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userType }));
      saveSubject.complete();

      // THEN
      expect(userTypeFormService.getUserType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userTypeService.update).toHaveBeenCalledWith(expect.objectContaining(userType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserType>>();
      const userType = { id: 123 };
      jest.spyOn(userTypeFormService, 'getUserType').mockReturnValue({ id: null });
      jest.spyOn(userTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userType }));
      saveSubject.complete();

      // THEN
      expect(userTypeFormService.getUserType).toHaveBeenCalled();
      expect(userTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserType>>();
      const userType = { id: 123 };
      jest.spyOn(userTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
