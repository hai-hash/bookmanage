import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IBookItem, defaultValue } from 'app/shared/model/book-item.model';

export const ACTION_TYPES = {
  FETCH_BOOKITEM_LIST: 'bookItem/FETCH_BOOKITEM_LIST',
  FETCH_BOOKITEM: 'bookItem/FETCH_BOOKITEM',
  CREATE_BOOKITEM: 'bookItem/CREATE_BOOKITEM',
  UPDATE_BOOKITEM: 'bookItem/UPDATE_BOOKITEM',
  PARTIAL_UPDATE_BOOKITEM: 'bookItem/PARTIAL_UPDATE_BOOKITEM',
  DELETE_BOOKITEM: 'bookItem/DELETE_BOOKITEM',
  RESET: 'bookItem/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IBookItem>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type BookItemState = Readonly<typeof initialState>;

// Reducer

export default (state: BookItemState = initialState, action): BookItemState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_BOOKITEM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_BOOKITEM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_BOOKITEM):
    case REQUEST(ACTION_TYPES.UPDATE_BOOKITEM):
    case REQUEST(ACTION_TYPES.DELETE_BOOKITEM):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_BOOKITEM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_BOOKITEM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_BOOKITEM):
    case FAILURE(ACTION_TYPES.CREATE_BOOKITEM):
    case FAILURE(ACTION_TYPES.UPDATE_BOOKITEM):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_BOOKITEM):
    case FAILURE(ACTION_TYPES.DELETE_BOOKITEM):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_BOOKITEM_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_BOOKITEM):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_BOOKITEM):
    case SUCCESS(ACTION_TYPES.UPDATE_BOOKITEM):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_BOOKITEM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_BOOKITEM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/book-items';

// Actions

export const getEntities: ICrudGetAllAction<IBookItem> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_BOOKITEM_LIST,
    payload: axios.get<IBookItem>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IBookItem> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_BOOKITEM,
    payload: axios.get<IBookItem>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IBookItem> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_BOOKITEM,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IBookItem> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_BOOKITEM,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IBookItem> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_BOOKITEM,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IBookItem> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_BOOKITEM,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
