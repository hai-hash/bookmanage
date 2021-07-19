import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './catagory.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICatagoryDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CatagoryDetail = (props: ICatagoryDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { catagoryEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="catagoryDetailsHeading">Catagory</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{catagoryEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{catagoryEntity.name}</dd>
          <dt>
            <span id="modifiedDate">Modified Date</span>
          </dt>
          <dd>
            {catagoryEntity.modifiedDate ? (
              <TextFormat value={catagoryEntity.modifiedDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="isActive">Is Active</span>
          </dt>
          <dd>{catagoryEntity.isActive ? 'true' : 'false'}</dd>
          <dt>User</dt>
          <dd>{catagoryEntity.user ? catagoryEntity.user.login : ''}</dd>
          <dt>Catalog</dt>
          <dd>{catagoryEntity.catalog ? catagoryEntity.catalog.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/catagory" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/catagory/${catagoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ catagory }: IRootState) => ({
  catagoryEntity: catagory.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CatagoryDetail);
