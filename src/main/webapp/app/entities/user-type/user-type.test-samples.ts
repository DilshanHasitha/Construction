import { IUserType, NewUserType } from './user-type.model';

export const sampleWithRequiredData: IUserType = {
  id: 38772,
  userRole: 'Kentucky',
};

export const sampleWithPartialData: IUserType = {
  id: 62775,
  code: 'Buckinghamshire',
  userRole: 'Soap Adaptive matrix',
};

export const sampleWithFullData: IUserType = {
  id: 56900,
  code: 'Response Books',
  userRole: 'Accountability transmitting parse',
  isActive: false,
};

export const sampleWithNewData: NewUserType = {
  userRole: 'Orchestrator encryption',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
