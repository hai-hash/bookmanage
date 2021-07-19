import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './rack.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRackDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RackDetail = (props: IRackDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { rackEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="rackDetailsHeading">Rack</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{rackEntity.id}</dd>
          <dt>
            <span id="number">Number</span>
          </dt>
          <dd>{rackEntity.number}</dd>
          <dt>
            <span id="locationIdentifier">Location Identifier</span>
          </dt>
          <dd>{rackEntity.locationIdentifier}</dd>
          <dt>
            <span id="modifiedDate">Modified Date</span>
          </dt>
          <dd>
            {rackEntity.modifiedDate ? <TextFormat value={rackEntity.modifiedDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="isActive">Is Active</span>
          </dt>
          <dd>{rackEntity.isActive ? 'true' : 'false'}</dd>
          <dt>User</dt>
          <dd>{rackEntity.user ? rackEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/rack" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/rack/${rackEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ rack }: IRootState) => ({
  rackEntity: rack.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RackDetail);
