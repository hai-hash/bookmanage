import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Catagory from './catagory';
import CatagoryDetail from './catagory-detail';
import CatagoryUpdate from './catagory-update';
import CatagoryDeleteDialog from './catagory-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CatagoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CatagoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CatagoryDetail} />
      <ErrorBoundaryRoute path={match.url} component={Catagory} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CatagoryDeleteDialog} />
  </>
);

export default Routes;
