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
import { ICatagory } from 'app/shared/model/catagory.model';
import { getEntities as getCatagories } from 'app/entities/catagory/catagory.reducer';
import { IPublisher } from 'app/shared/model/publisher.model';
import { getEntities as getPublishers } from 'app/entities/publisher/publisher.reducer';
import { IAuthor } from 'app/shared/model/author.model';
import { getEntities as getAuthors } from 'app/entities/author/author.reducer';
import { getEntity, updateEntity, createEntity, reset } from './book.reducer';
import { IBook } from 'app/shared/model/book.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IBookUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BookUpdate = (props: IBookUpdateProps) => {
  const [idsauthor, setIdsauthor] = useState([]);
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { bookEntity, users, catagories, publishers, authors, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/book' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getUsers();
    props.getCatagories();
    props.getPublishers();
    props.getAuthors();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...bookEntity,
        ...values,
        authors: mapIdList(values.authors),
        user: users.find(it => it.id.toString() === values.userId.toString()),
        catagory: catagories.find(it => it.id.toString() === values.catagoryId.toString()),
        publisher: publishers.find(it => it.id.toString() === values.publisherId.toString()),
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
          <h2 id="bookManageApp.book.home.createOrEditLabel" data-cy="BookCreateUpdateHeading">
            Create or edit a Book
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : bookEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="book-id">ID</Label>
                  <AvInput id="book-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="isbnLabel" for="book-isbn">
                  Isbn
                </Label>
                <AvField
                  id="book-isbn"
                  data-cy="isbn"
                  type="text"
                  name="isbn"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="titleLabel" for="book-title">
                  Title
                </Label>
                <AvField
                  id="book-title"
                  data-cy="title"
                  type="text"
                  name="title"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    maxLength: { value: 255, errorMessage: 'This field cannot be longer than 255 characters.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="subjectLabel" for="book-subject">
                  Subject
                </Label>
                <AvField
                  id="book-subject"
                  data-cy="subject"
                  type="text"
                  name="subject"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    maxLength: { value: 4000, errorMessage: 'This field cannot be longer than 4000 characters.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="targetLabel" for="book-target">
                  Target
                </Label>
                <AvField
                  id="book-target"
                  data-cy="target"
                  type="text"
                  name="target"
                  validate={{
                    maxLength: { value: 4000, errorMessage: 'This field cannot be longer than 4000 characters.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="languageLabel" for="book-language">
                  Language
                </Label>
                <AvField
                  id="book-language"
                  data-cy="language"
                  type="text"
                  name="language"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="numberOfPagesLabel" for="book-numberOfPages">
                  Number Of Pages
                </Label>
                <AvField
                  id="book-numberOfPages"
                  data-cy="numberOfPages"
                  type="string"
                  className="form-control"
                  name="numberOfPages"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="imageUrlLabel" for="book-imageUrl">
                  Image Url
                </Label>
                <AvField id="book-imageUrl" data-cy="imageUrl" type="text" name="imageUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="modifiedDateLabel" for="book-modifiedDate">
                  Modified Date
                </Label>
                <AvField
                  id="book-modifiedDate"
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
                <Label for="book-user">User</Label>
                <AvInput id="book-user" data-cy="user" type="select" className="form-control" name="userId">
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
                <Label for="book-catagory">Catagory</Label>
                <AvInput id="book-catagory" data-cy="catagory" type="select" className="form-control" name="catagoryId">
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
              <AvGroup>
                <Label for="book-publisher">Publisher</Label>
                <AvInput id="book-publisher" data-cy="publisher" type="select" className="form-control" name="publisherId">
                  <option value="" key="0" />
                  {publishers
                    ? publishers.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="book-author">Author</Label>
                <AvInput
                  id="book-author"
                  data-cy="author"
                  type="select"
                  multiple
                  className="form-control"
                  name="authors"
                  value={!isNew && bookEntity.authors && bookEntity.authors.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {authors
                    ? authors.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/book" replace color="info">
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
  publishers: storeState.publisher.entities,
  authors: storeState.author.entities,
  bookEntity: storeState.book.entity,
  loading: storeState.book.loading,
  updating: storeState.book.updating,
  updateSuccess: storeState.book.updateSuccess,
});

const mapDispatchToProps = {
  getUsers,
  getCatagories,
  getPublishers,
  getAuthors,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BookUpdate);
