import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import book, {
  BookState
} from 'app/entities/book/book.reducer';
// prettier-ignore
import bookItem, {
  BookItemState
} from 'app/entities/book-item/book-item.reducer';
// prettier-ignore
import reader, {
  ReaderState
} from 'app/entities/reader/reader.reducer';
// prettier-ignore
import rack, {
  RackState
} from 'app/entities/rack/rack.reducer';
// prettier-ignore
import author, {
  AuthorState
} from 'app/entities/author/author.reducer';
// prettier-ignore
import catagory, {
  CatagoryState
} from 'app/entities/catagory/catagory.reducer';
// prettier-ignore
import publisher, {
  PublisherState
} from 'app/entities/publisher/publisher.reducer';
// prettier-ignore
import bookReservation, {
  BookReservationState
} from 'app/entities/book-reservation/book-reservation.reducer';
// prettier-ignore
import bookLending, {
  BookLendingState
} from 'app/entities/book-lending/book-lending.reducer';
// prettier-ignore
import bookLendingDetails, {
  BookLendingDetailsState
} from 'app/entities/book-lending-details/book-lending-details.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly book: BookState;
  readonly bookItem: BookItemState;
  readonly reader: ReaderState;
  readonly rack: RackState;
  readonly author: AuthorState;
  readonly catagory: CatagoryState;
  readonly publisher: PublisherState;
  readonly bookReservation: BookReservationState;
  readonly bookLending: BookLendingState;
  readonly bookLendingDetails: BookLendingDetailsState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  book,
  bookItem,
  reader,
  rack,
  author,
  catagory,
  publisher,
  bookReservation,
  bookLending,
  bookLendingDetails,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
});

export default rootReducer;
