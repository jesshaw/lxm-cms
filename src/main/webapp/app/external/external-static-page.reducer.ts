import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IStaticPage, defaultValue } from 'app/shared/model/static-page.model';

const initialState: EntityState<IStaticPage> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/external/static-pages';

// Actions

export const getEntities = createAsyncThunk('external/staticPage/fetch_entity_list', async ({ query, page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?${query ? `${query}&` : ''}${sort ? `page=${page}&size=${size}&sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
  return axios.get<IStaticPage[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'external/staticPage/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IStaticPage>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

// slice

export const StaticPageSlice = createEntitySlice({
  name: 'externalStaticPage',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data, headers } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
          totalItems: parseInt(headers['x-total-count'], 10),
        };
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.totalItems = 0;
        state.updateSuccess = false;
        state.loading = true;
      });
  },
});

export const { reset } = StaticPageSlice.actions;

// Reducer
export default StaticPageSlice.reducer;
