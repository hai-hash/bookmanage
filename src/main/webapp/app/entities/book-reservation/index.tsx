import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BookReservation from './book-reservation';
import BookReservationDetail from './book-reservation-detail';
import BookReservationUpdate from './book-reservation-update';
import BookReservationDeleteDialog from './book-reservation-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BookReservationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BookReservationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BookReservationDetail} />
      <ErrorBoundaryRoute path={match.url} component={BookReservation} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BookReservationDeleteDialog} />
  </>
);

export default Routes;
