import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICatagory, defaultValue } from 'app/shared/model/catagory.model';

export const ACTION_TYPES = {
  FETCH_CATAGORY_LIST: 'catagory/FETCH_CATAGORY_LIST',
  FETCH_CATAGORY: 'catagory/FETCH_CATAGORY',
  CREATE_CATAGORY: 'catagory/CREATE_CATAGORY',
  UPDATE_CATAGORY: 'catagory/UPDATE_CATAGORY',
  PARTIAL_UPDATE_CATAGORY: 'catagory/PARTIAL_UPDATE_CATAGORY',
  DELETE_CATAGORY: 'catagory/DELETE_CATAGORY',
  RESET: 'catagory/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICatagory>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type CatagoryState = Readonly<typeof initialState>;

// Reducer

export default (state: CatagoryState = initialState, action): CatagoryState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CATAGORY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CATAGORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_CATAGORY):
    case REQUEST(ACTION_TYPES.UPDATE_CATAGORY):
    case REQUEST(ACTION_TYPES.DELETE_CATAGORY):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_CATAGORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_CATAGORY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CATAGORY):
    case FAILURE(ACTION_TYPES.CREATE_CATAGORY):
    case FAILURE(ACTION_TYPES.UPDATE_CATAGORY):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_CATAGORY):
    case FAILURE(ACTION_TYPES.DELETE_CATAGORY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_CATAGORY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_CATAGORY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_CATAGORY):
    case SUCCESS(ACTION_TYPES.UPDATE_CATAGORY):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_CATAGORY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_CATAGORY):
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

const apiUrl = 'api/catagories';

// Actions

export const getEntities: ICrudGetAllAction<ICatagory> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CATAGORY_LIST,
    payload: axios.get<ICatagory>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ICatagory> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CATAGORY,
    payload: axios.get<ICatagory>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ICatagory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CATAGORY,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICatagory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CATAGORY,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ICatagory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_CATAGORY,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICatagory> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CATAGORY,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
