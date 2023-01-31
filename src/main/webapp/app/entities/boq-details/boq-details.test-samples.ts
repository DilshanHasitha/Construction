import dayjs from 'dayjs/esm';

import { IBOQDetails, NewBOQDetails } from './boq-details.model';

export const sampleWithRequiredData: IBOQDetails = {
  id: 11375,
  code: 'productivity Computer',
};

export const sampleWithPartialData: IBOQDetails = {
  id: 19919,
  code: 'Buckinghamshire',
};

export const sampleWithFullData: IBOQDetails = {
  id: 8235,
  code: 'payment Director Switchable',
  orderPlacedOn: dayjs('2023-01-30'),
  qty: 78200,
  isActive: true,
};

export const sampleWithNewData: NewBOQDetails = {
  code: 'Bahraini',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
