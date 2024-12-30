export interface ICategory {
  id?: number;
  name?: string | null;
  contentType?: string | null;
  sort?: number | null;
  parent?: ICategory | null;
}

export const defaultValue: Readonly<ICategory> = {};
