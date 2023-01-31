import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MasterItemFormService } from './master-item-form.service';
import { MasterItemService } from '../service/master-item.service';
import { IMasterItem } from '../master-item.model';
import { IExUser } from 'app/entities/ex-user/ex-user.model';
import { ExUserService } from 'app/entities/ex-user/service/ex-user.service';

import { MasterItemUpdateComponent } from './master-item-update.component';

describe('MasterItem Management Update Component', () => {
  let comp: MasterItemUpdateComponent;
  let fixture: ComponentFixture<MasterItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let masterItemFormService: MasterItemFormService;
  let masterItemService: MasterItemService;
  let exUserService: ExUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MasterItemUpdateComponent],
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
      .overrideTemplate(MasterItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MasterItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    masterItemFormService = TestBed.inject(MasterItemFormService);
    masterItemService = TestBed.inject(MasterItemService);
    exUserService = TestBed.inject(ExUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ExUser query and add missing value', () => {
      const masterItem: IMasterItem = { id: 456 };
      const exUser: IExUser = { id: 91340 };
      masterItem.exUser = exUser;

      const exUserCollection: IExUser[] = [{ id: 5082 }];
      jest.spyOn(exUserService, 'query').mockReturnValue(of(new HttpResponse({ body: exUserCollection })));
      const additionalExUsers = [exUser];
      const expectedCollection: IExUser[] = [...additionalExUsers, ...exUserCollection];
      jest.spyOn(exUserService, 'addExUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ masterItem });
      comp.ngOnInit();

      expect(exUserService.query).toHaveBeenCalled();
      expect(exUserService.addExUserToCollectionIfMissing).toHaveBeenCalledWith(
        exUserCollection,
        ...additionalExUsers.map(expect.objectContaining)
      );
      expect(comp.exUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const masterItem: IMasterItem = { id: 456 };
      const exUser: IExUser = { id: 41369 };
      masterItem.exUser = exUser;

      activatedRoute.data = of({ masterItem });
      comp.ngOnInit();

      expect(comp.exUsersSharedCollection).toContain(exUser);
      expect(comp.masterItem).toEqual(masterItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMasterItem>>();
      const masterItem = { id: 123 };
      jest.spyOn(masterItemFormService, 'getMasterItem').mockReturnValue(masterItem);
      jest.spyOn(masterItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ masterItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: masterItem }));
      saveSubject.complete();

      // THEN
      expect(masterItemFormService.getMasterItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(masterItemService.update).toHaveBeenCalledWith(expect.objectContaining(masterItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMasterItem>>();
      const masterItem = { id: 123 };
      jest.spyOn(masterItemFormService, 'getMasterItem').mockReturnValue({ id: null });
      jest.spyOn(masterItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ masterItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: masterItem }));
      saveSubject.complete();

      // THEN
      expect(masterItemFormService.getMasterItem).toHaveBeenCalled();
      expect(masterItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMasterItem>>();
      const masterItem = { id: 123 };
      jest.spyOn(masterItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ masterItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(masterItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareExUser', () => {
      it('Should forward to exUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(exUserService, 'compareExUser');
        comp.compareExUser(entity, entity2);
        expect(exUserService.compareExUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
