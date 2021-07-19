import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IRack } from 'app/shared/model/rack.model';
import { IBook } from 'app/shared/model/book.model';
import { IBookReservation } from 'app/shared/model/book-reservation.model';
import { BookFormat } from 'app/shared/model/enumerations/book-format.model';
import { BookStatus } from 'app/shared/model/enumerations/book-status.model';

export interface IBookItem {
  id?: number;
  barcode?: string | null;
  isReferenceOnly?: boolean | null;
  borrowed?: string | null;
  dueDate?: string | null;
  price?: number | null;
  format?: BookFormat;
  status?: BookStatus | null;
  dateOfPurchase?: string | null;
  publicationDate?: string;
  modifiedDate?: string;
  user?: IUser | null;
  rack?: IRack | null;
  book?: IBook | null;
  bookReservation?: IBookReservation | null;
}

export const defaultValue: Readonly<IBookItem> = {
  isReferenceOnly: false,
};
