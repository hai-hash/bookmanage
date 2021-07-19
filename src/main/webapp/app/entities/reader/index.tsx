import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Reader from './reader';
import ReaderDetail from './reader-detail';
import ReaderUpdate from './reader-update';
import ReaderDeleteDialog from './reader-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ReaderUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ReaderUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ReaderDetail} />
      <ErrorBoundaryRoute path={match.url} component={Reader} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ReaderDeleteDialog} />
  </>
);

export default Routes;
