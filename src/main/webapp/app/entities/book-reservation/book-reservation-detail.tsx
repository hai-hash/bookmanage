import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './book-reservation.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IBookReservationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BookReservationDetail = (props: IBookReservationDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { bookReservationEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bookReservationDetailsHeading">BookReservation</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{bookReservationEntity.id}</dd>
          <dt>
            <span id="creationDate">Creation Date</span>
          </dt>
          <dd>
            {bookReservationEntity.creationDate ? (
              <TextFormat value={bookReservationEntity.creationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{bookReservationEntity.status}</dd>
          <dt>Book Item</dt>
          <dd>{bookReservationEntity.bookItem ? bookReservationEntity.bookItem.id : ''}</dd>
          <dt>Reader</dt>
          <dd>{bookReservationEntity.reader ? bookReservationEntity.reader.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/book-reservation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/book-reservation/${bookReservationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ bookReservation }: IRootState) => ({
  bookReservationEntity: bookReservation.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BookReservationDetail);
