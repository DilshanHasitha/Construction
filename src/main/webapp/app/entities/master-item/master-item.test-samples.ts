import { IMasterItem, NewMasterItem } from './master-item.model';

export const sampleWithRequiredData: IMasterItem = {
  id: 70797,
  code: 'Switchable',
  name: 'Creative implement enable',
};

export const sampleWithPartialData: IMasterItem = {
  id: 12954,
  code: 'Checking',
  name: 'navigating Officer encryption',
};

export const sampleWithFullData: IMasterItem = {
  id: 40016,
  code: 'Minnesota',
  name: 'connecting groupware Rubber',
  description: 'drive',
  isActive: false,
};

export const sampleWithNewData: NewMasterItem = {
  code: 'Cedi',
  name: 'Rustic',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
