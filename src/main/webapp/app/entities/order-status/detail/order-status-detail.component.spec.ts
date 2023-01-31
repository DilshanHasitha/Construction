import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrderStatusDetailComponent } from './order-status-detail.component';

describe('OrderStatus Management Detail Component', () => {
  let comp: OrderStatusDetailComponent;
  let fixture: ComponentFixture<OrderStatusDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OrderStatusDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ orderStatus: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OrderStatusDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OrderStatusDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load orderStatus on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.orderStatus).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
