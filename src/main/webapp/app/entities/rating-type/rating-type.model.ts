export interface IRatingType {
  id: number;
  name?: string | null;
  description?: number | null;
}

export type NewRatingType = Omit<IRatingType, 'id'> & { id: null };
