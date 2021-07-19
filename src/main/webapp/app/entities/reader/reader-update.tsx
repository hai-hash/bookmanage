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
import { getEntity, updateEntity, createEntity, reset } from './reader.reducer';
import { IReader } from 'app/shared/model/reader.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IReaderUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ReaderUpdate = (props: IReaderUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { readerEntity, users, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/reader' + props.location.search);
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
        ...readerEntity,
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
          <h2 id="bookManageApp.reader.home.createOrEditLabel" data-cy="ReaderCreateUpdateHeading">
            Create or edit a Reader
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : readerEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="reader-id">ID</Label>
                  <AvInput id="reader-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="phoneLabel" for="reader-phone">
                  Phone
                </Label>
                <AvField id="reader-phone" data-cy="phone" type="text" name="phone" />
              </AvGroup>
              <AvGroup>
                <Label id="streetAddressLabel" for="reader-streetAddress">
                  Street Address
                </Label>
                <AvField id="reader-streetAddress" data-cy="streetAddress" type="text" name="streetAddress" />
              </AvGroup>
              <AvGroup>
                <Label id="cityLabel" for="reader-city">
                  City
                </Label>
                <AvField id="reader-city" data-cy="city" type="text" name="city" />
              </AvGroup>
              <AvGroup>
                <Label id="stateLabel" for="reader-state">
                  State
                </Label>
                <AvField id="reader-state" data-cy="state" type="text" name="state" />
              </AvGroup>
              <AvGroup>
                <Label id="zipCodeLabel" for="reader-zipCode">
                  Zip Code
                </Label>
                <AvField id="reader-zipCode" data-cy="zipCode" type="text" name="zipCode" />
              </AvGroup>
              <AvGroup>
                <Label id="countryLabel" for="reader-country">
                  Country
                </Label>
                <AvField
                  id="reader-country"
                  data-cy="country"
                  type="text"
                  name="country"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="reader-status">
                  Status
                </Label>
                <AvInput
                  id="reader-status"
                  data-cy="status"
                  type="select"
                  className="form-control"
                  name="status"
                  value={(!isNew && readerEntity.status) || 'ACTIVE'}
                >
                  <option value="ACTIVE">ACTIVE</option>
                  <option value="CLOSED">CLOSED</option>
                  <option value="CANCELED">CANCELED</option>
                  <option value="BLACKLISTED">BLACKLISTED</option>
                  <option value="NONE">NONE</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="modifiedDateLabel" for="reader-modifiedDate">
                  Modified Date
                </Label>
                <AvField
                  id="reader-modifiedDate"
                  data-cy="modifiedDate"
                  type="date"
                  className="form-control"
                  name="modifiedDate"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="reader-user">User</Label>
                <AvInput id="reader-user" data-cy="user" type="select" className="form-control" name="userId">
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
              <Button tag={Link} id="cancel-save" to="/reader" replace color="info">
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
  readerEntity: storeState.reader.entity,
  loading: storeState.reader.loading,
  updating: storeState.reader.updating,
  updateSuccess: storeState.reader.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(ReaderUpdate);
