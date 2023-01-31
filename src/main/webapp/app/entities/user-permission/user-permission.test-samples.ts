import { IUserPermission, NewUserPermission } from './user-permission.model';

export const sampleWithRequiredData: IUserPermission = {
  id: 34645,
  action: 'cross-platform',
  document: 'Kwacha',
  description: 'calculating',
};

export const sampleWithPartialData: IUserPermission = {
  id: 74966,
  action: 'Incredible Cote Optimization',
  document: 'deposit Clothing Handmade',
  description: 'ivory CSS program',
};

export const sampleWithFullData: IUserPermission = {
  id: 48523,
  action: 'Chief',
  document: 'technologies Metrics',
  description: 'Hryvnia Small deposit',
};

export const sampleWithNewData: NewUserPermission = {
  action: 'withdrawal',
  document: 'Rubber',
  description: 'Bedfordshire',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
