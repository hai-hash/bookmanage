import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IBook } from 'app/shared/model/book.model';

export interface ICatagory {
  id?: number;
  name?: string;
  modifiedDate?: string;
  isActive?: boolean;
  user?: IUser | null;
  catalog?: ICatagory | null;
  books?: IBook[] | null;
}

export const defaultValue: Readonly<ICatagory> = {
  isActive: false,
};
