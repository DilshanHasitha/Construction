import { IUser } from 'app/entities/user/user.model';
import { IUserRole } from 'app/entities/user-role/user-role.model';
import { ICompany } from 'app/entities/company/company.model';

export interface IExUser {
  id: number;
  login?: string | null;
  userName?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  isActive?: boolean | null;
  phone?: number | null;
  brNumber?: string | null;
  user?: Pick<IUser, 'id'> | null;
  userRole?: Pick<IUserRole, 'id' | 'userRole'> | null;
  company?: Pick<ICompany, 'id' | 'name'> | null;
}

export type NewExUser = Omit<IExUser, 'id'> & { id: null };
