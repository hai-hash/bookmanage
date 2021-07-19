import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IBookLending, defaultValue } from 'app/shared/model/book-lending.model';

export const ACTION_TYPES = {
  FETCH_BOOKLENDING_LIST: 'bookLending/FETCH_BOOKLENDING_LIST',
  FETCH_BOOKLENDING: 'bookLending/FETCH_BOOKLENDING',
  CREATE_BOOKLENDING: 'bookLending/CREATE_BOOKLENDING',
  UPDATE_BOOKLENDING: 'bookLending/UPDATE_BOOKLENDING',
  PARTIAL_UPDATE_BOOKLENDING: 'bookLending/PARTIAL_UPDATE_BOOKLENDING',
  DELETE_BOOKLENDING: 'bookLending/DELETE_BOOKLENDING',
  RESET: 'bookLending/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IBookLending>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type BookLendingState = Readonly<typeof initialState>;

// Reducer

export default (state: BookLendingState = initialState, action): BookLendingState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_BOOKLENDING_LIST):
    case REQUEST(ACTION_TYPES.FETCH_BOOKLENDING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_BOOKLENDING):
    case REQUEST(ACTION_TYPES.UPDATE_BOOKLENDING):
    case REQUEST(ACTION_TYPES.DELETE_BOOKLENDING):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_BOOKLENDING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_BOOKLENDING_LIST):
    case FAILURE(ACTION_TYPES.FETCH_BOOKLENDING):
    case FAILURE(ACTION_TYPES.CREATE_BOOKLENDING):
    case FAILURE(ACTION_TYPES.UPDATE_BOOKLENDING):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_BOOKLENDING):
    case FAILURE(ACTION_TYPES.DELETE_BOOKLENDING):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_BOOKLENDING_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_BOOKLENDING):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_BOOKLENDING):
    case SUCCESS(ACTION_TYPES.UPDATE_BOOKLENDING):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_BOOKLENDING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_BOOKLENDING):
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

const apiUrl = 'api/book-lendings';

// Actions

export const getEntities: ICrudGetAllAction<IBookLending> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_BOOKLENDING_LIST,
    payload: axios.get<IBookLending>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IBookLending> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_BOOKLENDING,
    payload: axios.get<IBookLending>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IBookLending> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_BOOKLENDING,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IBookLending> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_BOOKLENDING,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IBookLending> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_BOOKLENDING,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IBookLending> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_BOOKLENDING,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
