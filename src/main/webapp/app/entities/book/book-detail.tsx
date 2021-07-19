import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './book.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IBookDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BookDetail = (props: IBookDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { bookEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bookDetailsHeading">Book</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{bookEntity.id}</dd>
          <dt>
            <span id="isbn">Isbn</span>
          </dt>
          <dd>{bookEntity.isbn}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{bookEntity.title}</dd>
          <dt>
            <span id="subject">Subject</span>
          </dt>
          <dd>{bookEntity.subject}</dd>
          <dt>
            <span id="target">Target</span>
          </dt>
          <dd>{bookEntity.target}</dd>
          <dt>
            <span id="language">Language</span>
          </dt>
          <dd>{bookEntity.language}</dd>
          <dt>
            <span id="numberOfPages">Number Of Pages</span>
          </dt>
          <dd>{bookEntity.numberOfPages}</dd>
          <dt>
            <span id="imageUrl">Image Url</span>
          </dt>
          <dd>{bookEntity.imageUrl}</dd>
          <dt>
            <span id="modifiedDate">Modified Date</span>
          </dt>
          <dd>
            {bookEntity.modifiedDate ? <TextFormat value={bookEntity.modifiedDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>User</dt>
          <dd>{bookEntity.user ? bookEntity.user.login : ''}</dd>
          <dt>Catagory</dt>
          <dd>{bookEntity.catagory ? bookEntity.catagory.id : ''}</dd>
          <dt>Publisher</dt>
          <dd>{bookEntity.publisher ? bookEntity.publisher.id : ''}</dd>
          <dt>Author</dt>
          <dd>
            {bookEntity.authors
              ? bookEntity.authors.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {bookEntity.authors && i === bookEntity.authors.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/book" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/book/${bookEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ book }: IRootState) => ({
  bookEntity: book.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BookDetail);
