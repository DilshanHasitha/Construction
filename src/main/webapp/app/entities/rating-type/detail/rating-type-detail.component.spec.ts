import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RatingTypeDetailComponent } from './rating-type-detail.component';

describe('RatingType Management Detail Component', () => {
  let comp: RatingTypeDetailComponent;
  let fixture: ComponentFixture<RatingTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RatingTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ ratingType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RatingTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RatingTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load ratingType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.ratingType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
