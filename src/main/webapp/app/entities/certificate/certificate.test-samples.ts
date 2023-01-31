import { ICertificate, NewCertificate } from './certificate.model';

export const sampleWithRequiredData: ICertificate = {
  id: 61536,
};

export const sampleWithPartialData: ICertificate = {
  id: 11671,
  imgUrl: 'expedite e-tailers Jewelery',
};

export const sampleWithFullData: ICertificate = {
  id: 73347,
  imgUrl: 'scale',
  description: 'gold ADP Ergonomic',
};

export const sampleWithNewData: NewCertificate = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
