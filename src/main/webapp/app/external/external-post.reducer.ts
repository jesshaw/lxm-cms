import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IPost, defaultValue } from 'app/shared/model/post.model';

const initialState: EntityState<IPost> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/external/posts';

// Actions

export const getEntities = createAsyncThunk('external/post/fetch_entity_list', async ({ query, page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?${query ? `${query}&` : ''}${sort ? `page=${page}&size=${size}&sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
  return axios.get<IPost[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'external/post/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IPost>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

// slice

export const PostSlice = createEntitySlice({
  name: 'externalPost',
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
        state.updateSuccess = false;
        state.loading = true;
      });
  },
});

export const { reset } = PostSlice.actions;

// Reducer
export default PostSlice.reducer;
