import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './book-lending.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IBookLendingDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BookLendingDetail = (props: IBookLendingDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { bookLendingEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bookLendingDetailsHeading">BookLending</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{bookLendingEntity.id}</dd>
          <dt>
            <span id="creationDate">Creation Date</span>
          </dt>
          <dd>
            {bookLendingEntity.creationDate ? (
              <TextFormat value={bookLendingEntity.creationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{bookLendingEntity.status}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{bookLendingEntity.description}</dd>
          <dt>User</dt>
          <dd>{bookLendingEntity.user ? bookLendingEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/book-lending" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/book-lending/${bookLendingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ bookLending }: IRootState) => ({
  bookLendingEntity: bookLending.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BookLendingDetail);
