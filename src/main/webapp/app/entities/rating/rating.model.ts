import { IRatingType } from 'app/entities/rating-type/rating-type.model';
import { IItem } from 'app/entities/item/item.model';

export interface IRating {
  id: number;
  name?: string | null;
  rateValue?: number | null;
  ratingType?: Pick<IRatingType, 'id'> | null;
  exUsers?: Pick<IItem, 'id'>[] | null;
}

export type NewRating = Omit<IRating, 'id'> & { id: null };
