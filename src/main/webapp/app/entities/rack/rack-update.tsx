import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './rack.reducer';
import { IRack } from 'app/shared/model/rack.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRackUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RackUpdate = (props: IRackUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { rackEntity, users, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/rack' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getUsers();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...rackEntity,
        ...values,
        user: users.find(it => it.id.toString() === values.userId.toString()),
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bookManageApp.rack.home.createOrEditLabel" data-cy="RackCreateUpdateHeading">
            Create or edit a Rack
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : rackEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="rack-id">ID</Label>
                  <AvInput id="rack-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="numberLabel" for="rack-number">
                  Number
                </Label>
                <AvField id="rack-number" data-cy="number" type="string" className="form-control" name="number" />
              </AvGroup>
              <AvGroup>
                <Label id="locationIdentifierLabel" for="rack-locationIdentifier">
                  Location Identifier
                </Label>
                <AvField id="rack-locationIdentifier" data-cy="locationIdentifier" type="text" name="locationIdentifier" />
              </AvGroup>
              <AvGroup>
                <Label id="modifiedDateLabel" for="rack-modifiedDate">
                  Modified Date
                </Label>
                <AvField
                  id="rack-modifiedDate"
                  data-cy="modifiedDate"
                  type="date"
                  className="form-control"
                  name="modifiedDate"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="isActiveLabel">
                  <AvInput id="rack-isActive" data-cy="isActive" type="checkbox" className="form-check-input" name="isActive" />
                  Is Active
                </Label>
              </AvGroup>
              <AvGroup>
                <Label for="rack-user">User</Label>
                <AvInput id="rack-user" data-cy="user" type="select" className="form-control" name="userId">
                  <option value="" key="0" />
                  {users
                    ? users.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.login}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/rack" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  users: storeState.userManagement.users,
  rackEntity: storeState.rack.entity,
  loading: storeState.rack.loading,
  updating: storeState.rack.updating,
  updateSuccess: storeState.rack.updateSuccess,
});

const mapDispatchToProps = {
  getUsers,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RackUpdate);
