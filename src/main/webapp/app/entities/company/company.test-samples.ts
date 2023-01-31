import { ICompany, NewCompany } from './company.model';

export const sampleWithRequiredData: ICompany = {
  id: 32440,
  name: 'Djibouti Washington',
};

export const sampleWithPartialData: ICompany = {
  id: 21993,
  code: 'Handmade Movies',
  name: 'Maryland Oval',
  isActive: false,
};

export const sampleWithFullData: ICompany = {
  id: 47542,
  code: 'Falkland',
  name: 'Ball',
  brNumber: 'contingency',
  isActive: true,
};

export const sampleWithNewData: NewCompany = {
  name: 'Organized Granite',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
