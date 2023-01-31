import dayjs from 'dayjs/esm';

import { IProject, NewProject } from './project.model';

export const sampleWithRequiredData: IProject = {
  id: 55962,
  name: 'COM dedicated Agent',
};

export const sampleWithPartialData: IProject = {
  id: 77481,
  code: 'Personal enhance Borders',
  name: 'Centers',
  description: 'regional Loan bluetooth',
  completionDate: dayjs('2023-01-31'),
  notes: 'Product Light Niger',
  address: 'redundant microchip',
};

export const sampleWithFullData: IProject = {
  id: 16934,
  code: 'payment',
  name: 'backing Proactive',
  isActive: true,
  description: 'Assurance state',
  completionDate: dayjs('2023-01-30'),
  regNumber: 'Product Berkshire Cotton',
  notes: 'fresh-thinking Granite EXE',
  address: 'leading Developer',
};

export const sampleWithNewData: NewProject = {
  name: 'open-source Auto',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
