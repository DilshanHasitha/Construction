import dayjs from 'dayjs/esm';
import { IMasterItem } from 'app/entities/master-item/master-item.model';
import { IUnitOfMeasure } from 'app/entities/unit-of-measure/unit-of-measure.model';
import { IBOQs } from 'app/entities/bo-qs/bo-qs.model';

export interface IBOQDetails {
  id: number;
  code?: string | null;
  orderPlacedOn?: dayjs.Dayjs | null;
  qty?: number | null;
  isActive?: boolean | null;
  item?: Pick<IMasterItem, 'id' | 'code'> | null;
  per?: Pick<IUnitOfMeasure, 'id' | 'unitOfMeasureDescription'> | null;
  unit?: Pick<IUnitOfMeasure, 'id' | 'unitOfMeasureDescription'> | null;
  boqs?: Pick<IBOQs, 'id'>[] | null;
}

export type NewBOQDetails = Omit<IBOQDetails, 'id'> & { id: null };
