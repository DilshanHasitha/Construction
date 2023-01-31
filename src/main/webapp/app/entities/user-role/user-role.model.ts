import { IUserPermission } from 'app/entities/user-permission/user-permission.model';

export interface IUserRole {
  id: number;
  code?: string | null;
  userRole?: string | null;
  isActive?: boolean | null;
  userPermissions?: Pick<IUserPermission, 'id' | 'document'>[] | null;
}

export type NewUserRole = Omit<IUserRole, 'id'> & { id: null };
