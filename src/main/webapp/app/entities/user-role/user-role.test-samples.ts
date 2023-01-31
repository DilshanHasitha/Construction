import { IUserRole, NewUserRole } from './user-role.model';

export const sampleWithRequiredData: IUserRole = {
  id: 43156,
  userRole: 'enable invoice',
};

export const sampleWithPartialData: IUserRole = {
  id: 30276,
  code: 'Account Borders bluetooth',
  userRole: 'paradigm compress',
};

export const sampleWithFullData: IUserRole = {
  id: 30809,
  code: 'system-worthy quantifying',
  userRole: 'ADP',
  isActive: false,
};

export const sampleWithNewData: NewUserRole = {
  userRole: 'Industrial',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
