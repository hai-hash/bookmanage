import dayjs from 'dayjs';
import { IBookReservation } from 'app/shared/model/book-reservation.model';
import { IBookLending } from 'app/shared/model/book-lending.model';

export interface IBookLendingDetails {
  id?: number;
  dueDate?: string;
  returnDate?: string | null;
  price?: number | null;
  bookReservation?: IBookReservation | null;
  bookLending?: IBookLending | null;
}

export const defaultValue: Readonly<IBookLendingDetails> = {};
