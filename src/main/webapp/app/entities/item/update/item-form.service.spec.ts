import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../item.test-samples';

import { ItemFormService } from './item-form.service';

describe('Item Form Service', () => {
  let service: ItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ItemFormService);
  });

  describe('Service methods', () => {
    describe('createItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            itemPrice: expect.any(Object),
            itemCost: expect.any(Object),
            bannerText: expect.any(Object),
            specialPrice: expect.any(Object),
            isActive: expect.any(Object),
            minQTY: expect.any(Object),
            maxQTY: expect.any(Object),
            steps: expect.any(Object),
            longDescription: expect.any(Object),
            leadTime: expect.any(Object),
            reorderQty: expect.any(Object),
            itemBarcode: expect.any(Object),
            masterItem: expect.any(Object),
            unit: expect.any(Object),
            exUser: expect.any(Object),
            ratings: expect.any(Object),
            certificates: expect.any(Object),
          })
        );
      });

      it('passing IItem should create a new form with FormGroup', () => {
        const formGroup = service.createItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            itemPrice: expect.any(Object),
            itemCost: expect.any(Object),
            bannerText: expect.any(Object),
            specialPrice: expect.any(Object),
            isActive: expect.any(Object),
            minQTY: expect.any(Object),
            maxQTY: expect.any(Object),
            steps: expect.any(Object),
            longDescription: expect.any(Object),
            leadTime: expect.any(Object),
            reorderQty: expect.any(Object),
            itemBarcode: expect.any(Object),
            masterItem: expect.any(Object),
            unit: expect.any(Object),
            exUser: expect.any(Object),
            ratings: expect.any(Object),
            certificates: expect.any(Object),
          })
        );
      });
    });

    describe('getItem', () => {
      it('should return NewItem for default Item initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createItemFormGroup(sampleWithNewData);

        const item = service.getItem(formGroup) as any;

        expect(item).toMatchObject(sampleWithNewData);
      });

      it('should return NewItem for empty Item initial value', () => {
        const formGroup = service.createItemFormGroup();

        const item = service.getItem(formGroup) as any;

        expect(item).toMatchObject({});
      });

      it('should return IItem', () => {
        const formGroup = service.createItemFormGroup(sampleWithRequiredData);

        const item = service.getItem(formGroup) as any;

        expect(item).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IItem should not enable id FormControl', () => {
        const formGroup = service.createItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewItem should disable id FormControl', () => {
        const formGroup = service.createItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
