export interface ILocation {
  id: number;
  code?: string | null;
  city?: string | null;
  country?: string | null;
  countryCode?: string | null;
  lat?: number | null;
  lon?: number | null;
  isActive?: boolean | null;
}

export type NewLocation = Omit<ILocation, 'id'> & { id: null };
