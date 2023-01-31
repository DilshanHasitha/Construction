import { IUnitOfMeasure, NewUnitOfMeasure } from './unit-of-measure.model';

export const sampleWithRequiredData: IUnitOfMeasure = {
  id: 42736,
  unitOfMeasureCode: 'York RAM Health',
  unitOfMeasureDescription: 'Producer Technician Designer',
};

export const sampleWithPartialData: IUnitOfMeasure = {
  id: 72468,
  unitOfMeasureCode: 'Kids',
  unitOfMeasureDescription: 'exploit',
};

export const sampleWithFullData: IUnitOfMeasure = {
  id: 40424,
  unitOfMeasureCode: 'Synergistic system-worthy',
  unitOfMeasureDescription: 'calculating Causeway multi-byte',
  isActive: true,
};

export const sampleWithNewData: NewUnitOfMeasure = {
  unitOfMeasureCode: 'Electronics azure',
  unitOfMeasureDescription: 'Licensed Face withdrawal',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
