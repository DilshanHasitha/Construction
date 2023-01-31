import { IUserType } from 'app/entities/user-type/user-type.model';

export interface ICompany {
  id: number;
  code?: string | null;
  name?: string | null;
  brNumber?: string | null;
  isActive?: boolean | null;
  userType?: Pick<IUserType, 'id' | 'userRole'> | null;
}

export type NewCompany = Omit<ICompany, 'id'> & { id: null };
