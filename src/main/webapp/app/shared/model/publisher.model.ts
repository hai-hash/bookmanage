import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IBook } from 'app/shared/model/book.model';

export interface IPublisher {
  id?: number;
  name?: string;
  modifiedDate?: string;
  isActive?: boolean;
  user?: IUser | null;
  books?: IBook[] | null;
}

export const defaultValue: Readonly<IPublisher> = {
  isActive: false,
};
