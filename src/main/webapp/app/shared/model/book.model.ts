import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ICatagory } from 'app/shared/model/catagory.model';
import { IPublisher } from 'app/shared/model/publisher.model';
import { IAuthor } from 'app/shared/model/author.model';
import { IBookItem } from 'app/shared/model/book-item.model';

export interface IBook {
  id?: number;
  isbn?: string;
  title?: string;
  subject?: string;
  target?: string | null;
  language?: string;
  numberOfPages?: number;
  imageUrl?: string | null;
  modifiedDate?: string;
  user?: IUser | null;
  catagory?: ICatagory | null;
  publisher?: IPublisher | null;
  authors?: IAuthor[] | null;
  bookItems?: IBookItem[] | null;
}

export const defaultValue: Readonly<IBook> = {};
