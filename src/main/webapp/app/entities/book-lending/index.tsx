import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BookLending from './book-lending';
import BookLendingDetail from './book-lending-detail';
import BookLendingUpdate from './book-lending-update';
import BookLendingDeleteDialog from './book-lending-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BookLendingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BookLendingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BookLendingDetail} />
      <ErrorBoundaryRoute path={match.url} component={BookLending} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BookLendingDeleteDialog} />
  </>
);

export default Routes;
