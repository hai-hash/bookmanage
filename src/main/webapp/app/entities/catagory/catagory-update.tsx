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
import { getEntities as getCatagories } from 'app/entities/catagory/catagory.reducer';
import { getEntity, updateEntity, createEntity, reset } from './catagory.reducer';
import { ICatagory } from 'app/shared/model/catagory.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICatagoryUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CatagoryUpdate = (props: ICatagoryUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { catagoryEntity, users, catagories, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/catagory' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getUsers();
    props.getCatagories();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...catagoryEntity,
        ...values,
        user: users.find(it => it.id.toString() === values.userId.toString()),
        catalog: catagories.find(it => it.id.toString() === values.catalogId.toString()),
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
          <h2 id="bookManageApp.catagory.home.createOrEditLabel" data-cy="CatagoryCreateUpdateHeading">
            Create or edit a Catagory
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : catagoryEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="catagory-id">ID</Label>
                  <AvInput id="catagory-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="catagory-name">
                  Name
                </Label>
                <AvField
                  id="catagory-name"
                  data-cy="name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="modifiedDateLabel" for="catagory-modifiedDate">
                  Modified Date
                </Label>
                <AvField
                  id="catagory-modifiedDate"
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
                  <AvInput id="catagory-isActive" data-cy="isActive" type="checkbox" className="form-check-input" name="isActive" />
                  Is Active
                </Label>
              </AvGroup>
              <AvGroup>
                <Label for="catagory-user">User</Label>
                <AvInput id="catagory-user" data-cy="user" type="select" className="form-control" name="userId">
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
              <AvGroup>
                <Label for="catagory-catalog">Catalog</Label>
                <AvInput id="catagory-catalog" data-cy="catalog" type="select" className="form-control" name="catalogId">
                  <option value="" key="0" />
                  {catagories
                    ? catagories.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/catagory" replace color="info">
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
  catagories: storeState.catagory.entities,
  catagoryEntity: storeState.catagory.entity,
  loading: storeState.catagory.loading,
  updating: storeState.catagory.updating,
  updateSuccess: storeState.catagory.updateSuccess,
});

const mapDispatchToProps = {
  getUsers,
  getCatagories,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CatagoryUpdate);
