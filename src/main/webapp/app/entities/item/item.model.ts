import { IMasterItem } from 'app/entities/master-item/master-item.model';
import { IUnitOfMeasure } from 'app/entities/unit-of-measure/unit-of-measure.model';
import { IExUser } from 'app/entities/ex-user/ex-user.model';
import { IRating } from 'app/entities/rating/rating.model';
import { ICertificate } from 'app/entities/certificate/certificate.model';

export interface IItem {
  id: number;
  itemPrice?: number | null;
  itemCost?: number | null;
  bannerText?: string | null;
  specialPrice?: number | null;
  isActive?: boolean | null;
  minQTY?: number | null;
  maxQTY?: number | null;
  steps?: number | null;
  longDescription?: string | null;
  leadTime?: number | null;
  reorderQty?: number | null;
  itemBarcode?: string | null;
  masterItem?: Pick<IMasterItem, 'id' | 'name'> | null;
  unit?: Pick<IUnitOfMeasure, 'id' | 'unitOfMeasureDescription'> | null;
  exUser?: Pick<IExUser, 'id' | 'userName'> | null;
  ratings?: Pick<IRating, 'id' | 'name'>[] | null;
  certificates?: Pick<ICertificate, 'id'>[] | null;
}

export type NewItem = Omit<IItem, 'id'> & { id: null };
