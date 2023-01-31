import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UserPermissionDetailComponent } from './user-permission-detail.component';

describe('UserPermission Management Detail Component', () => {
  let comp: UserPermissionDetailComponent;
  let fixture: ComponentFixture<UserPermissionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserPermissionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ userPermission: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(UserPermissionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(UserPermissionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load userPermission on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.userPermission).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
