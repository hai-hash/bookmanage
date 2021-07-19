import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IBookItem } from 'app/shared/model/book-item.model';
import { getEntities as getBookItems } from 'app/entities/book-item/book-item.reducer';
import { IReader } from 'app/shared/model/reader.model';
import { getEntities as getReaders } from 'app/entities/reader/reader.reducer';
import { IBookLendingDetails } from 'app/shared/model/book-lending-details.model';
import { getEntities as getBookLendingDetails } from 'app/entities/book-lending-details/book-lending-details.reducer';
import { getEntity, updateEntity, createEntity, reset } from './book-reservation.reducer';
import { IBookReservation } from 'app/shared/model/book-reservation.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IBookReservationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BookReservationUpdate = (props: IBookReservationUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { bookReservationEntity, bookItems, readers, bookLendingDetails, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/book-reservation' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getBookItems();
    props.getReaders();
    props.getBookLendingDetails();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...bookReservationEntity,
        ...values,
        bookItem: bookItems.find(it => it.id.toString() === values.bookItemId.toString()),
        reader: readers.find(it => it.id.toString() === values.readerId.toString()),
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
          <h2 id="bookManageApp.bookReservation.home.createOrEditLabel" data-cy="BookReservationCreateUpdateHeading">
            Create or edit a BookReservation
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : bookReservationEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="book-reservation-id">ID</Label>
                  <AvInput id="book-reservation-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="creationDateLabel" for="book-reservation-creationDate">
                  Creation Date
                </Label>
                <AvField
                  id="book-reservation-creationDate"
                  data-cy="creationDate"
                  type="date"
                  className="form-control"
                  name="creationDate"
                />
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="book-reservation-status">
                  Status
                </Label>
                <AvInput
                  id="book-reservation-status"
                  data-cy="status"
                  type="select"
                  className="form-control"
                  name="status"
                  value={(!isNew && bookReservationEntity.status) || 'WAITING'}
                >
                  <option value="WAITING">WAITING</option>
                  <option value="CANCELED">CANCELED</option>
                  <option value="DONE">DONE</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="book-reservation-bookItem">Book Item</Label>
                <AvInput id="book-reservation-bookItem" data-cy="bookItem" type="select" className="form-control" name="bookItemId">
                  <option value="" key="0" />
                  {bookItems
                    ? bookItems.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="book-reservation-reader">Reader</Label>
                <AvInput id="book-reservation-reader" data-cy="reader" type="select" className="form-control" name="readerId">
                  <option value="" key="0" />
                  {readers
                    ? readers.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/book-reservation" replace color="info">
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
  bookItems: storeState.bookItem.entities,
  readers: storeState.reader.entities,
  bookLendingDetails: storeState.bookLendingDetails.entities,
  bookReservationEntity: storeState.bookReservation.entity,
  loading: storeState.bookReservation.loading,
  updating: storeState.bookReservation.updating,
  updateSuccess: storeState.bookReservation.updateSuccess,
});

const mapDispatchToProps = {
  getBookItems,
  getReaders,
  getBookLendingDetails,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BookReservationUpdate);
