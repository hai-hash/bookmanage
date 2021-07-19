import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BookLendingDetails from './book-lending-details';
import BookLendingDetailsDetail from './book-lending-details-detail';
import BookLendingDetailsUpdate from './book-lending-details-update';
import BookLendingDetailsDeleteDialog from './book-lending-details-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BookLendingDetailsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BookLendingDetailsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BookLendingDetailsDetail} />
      <ErrorBoundaryRoute path={match.url} component={BookLendingDetails} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BookLendingDetailsDeleteDialog} />
  </>
);

export default Routes;
