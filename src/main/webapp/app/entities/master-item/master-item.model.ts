import { IExUser } from 'app/entities/ex-user/ex-user.model';

export interface IMasterItem {
  id: number;
  code?: string | null;
  name?: string | null;
  description?: string | null;
  isActive?: boolean | null;
  exUser?: Pick<IExUser, 'id' | 'userName'> | null;
}

export type NewMasterItem = Omit<IMasterItem, 'id'> & { id: null };
