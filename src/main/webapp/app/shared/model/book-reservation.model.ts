import dayjs from 'dayjs';
import { IBookItem } from 'app/shared/model/book-item.model';
import { IReader } from 'app/shared/model/reader.model';
import { IBookLendingDetails } from 'app/shared/model/book-lending-details.model';
import { ReservationStatus } from 'app/shared/model/enumerations/reservation-status.model';

export interface IBookReservation {
  id?: number;
  creationDate?: string | null;
  status?: ReservationStatus | null;
  bookItem?: IBookItem | null;
  reader?: IReader | null;
  bookLendingDetails?: IBookLendingDetails | null;
}

export const defaultValue: Readonly<IBookReservation> = {};
