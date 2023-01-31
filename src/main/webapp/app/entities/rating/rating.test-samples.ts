import { IRating, NewRating } from './rating.model';

export const sampleWithRequiredData: IRating = {
  id: 92458,
};

export const sampleWithPartialData: IRating = {
  id: 62920,
  name: 'flexibility Liechtenstein',
};

export const sampleWithFullData: IRating = {
  id: 96232,
  name: 'Intelligent',
  rateValue: 16539,
};

export const sampleWithNewData: NewRating = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
