import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BOQDetailsDetailComponent } from './boq-details-detail.component';

describe('BOQDetails Management Detail Component', () => {
  let comp: BOQDetailsDetailComponent;
  let fixture: ComponentFixture<BOQDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BOQDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ bOQDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BOQDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BOQDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bOQDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.bOQDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
