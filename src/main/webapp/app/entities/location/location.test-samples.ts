import { ILocation, NewLocation } from './location.model';

export const sampleWithRequiredData: ILocation = {
  id: 91847,
  country: 'Austria',
};

export const sampleWithPartialData: ILocation = {
  id: 73848,
  code: 'Beauty open 1080p',
  city: 'Pocatello',
  country: 'Monaco',
  lat: 6297,
  isActive: true,
};

export const sampleWithFullData: ILocation = {
  id: 80756,
  code: 'Directives',
  city: 'Shoreline',
  country: 'India',
  countryCode: 'AT',
  lat: 41809,
  lon: 27801,
  isActive: true,
};

export const sampleWithNewData: NewLocation = {
  country: 'Armenia',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
