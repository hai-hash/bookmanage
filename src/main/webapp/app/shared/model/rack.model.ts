import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IBookItem } from 'app/shared/model/book-item.model';

export interface IRack {
  id?: number;
  number?: number | null;
  locationIdentifier?: string | null;
  modifiedDate?: string;
  isActive?: boolean;
  user?: IUser | null;
  bookItems?: IBookItem[] | null;
}

export const defaultValue: Readonly<IRack> = {
  isActive: false,
};
