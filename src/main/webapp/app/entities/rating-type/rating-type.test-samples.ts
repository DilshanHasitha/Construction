import { IRatingType, NewRatingType } from './rating-type.model';

export const sampleWithRequiredData: IRatingType = {
  id: 84444,
};

export const sampleWithPartialData: IRatingType = {
  id: 67049,
  name: 'Cheese Senior Account',
  description: 67766,
};

export const sampleWithFullData: IRatingType = {
  id: 65840,
  name: 'Moldovan Baby',
  description: 28834,
};

export const sampleWithNewData: NewRatingType = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
