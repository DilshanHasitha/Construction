export interface IUserType {
  id: number;
  code?: string | null;
  userRole?: string | null;
  isActive?: boolean | null;
}

export type NewUserType = Omit<IUserType, 'id'> & { id: null };
