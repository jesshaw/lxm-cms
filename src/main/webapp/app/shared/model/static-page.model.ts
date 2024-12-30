import { ICategory } from 'app/shared/model/category.model';

export interface IStaticPage {
  id?: number;
  title?: string | null;
  content?: string | null;
  status?: string | null;
  category?: ICategory | null;
}

export const defaultValue: Readonly<IStaticPage> = {};
