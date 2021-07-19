import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IBookLendingDetails, defaultValue } from 'app/shared/model/book-lending-details.model';

export const ACTION_TYPES = {
  FETCH_BOOKLENDINGDETAILS_LIST: 'bookLendingDetails/FETCH_BOOKLENDINGDETAILS_LIST',
  FETCH_BOOKLENDINGDETAILS: 'bookLendingDetails/FETCH_BOOKLENDINGDETAILS',
  CREATE_BOOKLENDINGDETAILS: 'bookLendingDetails/CREATE_BOOKLENDINGDETAILS',
  UPDATE_BOOKLENDINGDETAILS: 'bookLendingDetails/UPDATE_BOOKLENDINGDETAILS',
  PARTIAL_UPDATE_BOOKLENDINGDETAILS: 'bookLendingDetails/PARTIAL_UPDATE_BOOKLENDINGDETAILS',
  DELETE_BOOKLENDINGDETAILS: 'bookLendingDetails/DELETE_BOOKLENDINGDETAILS',
  RESET: 'bookLendingDetails/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IBookLendingDetails>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type BookLendingDetailsState = Readonly<typeof initialState>;

// Reducer

export default (state: BookLendingDetailsState = initialState, action): BookLendingDetailsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_BOOKLENDINGDETAILS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_BOOKLENDINGDETAILS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_BOOKLENDINGDETAILS):
    case REQUEST(ACTION_TYPES.UPDATE_BOOKLENDINGDETAILS):
    case REQUEST(ACTION_TYPES.DELETE_BOOKLENDINGDETAILS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_BOOKLENDINGDETAILS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_BOOKLENDINGDETAILS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_BOOKLENDINGDETAILS):
    case FAILURE(ACTION_TYPES.CREATE_BOOKLENDINGDETAILS):
    case FAILURE(ACTION_TYPES.UPDATE_BOOKLENDINGDETAILS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_BOOKLENDINGDETAILS):
    case FAILURE(ACTION_TYPES.DELETE_BOOKLENDINGDETAILS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_BOOKLENDINGDETAILS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_BOOKLENDINGDETAILS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_BOOKLENDINGDETAILS):
    case SUCCESS(ACTION_TYPES.UPDATE_BOOKLENDINGDETAILS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_BOOKLENDINGDETAILS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_BOOKLENDINGDETAILS):
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

const apiUrl = 'api/book-lending-details';

// Actions

export const getEntities: ICrudGetAllAction<IBookLendingDetails> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_BOOKLENDINGDETAILS_LIST,
    payload: axios.get<IBookLendingDetails>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IBookLendingDetails> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_BOOKLENDINGDETAILS,
    payload: axios.get<IBookLendingDetails>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IBookLendingDetails> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_BOOKLENDINGDETAILS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IBookLendingDetails> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_BOOKLENDINGDETAILS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IBookLendingDetails> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_BOOKLENDINGDETAILS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IBookLendingDetails> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_BOOKLENDINGDETAILS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
