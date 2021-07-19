import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IBookReservation, defaultValue } from 'app/shared/model/book-reservation.model';

export const ACTION_TYPES = {
  FETCH_BOOKRESERVATION_LIST: 'bookReservation/FETCH_BOOKRESERVATION_LIST',
  FETCH_BOOKRESERVATION: 'bookReservation/FETCH_BOOKRESERVATION',
  CREATE_BOOKRESERVATION: 'bookReservation/CREATE_BOOKRESERVATION',
  UPDATE_BOOKRESERVATION: 'bookReservation/UPDATE_BOOKRESERVATION',
  PARTIAL_UPDATE_BOOKRESERVATION: 'bookReservation/PARTIAL_UPDATE_BOOKRESERVATION',
  DELETE_BOOKRESERVATION: 'bookReservation/DELETE_BOOKRESERVATION',
  RESET: 'bookReservation/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IBookReservation>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type BookReservationState = Readonly<typeof initialState>;

// Reducer

export default (state: BookReservationState = initialState, action): BookReservationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_BOOKRESERVATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_BOOKRESERVATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_BOOKRESERVATION):
    case REQUEST(ACTION_TYPES.UPDATE_BOOKRESERVATION):
    case REQUEST(ACTION_TYPES.DELETE_BOOKRESERVATION):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_BOOKRESERVATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_BOOKRESERVATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_BOOKRESERVATION):
    case FAILURE(ACTION_TYPES.CREATE_BOOKRESERVATION):
    case FAILURE(ACTION_TYPES.UPDATE_BOOKRESERVATION):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_BOOKRESERVATION):
    case FAILURE(ACTION_TYPES.DELETE_BOOKRESERVATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_BOOKRESERVATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_BOOKRESERVATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_BOOKRESERVATION):
    case SUCCESS(ACTION_TYPES.UPDATE_BOOKRESERVATION):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_BOOKRESERVATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_BOOKRESERVATION):
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

const apiUrl = 'api/book-reservations';

// Actions

export const getEntities: ICrudGetAllAction<IBookReservation> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_BOOKRESERVATION_LIST,
    payload: axios.get<IBookReservation>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IBookReservation> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_BOOKRESERVATION,
    payload: axios.get<IBookReservation>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IBookReservation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_BOOKRESERVATION,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IBookReservation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_BOOKRESERVATION,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IBookReservation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_BOOKRESERVATION,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IBookReservation> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_BOOKRESERVATION,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
