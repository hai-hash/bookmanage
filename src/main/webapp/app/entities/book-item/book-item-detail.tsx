import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './book-item.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IBookItemDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BookItemDetail = (props: IBookItemDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { bookItemEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bookItemDetailsHeading">BookItem</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{bookItemEntity.id}</dd>
          <dt>
            <span id="barcode">Barcode</span>
          </dt>
          <dd>{bookItemEntity.barcode}</dd>
          <dt>
            <span id="isReferenceOnly">Is Reference Only</span>
          </dt>
          <dd>{bookItemEntity.isReferenceOnly ? 'true' : 'false'}</dd>
          <dt>
            <span id="borrowed">Borrowed</span>
          </dt>
          <dd>
            {bookItemEntity.borrowed ? <TextFormat value={bookItemEntity.borrowed} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="dueDate">Due Date</span>
          </dt>
          <dd>
            {bookItemEntity.dueDate ? <TextFormat value={bookItemEntity.dueDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{bookItemEntity.price}</dd>
          <dt>
            <span id="format">Format</span>
          </dt>
          <dd>{bookItemEntity.format}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{bookItemEntity.status}</dd>
          <dt>
            <span id="dateOfPurchase">Date Of Purchase</span>
          </dt>
          <dd>
            {bookItemEntity.dateOfPurchase ? (
              <TextFormat value={bookItemEntity.dateOfPurchase} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="publicationDate">Publication Date</span>
          </dt>
          <dd>
            {bookItemEntity.publicationDate ? (
              <TextFormat value={bookItemEntity.publicationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="modifiedDate">Modified Date</span>
          </dt>
          <dd>
            {bookItemEntity.modifiedDate ? (
              <TextFormat value={bookItemEntity.modifiedDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>User</dt>
          <dd>{bookItemEntity.user ? bookItemEntity.user.login : ''}</dd>
          <dt>Rack</dt>
          <dd>{bookItemEntity.rack ? bookItemEntity.rack.id : ''}</dd>
          <dt>Book</dt>
          <dd>{bookItemEntity.book ? bookItemEntity.book.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/book-item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/book-item/${bookItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ bookItem }: IRootState) => ({
  bookItemEntity: bookItem.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BookItemDetail);
