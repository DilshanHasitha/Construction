import { IOrderDetails, NewOrderDetails } from './order-details.model';

export const sampleWithRequiredData: IOrderDetails = {
  id: 38246,
  orderQty: 51069,
};

export const sampleWithPartialData: IOrderDetails = {
  id: 31715,
  orderQty: 5009,
};

export const sampleWithFullData: IOrderDetails = {
  id: 41207,
  orderQty: 4571,
  revisedItemSalesPrice: 46445,
  note: 'Rupee ivory withdrawal',
};

export const sampleWithNewData: NewOrderDetails = {
  orderQty: 7528,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
