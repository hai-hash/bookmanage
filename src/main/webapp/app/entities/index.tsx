import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Book from './book';
import BookItem from './book-item';
import Reader from './reader';
import Rack from './rack';
import Author from './author';
import Catagory from './catagory';
import Publisher from './publisher';
import BookReservation from './book-reservation';
import BookLending from './book-lending';
import BookLendingDetails from './book-lending-details';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}book`} component={Book} />
      <ErrorBoundaryRoute path={`${match.url}book-item`} component={BookItem} />
      <ErrorBoundaryRoute path={`${match.url}reader`} component={Reader} />
      <ErrorBoundaryRoute path={`${match.url}rack`} component={Rack} />
      <ErrorBoundaryRoute path={`${match.url}author`} component={Author} />
      <ErrorBoundaryRoute path={`${match.url}catagory`} component={Catagory} />
      <ErrorBoundaryRoute path={`${match.url}publisher`} component={Publisher} />
      <ErrorBoundaryRoute path={`${match.url}book-reservation`} component={BookReservation} />
      <ErrorBoundaryRoute path={`${match.url}book-lending`} component={BookLending} />
      <ErrorBoundaryRoute path={`${match.url}book-lending-details`} component={BookLendingDetails} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
