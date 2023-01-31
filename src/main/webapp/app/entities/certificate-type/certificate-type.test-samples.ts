import { ICertificateType, NewCertificateType } from './certificate-type.model';

export const sampleWithRequiredData: ICertificateType = {
  id: 56533,
  code: 'input',
};

export const sampleWithPartialData: ICertificateType = {
  id: 66761,
  code: 'Mouse mission-critical Applications',
  name: 'throughput',
  isActive: false,
};

export const sampleWithFullData: ICertificateType = {
  id: 47801,
  code: 'Macedonia Granite parsing',
  name: 'Awesome Pakistan',
  isActive: false,
};

export const sampleWithNewData: NewCertificateType = {
  code: 'turquoise transform',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
