import { IBOQs, NewBOQs } from './bo-qs.model';

export const sampleWithRequiredData: IBOQs = {
  id: 45770,
};

export const sampleWithPartialData: IBOQs = {
  id: 22646,
  code: 'Lead US',
};

export const sampleWithFullData: IBOQs = {
  id: 92943,
  code: 'strategy',
  isActive: false,
};

export const sampleWithNewData: NewBOQs = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
