import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './book-item.reducer';
import { IBookItem } from 'app/shared/model/book-item.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export interface IBookItemProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const BookItem = (props: IBookItemProps) => {
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );

  const getAllEntities = () => {
    props.getEntities(paginationState.activePage - 1, paginationState.itemsPerPage, `${paginationState.sort},${paginationState.order}`);
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (props.location.search !== endURL) {
      props.history.push(`${props.location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(props.location.search);
    const page = params.get('page');
    const sort = params.get('sort');
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [props.location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === 'asc' ? 'desc' : 'asc',
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const { bookItemList, match, loading, totalItems } = props;
  return (
    <div>
      <h2 id="book-item-heading" data-cy="BookItemHeading">
        Book Items
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Book Item
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {bookItemList && bookItemList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('barcode')}>
                  Barcode <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('isReferenceOnly')}>
                  Is Reference Only <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('borrowed')}>
                  Borrowed <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dueDate')}>
                  Due Date <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('price')}>
                  Price <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('format')}>
                  Format <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('status')}>
                  Status <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dateOfPurchase')}>
                  Date Of Purchase <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('publicationDate')}>
                  Publication Date <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('modifiedDate')}>
                  Modified Date <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  User <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Rack <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Book <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {bookItemList.map((bookItem, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${bookItem.id}`} color="link" size="sm">
                      {bookItem.id}
                    </Button>
                  </td>
                  <td>{bookItem.barcode}</td>
                  <td>{bookItem.isReferenceOnly ? 'true' : 'false'}</td>
                  <td>{bookItem.borrowed ? <TextFormat type="date" value={bookItem.borrowed} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{bookItem.dueDate ? <TextFormat type="date" value={bookItem.dueDate} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{bookItem.price}</td>
                  <td>{bookItem.format}</td>
                  <td>{bookItem.status}</td>
                  <td>
                    {bookItem.dateOfPurchase ? (
                      <TextFormat type="date" value={bookItem.dateOfPurchase} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {bookItem.publicationDate ? (
                      <TextFormat type="date" value={bookItem.publicationDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {bookItem.modifiedDate ? <TextFormat type="date" value={bookItem.modifiedDate} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{bookItem.user ? bookItem.user.login : ''}</td>
                  <td>{bookItem.rack ? <Link to={`rack/${bookItem.rack.id}`}>{bookItem.rack.id}</Link> : ''}</td>
                  <td>{bookItem.book ? <Link to={`book/${bookItem.book.id}`}>{bookItem.book.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${bookItem.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${bookItem.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${bookItem.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Book Items found</div>
        )}
      </div>
      {props.totalItems ? (
        <div className={bookItemList && bookItemList.length > 0 ? '' : 'd-none'}>
          <Row className="justify-content-center">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
          </Row>
          <Row className="justify-content-center">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={props.totalItems}
            />
          </Row>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

const mapStateToProps = ({ bookItem }: IRootState) => ({
  bookItemList: bookItem.entities,
  loading: bookItem.loading,
  totalItems: bookItem.totalItems,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BookItem);
