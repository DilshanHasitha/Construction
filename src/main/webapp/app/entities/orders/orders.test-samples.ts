import dayjs from 'dayjs/esm';

import { IOrders, NewOrders } from './orders.model';

export const sampleWithRequiredData: IOrders = {
  id: 32144,
  orderID: 'line transmit application',
  customerName: 'sensor',
};

export const sampleWithPartialData: IOrders = {
  id: 27953,
  orderID: 'Agent',
  customerName: 'Buckinghamshire',
  isActive: true,
  orderPlacedOn: dayjs('2023-01-30'),
  note: 'Response',
};

export const sampleWithFullData: IOrders = {
  id: 60530,
  orderID: 'Fresh',
  customerName: 'hack viral',
  isActive: true,
  orderPlacedOn: dayjs('2023-01-30'),
  note: 'Metal',
};

export const sampleWithNewData: NewOrders = {
  orderID: 'quantify Bhutanese',
  customerName: 'standardization invoice GB',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
