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
import { IRack } from 'app/shared/model/rack.model';
import { getEntities as getRacks } from 'app/entities/rack/rack.reducer';
import { IBook } from 'app/shared/model/book.model';
import { getEntities as getBooks } from 'app/entities/book/book.reducer';
import { IBookReservation } from 'app/shared/model/book-reservation.model';
import { getEntities as getBookReservations } from 'app/entities/book-reservation/book-reservation.reducer';
import { getEntity, updateEntity, createEntity, reset } from './book-item.reducer';
import { IBookItem } from 'app/shared/model/book-item.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IBookItemUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BookItemUpdate = (props: IBookItemUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { bookItemEntity, users, racks, books, bookReservations, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/book-item' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getUsers();
    props.getRacks();
    props.getBooks();
    props.getBookReservations();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...bookItemEntity,
        ...values,
        user: users.find(it => it.id.toString() === values.userId.toString()),
        rack: racks.find(it => it.id.toString() === values.rackId.toString()),
        book: books.find(it => it.id.toString() === values.bookId.toString()),
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
          <h2 id="bookManageApp.bookItem.home.createOrEditLabel" data-cy="BookItemCreateUpdateHeading">
            Create or edit a BookItem
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : bookItemEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="book-item-id">ID</Label>
                  <AvInput id="book-item-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="barcodeLabel" for="book-item-barcode">
                  Barcode
                </Label>
                <AvField id="book-item-barcode" data-cy="barcode" type="text" name="barcode" />
              </AvGroup>
              <AvGroup check>
                <Label id="isReferenceOnlyLabel">
                  <AvInput
                    id="book-item-isReferenceOnly"
                    data-cy="isReferenceOnly"
                    type="checkbox"
                    className="form-check-input"
                    name="isReferenceOnly"
                  />
                  Is Reference Only
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="borrowedLabel" for="book-item-borrowed">
                  Borrowed
                </Label>
                <AvField id="book-item-borrowed" data-cy="borrowed" type="date" className="form-control" name="borrowed" />
              </AvGroup>
              <AvGroup>
                <Label id="dueDateLabel" for="book-item-dueDate">
                  Due Date
                </Label>
                <AvField id="book-item-dueDate" data-cy="dueDate" type="date" className="form-control" name="dueDate" />
              </AvGroup>
              <AvGroup>
                <Label id="priceLabel" for="book-item-price">
                  Price
                </Label>
                <AvField id="book-item-price" data-cy="price" type="text" name="price" />
              </AvGroup>
              <AvGroup>
                <Label id="formatLabel" for="book-item-format">
                  Format
                </Label>
                <AvInput
                  id="book-item-format"
                  data-cy="format"
                  type="select"
                  className="form-control"
                  name="format"
                  value={(!isNew && bookItemEntity.format) || 'HARDCOVER'}
                >
                  <option value="HARDCOVER">HARDCOVER</option>
                  <option value="PAPERBACK">PAPERBACK</option>
                  <option value="AUDIO_BOOK">AUDIO_BOOK</option>
                  <option value="EBOOK">EBOOK</option>
                  <option value="NEWSPAPER">NEWSPAPER</option>
                  <option value="MAGAZINE">MAGAZINE</option>
                  <option value="JOURNAL">JOURNAL</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="book-item-status">
                  Status
                </Label>
                <AvInput
                  id="book-item-status"
                  data-cy="status"
                  type="select"
                  className="form-control"
                  name="status"
                  value={(!isNew && bookItemEntity.status) || 'AVAILABLE'}
                >
                  <option value="AVAILABLE">AVAILABLE</option>
                  <option value="RESERVED">RESERVED</option>
                  <option value="LOANED">LOANED</option>
                  <option value="LOST">LOST</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="dateOfPurchaseLabel" for="book-item-dateOfPurchase">
                  Date Of Purchase
                </Label>
                <AvField
                  id="book-item-dateOfPurchase"
                  data-cy="dateOfPurchase"
                  type="date"
                  className="form-control"
                  name="dateOfPurchase"
                />
              </AvGroup>
              <AvGroup>
                <Label id="publicationDateLabel" for="book-item-publicationDate">
                  Publication Date
                </Label>
                <AvField
                  id="book-item-publicationDate"
                  data-cy="publicationDate"
                  type="date"
                  className="form-control"
                  name="publicationDate"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="modifiedDateLabel" for="book-item-modifiedDate">
                  Modified Date
                </Label>
                <AvField
                  id="book-item-modifiedDate"
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
                <Label for="book-item-user">User</Label>
                <AvInput id="book-item-user" data-cy="user" type="select" className="form-control" name="userId">
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
                <Label for="book-item-rack">Rack</Label>
                <AvInput id="book-item-rack" data-cy="rack" type="select" className="form-control" name="rackId">
                  <option value="" key="0" />
                  {racks
                    ? racks.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="book-item-book">Book</Label>
                <AvInput id="book-item-book" data-cy="book" type="select" className="form-control" name="bookId">
                  <option value="" key="0" />
                  {books
                    ? books.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/book-item" replace color="info">
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
  racks: storeState.rack.entities,
  books: storeState.book.entities,
  bookReservations: storeState.bookReservation.entities,
  bookItemEntity: storeState.bookItem.entity,
  loading: storeState.bookItem.loading,
  updating: storeState.bookItem.updating,
  updateSuccess: storeState.bookItem.updateSuccess,
});

const mapDispatchToProps = {
  getUsers,
  getRacks,
  getBooks,
  getBookReservations,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BookItemUpdate);
