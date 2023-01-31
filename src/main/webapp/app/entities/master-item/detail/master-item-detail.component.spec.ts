import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MasterItemDetailComponent } from './master-item-detail.component';

describe('MasterItem Management Detail Component', () => {
  let comp: MasterItemDetailComponent;
  let fixture: ComponentFixture<MasterItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MasterItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ masterItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MasterItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MasterItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load masterItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.masterItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
