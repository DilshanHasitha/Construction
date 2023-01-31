import { IOrderStatus, NewOrderStatus } from './order-status.model';

export const sampleWithRequiredData: IOrderStatus = {
  id: 33405,
  code: 'framework infrastructures',
  description: 'Account',
};

export const sampleWithPartialData: IOrderStatus = {
  id: 97136,
  code: 'Granite',
  description: 'Turkmenistan Program',
  isActive: false,
};

export const sampleWithFullData: IOrderStatus = {
  id: 40401,
  code: 'FTP edge',
  description: 'circuit Drive',
  isActive: false,
};

export const sampleWithNewData: NewOrderStatus = {
  code: 'deposit Account Identity',
  description: 'software',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
