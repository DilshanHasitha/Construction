import dayjs from 'dayjs/esm';
import { IExUser } from 'app/entities/ex-user/ex-user.model';
import { IOrderStatus } from 'app/entities/order-status/order-status.model';
import { IOrderDetails } from 'app/entities/order-details/order-details.model';

export interface IOrders {
  id: number;
  orderID?: string | null;
  customerName?: string | null;
  isActive?: boolean | null;
  orderPlacedOn?: dayjs.Dayjs | null;
  note?: string | null;
  exUser?: Pick<IExUser, 'id'> | null;
  orderStatus?: Pick<IOrderStatus, 'id'> | null;
  orderDetails?: Pick<IOrderDetails, 'id'>[] | null;
}

export type NewOrders = Omit<IOrders, 'id'> & { id: null };
