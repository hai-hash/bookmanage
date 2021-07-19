import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './reader.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IReaderDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ReaderDetail = (props: IReaderDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { readerEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="readerDetailsHeading">Reader</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{readerEntity.id}</dd>
          <dt>
            <span id="phone">Phone</span>
          </dt>
          <dd>{readerEntity.phone}</dd>
          <dt>
            <span id="streetAddress">Street Address</span>
          </dt>
          <dd>{readerEntity.streetAddress}</dd>
          <dt>
            <span id="city">City</span>
          </dt>
          <dd>{readerEntity.city}</dd>
          <dt>
            <span id="state">State</span>
          </dt>
          <dd>{readerEntity.state}</dd>
          <dt>
            <span id="zipCode">Zip Code</span>
          </dt>
          <dd>{readerEntity.zipCode}</dd>
          <dt>
            <span id="country">Country</span>
          </dt>
          <dd>{readerEntity.country}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{readerEntity.status}</dd>
          <dt>
            <span id="modifiedDate">Modified Date</span>
          </dt>
          <dd>
            {readerEntity.modifiedDate ? <TextFormat value={readerEntity.modifiedDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>User</dt>
          <dd>{readerEntity.user ? readerEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/reader" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/reader/${readerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ reader }: IRootState) => ({
  readerEntity: reader.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ReaderDetail);
