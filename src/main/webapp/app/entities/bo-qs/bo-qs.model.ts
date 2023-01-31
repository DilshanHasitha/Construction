import { IExUser } from 'app/entities/ex-user/ex-user.model';
import { IBOQDetails } from 'app/entities/boq-details/boq-details.model';

export interface IBOQs {
  id: number;
  code?: string | null;
  isActive?: boolean | null;
  constructors?: Pick<IExUser, 'id' | 'userName'> | null;
  boqDetails?: Pick<IBOQDetails, 'id'>[] | null;
}

export type NewBOQs = Omit<IBOQs, 'id'> & { id: null };
