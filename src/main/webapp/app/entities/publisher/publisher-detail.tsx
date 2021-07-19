import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './publisher.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPublisherDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PublisherDetail = (props: IPublisherDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { publisherEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="publisherDetailsHeading">Publisher</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{publisherEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{publisherEntity.name}</dd>
          <dt>
            <span id="modifiedDate">Modified Date</span>
          </dt>
          <dd>
            {publisherEntity.modifiedDate ? (
              <TextFormat value={publisherEntity.modifiedDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="isActive">Is Active</span>
          </dt>
          <dd>{publisherEntity.isActive ? 'true' : 'false'}</dd>
          <dt>User</dt>
          <dd>{publisherEntity.user ? publisherEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/publisher" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/publisher/${publisherEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ publisher }: IRootState) => ({
  publisherEntity: publisher.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PublisherDetail);
