import { ICertificateType } from 'app/entities/certificate-type/certificate-type.model';
import { IItem } from 'app/entities/item/item.model';

export interface ICertificate {
  id: number;
  imgUrl?: string | null;
  description?: string | null;
  certificateType?: Pick<ICertificateType, 'id' | 'code'> | null;
  items?: Pick<IItem, 'id'>[] | null;
}

export type NewCertificate = Omit<ICertificate, 'id'> & { id: null };
