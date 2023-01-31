import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BOQsDetailComponent } from './bo-qs-detail.component';

describe('BOQs Management Detail Component', () => {
  let comp: BOQsDetailComponent;
  let fixture: ComponentFixture<BOQsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BOQsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ bOQs: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BOQsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BOQsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bOQs on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.bOQs).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
