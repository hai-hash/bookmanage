import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './author.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAuthorDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AuthorDetail = (props: IAuthorDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { authorEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="authorDetailsHeading">Author</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{authorEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{authorEntity.name}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{authorEntity.description}</dd>
          <dt>
            <span id="modifiedDate">Modified Date</span>
          </dt>
          <dd>
            {authorEntity.modifiedDate ? <TextFormat value={authorEntity.modifiedDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="isActive">Is Active</span>
          </dt>
          <dd>{authorEntity.isActive ? 'true' : 'false'}</dd>
          <dt>User</dt>
          <dd>{authorEntity.user ? authorEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/author" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/author/${authorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ author }: IRootState) => ({
  authorEntity: author.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AuthorDetail);
