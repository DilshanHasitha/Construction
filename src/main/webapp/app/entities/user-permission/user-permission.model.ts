import { IUserRole } from 'app/entities/user-role/user-role.model';

export interface IUserPermission {
  id: number;
  action?: string | null;
  document?: string | null;
  description?: string | null;
  userRoles?: Pick<IUserRole, 'id'>[] | null;
}

export type NewUserPermission = Omit<IUserPermission, 'id'> & { id: null };
