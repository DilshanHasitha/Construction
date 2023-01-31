export interface ICertificateType {
  id: number;
  code?: string | null;
  name?: string | null;
  isActive?: boolean | null;
}

export type NewCertificateType = Omit<ICertificateType, 'id'> & { id: null };
