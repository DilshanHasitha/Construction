export interface IUnitOfMeasure {
  id: number;
  unitOfMeasureCode?: string | null;
  unitOfMeasureDescription?: string | null;
  isActive?: boolean | null;
}

export type NewUnitOfMeasure = Omit<IUnitOfMeasure, 'id'> & { id: null };
