import { IExUser, NewExUser } from './ex-user.model';

export const sampleWithRequiredData: IExUser = {
  id: 92426,
  login: 'Keyboard grid-enabled',
  userName: 'Consultant parsing',
  email: '>wLn5@Uj_.n$5',
};

export const sampleWithPartialData: IExUser = {
  id: 84192,
  login: 'compress',
  userName: 'Steel Plastic',
  firstName: 'Ruby',
  email: 'r`Bu@N#.V10+-R',
  isActive: true,
  phone: 31788,
};

export const sampleWithFullData: IExUser = {
  id: 46051,
  login: 'Radial',
  userName: 'Beauty SQL',
  firstName: 'Bryce',
  lastName: 'McLaughlin',
  email: 'u:En@D?i8.4K',
  isActive: true,
  phone: 13369,
  brNumber: 'Books',
};

export const sampleWithNewData: NewExUser = {
  login: 'transmit',
  userName: 'North',
  email: 'eN/2H@mJ5S.cq',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
