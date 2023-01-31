export interface IOrderStatus {
  id: number;
  code?: string | null;
  description?: string | null;
  isActive?: boolean | null;
}

export type NewOrderStatus = Omit<IOrderStatus, 'id'> & { id: null };
