import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IBookReservation } from 'app/shared/model/book-reservation.model';
import { getEntities as getBookReservations } from 'app/entities/book-reservation/book-reservation.reducer';
import { IBookLending } from 'app/shared/model/book-lending.model';
import { getEntities as getBookLendings } from 'app/entities/book-lending/book-lending.reducer';
import { getEntity, updateEntity, createEntity, reset } from './book-lending-details.reducer';
import { IBookLendingDetails } from 'app/shared/model/book-lending-details.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IBookLendingDetailsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BookLendingDetailsUpdate = (props: IBookLendingDetailsUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { bookLendingDetailsEntity, bookReservations, bookLendings, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/book-lending-details' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getBookReservations();
    props.getBookLendings();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...bookLendingDetailsEntity,
        ...values,
        bookReservation: bookReservations.find(it => it.id.toString() === values.bookReservationId.toString()),
        bookLending: bookLendings.find(it => it.id.toString() === values.bookLendingId.toString()),
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
          <h2 id="bookManageApp.bookLendingDetails.home.createOrEditLabel" data-cy="BookLendingDetailsCreateUpdateHeading">
            Create or edit a BookLendingDetails
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : bookLendingDetailsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="book-lending-details-id">ID</Label>
                  <AvInput id="book-lending-details-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="dueDateLabel" for="book-lending-details-dueDate">
                  Due Date
                </Label>
                <AvField
                  id="book-lending-details-dueDate"
                  data-cy="dueDate"
                  type="date"
                  className="form-control"
                  name="dueDate"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="returnDateLabel" for="book-lending-details-returnDate">
                  Return Date
                </Label>
                <AvField id="book-lending-details-returnDate" data-cy="returnDate" type="date" className="form-control" name="returnDate" />
              </AvGroup>
              <AvGroup>
                <Label id="priceLabel" for="book-lending-details-price">
                  Price
                </Label>
                <AvField id="book-lending-details-price" data-cy="price" type="text" name="price" />
              </AvGroup>
              <AvGroup>
                <Label for="book-lending-details-bookReservation">Book Reservation</Label>
                <AvInput
                  id="book-lending-details-bookReservation"
                  data-cy="bookReservation"
                  type="select"
                  className="form-control"
                  name="bookReservationId"
                >
                  <option value="" key="0" />
                  {bookReservations
                    ? bookReservations.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="book-lending-details-bookLending">Book Lending</Label>
                <AvInput
                  id="book-lending-details-bookLending"
                  data-cy="bookLending"
                  type="select"
                  className="form-control"
                  name="bookLendingId"
                >
                  <option value="" key="0" />
                  {bookLendings
                    ? bookLendings.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/book-lending-details" replace color="info">
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
  bookReservations: storeState.bookReservation.entities,
  bookLendings: storeState.bookLending.entities,
  bookLendingDetailsEntity: storeState.bookLendingDetails.entity,
  loading: storeState.bookLendingDetails.loading,
  updating: storeState.bookLendingDetails.updating,
  updateSuccess: storeState.bookLendingDetails.updateSuccess,
});

const mapDispatchToProps = {
  getBookReservations,
  getBookLendings,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BookLendingDetailsUpdate);
