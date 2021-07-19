import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IBookLendingDetails } from 'app/shared/model/book-lending-details.model';
import { LendingStatus } from 'app/shared/model/enumerations/lending-status.model';

export interface IBookLending {
  id?: number;
  creationDate?: string;
  status?: LendingStatus | null;
  description?: string | null;
  user?: IUser | null;
  bookLendingDetails?: IBookLendingDetails[] | null;
}

export const defaultValue: Readonly<IBookLending> = {};
