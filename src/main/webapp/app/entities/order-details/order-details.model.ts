import { IItem } from 'app/entities/item/item.model';
import { IOrders } from 'app/entities/orders/orders.model';

export interface IOrderDetails {
  id: number;
  orderQty?: number | null;
  revisedItemSalesPrice?: number | null;
  note?: string | null;
  item?: Pick<IItem, 'id'> | null;
  orders?: Pick<IOrders, 'id'>[] | null;
}

export type NewOrderDetails = Omit<IOrderDetails, 'id'> & { id: null };
