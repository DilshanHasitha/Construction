import { IItem, NewItem } from './item.model';

export const sampleWithRequiredData: IItem = {
  id: 89800,
};

export const sampleWithPartialData: IItem = {
  id: 98428,
  itemPrice: 90297,
  specialPrice: 59618,
  isActive: false,
  minQTY: 77433,
  maxQTY: 42728,
  leadTime: 40242,
  reorderQty: 47821,
  itemBarcode: 'Intelligent Silver',
};

export const sampleWithFullData: IItem = {
  id: 24056,
  itemPrice: 2770,
  itemCost: 34733,
  bannerText: 'proactive Peru harness',
  specialPrice: 35643,
  isActive: false,
  minQTY: 39162,
  maxQTY: 85412,
  steps: 59519,
  longDescription: 'Maryland Checking synthesize',
  leadTime: 64608,
  reorderQty: 96728,
  itemBarcode: 'empower Oro Generic',
};

export const sampleWithNewData: NewItem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
