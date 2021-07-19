import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IReader, defaultValue } from 'app/shared/model/reader.model';

export const ACTION_TYPES = {
  FETCH_READER_LIST: 'reader/FETCH_READER_LIST',
  FETCH_READER: 'reader/FETCH_READER',
  CREATE_READER: 'reader/CREATE_READER',
  UPDATE_READER: 'reader/UPDATE_READER',
  PARTIAL_UPDATE_READER: 'reader/PARTIAL_UPDATE_READER',
  DELETE_READER: 'reader/DELETE_READER',
  RESET: 'reader/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IReader>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ReaderState = Readonly<typeof initialState>;

// Reducer

export default (state: ReaderState = initialState, action): ReaderState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_READER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_READER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_READER):
    case REQUEST(ACTION_TYPES.UPDATE_READER):
    case REQUEST(ACTION_TYPES.DELETE_READER):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_READER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_READER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_READER):
    case FAILURE(ACTION_TYPES.CREATE_READER):
    case FAILURE(ACTION_TYPES.UPDATE_READER):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_READER):
    case FAILURE(ACTION_TYPES.DELETE_READER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_READER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_READER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_READER):
    case SUCCESS(ACTION_TYPES.UPDATE_READER):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_READER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_READER):
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

const apiUrl = 'api/readers';

// Actions

export const getEntities: ICrudGetAllAction<IReader> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_READER_LIST,
    payload: axios.get<IReader>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IReader> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_READER,
    payload: axios.get<IReader>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IReader> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_READER,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IReader> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_READER,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IReader> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_READER,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IReader> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_READER,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
