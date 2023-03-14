import dayjs from 'dayjs/esm';

import { IProject, NewProject } from './project.model';

export const sampleWithRequiredData: IProject = {
  id: 55962,
  name: 'COM dedicated Agent',
};

export const sampleWithPartialData: IProject = {
  id: 61186,
  code: 'Implemented dot-com Avon',
  name: 'capacitor',
  description: 'Investor Loan Avon',
  completionDate: dayjs('2023-01-30'),
  notes: 'indexing',
  address: 'Niger (Slovak mindshare',
  priority: 'Bike Timor-Leste',
  progress: 82463,
};

export const sampleWithFullData: IProject = {
  id: 2133,
  code: 'Proactive',
  name: 'productize Data',
  isActive: true,
  description: 'circuit Granite Canyon',
  completionDate: dayjs('2023-01-30'),
  regNumber: 'Ridges infomediaries',
  notes: 'Uzbekistan EXE',
  address: 'leading Developer',
  priority: 'open-source Auto',
  progress: 39852,
};

export const sampleWithNewData: NewProject = {
  name: 'auxiliary',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
