import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UnitOfMeasureDetailComponent } from './unit-of-measure-detail.component';

describe('UnitOfMeasure Management Detail Component', () => {
  let comp: UnitOfMeasureDetailComponent;
  let fixture: ComponentFixture<UnitOfMeasureDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UnitOfMeasureDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ unitOfMeasure: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(UnitOfMeasureDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(UnitOfMeasureDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load unitOfMeasure on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.unitOfMeasure).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
