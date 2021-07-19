import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IBookReservation } from 'app/shared/model/book-reservation.model';
import { AccountStatus } from 'app/shared/model/enumerations/account-status.model';

export interface IReader {
  id?: number;
  phone?: string | null;
  streetAddress?: string | null;
  city?: string | null;
  state?: string | null;
  zipCode?: string | null;
  country?: string;
  status?: AccountStatus | null;
  modifiedDate?: string;
  user?: IUser | null;
  bookReservations?: IBookReservation[] | null;
}

export const defaultValue: Readonly<IReader> = {};
