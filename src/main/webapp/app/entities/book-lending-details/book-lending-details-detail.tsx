import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './book-lending-details.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IBookLendingDetailsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BookLendingDetailsDetail = (props: IBookLendingDetailsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { bookLendingDetailsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bookLendingDetailsDetailsHeading">BookLendingDetails</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{bookLendingDetailsEntity.id}</dd>
          <dt>
            <span id="dueDate">Due Date</span>
          </dt>
          <dd>
            {bookLendingDetailsEntity.dueDate ? (
              <TextFormat value={bookLendingDetailsEntity.dueDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="returnDate">Return Date</span>
          </dt>
          <dd>
            {bookLendingDetailsEntity.returnDate ? (
              <TextFormat value={bookLendingDetailsEntity.returnDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{bookLendingDetailsEntity.price}</dd>
          <dt>Book Reservation</dt>
          <dd>{bookLendingDetailsEntity.bookReservation ? bookLendingDetailsEntity.bookReservation.id : ''}</dd>
          <dt>Book Lending</dt>
          <dd>{bookLendingDetailsEntity.bookLending ? bookLendingDetailsEntity.bookLending.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/book-lending-details" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/book-lending-details/${bookLendingDetailsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ bookLendingDetails }: IRootState) => ({
  bookLendingDetailsEntity: bookLendingDetails.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BookLendingDetailsDetail);
