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
import { getEntity, updateEntity, createEntity, reset } from './book-lending.reducer';
import { IBookLending } from 'app/shared/model/book-lending.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IBookLendingUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BookLendingUpdate = (props: IBookLendingUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { bookLendingEntity, users, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/book-lending' + props.location.search);
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
        ...bookLendingEntity,
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
          <h2 id="bookManageApp.bookLending.home.createOrEditLabel" data-cy="BookLendingCreateUpdateHeading">
            Create or edit a BookLending
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : bookLendingEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="book-lending-id">ID</Label>
                  <AvInput id="book-lending-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="creationDateLabel" for="book-lending-creationDate">
                  Creation Date
                </Label>
                <AvField
                  id="book-lending-creationDate"
                  data-cy="creationDate"
                  type="date"
                  className="form-control"
                  name="creationDate"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="book-lending-status">
                  Status
                </Label>
                <AvInput
                  id="book-lending-status"
                  data-cy="status"
                  type="select"
                  className="form-control"
                  name="status"
                  value={(!isNew && bookLendingEntity.status) || 'PENDING'}
                >
                  <option value="PENDING">PENDING</option>
                  <option value="DONE">DONE</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="descriptionLabel" for="book-lending-description">
                  Description
                </Label>
                <AvField id="book-lending-description" data-cy="description" type="text" name="description" />
              </AvGroup>
              <AvGroup>
                <Label for="book-lending-user">User</Label>
                <AvInput id="book-lending-user" data-cy="user" type="select" className="form-control" name="userId">
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
              <Button tag={Link} id="cancel-save" to="/book-lending" replace color="info">
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
  bookLendingEntity: storeState.bookLending.entity,
  loading: storeState.bookLending.loading,
  updating: storeState.bookLending.updating,
  updateSuccess: storeState.bookLending.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(BookLendingUpdate);
