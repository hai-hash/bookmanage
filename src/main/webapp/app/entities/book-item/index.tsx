import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BookItem from './book-item';
import BookItemDetail from './book-item-detail';
import BookItemUpdate from './book-item-update';
import BookItemDeleteDialog from './book-item-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BookItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BookItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BookItemDetail} />
      <ErrorBoundaryRoute path={match.url} component={BookItem} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BookItemDeleteDialog} />
  </>
);

export default Routes;
