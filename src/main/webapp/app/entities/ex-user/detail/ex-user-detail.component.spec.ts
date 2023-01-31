import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ExUserDetailComponent } from './ex-user-detail.component';

describe('ExUser Management Detail Component', () => {
  let comp: ExUserDetailComponent;
  let fixture: ComponentFixture<ExUserDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExUserDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ exUser: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ExUserDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ExUserDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load exUser on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.exUser).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
