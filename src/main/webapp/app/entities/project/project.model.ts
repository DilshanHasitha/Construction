import dayjs from 'dayjs/esm';
import { ILocation } from 'app/entities/location/location.model';
import { IExUser } from 'app/entities/ex-user/ex-user.model';

export interface IProject {
  id: number;
  code?: string | null;
  name?: string | null;
  isActive?: boolean | null;
  description?: string | null;
  completionDate?: dayjs.Dayjs | null;
  regNumber?: string | null;
  notes?: string | null;
  address?: string | null;
  location?: Pick<ILocation, 'id' | 'city'> | null;
  exUser?: Pick<IExUser, 'id' | 'userName'> | null;
}

export type NewProject = Omit<IProject, 'id'> & { id: null };
