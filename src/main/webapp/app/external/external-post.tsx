import React, { useState, useEffect, useRef } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import FullPageLayout from 'app/shared/layout/full-page-layout';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { getEntities } from './external-post.reducer';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { getPaginationState, translate } from 'react-jhipster';
import { ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { DataView, DataViewPageEvent } from 'primereact/dataview';
import { IPost, defaultValue } from 'app/shared/model/post.model';
import { classNames } from 'primereact/utils';

const ExternalPost: React.FC = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const pageLocation = useLocation();

  const { cid } = useParams<'cid'>();

  const postList = useAppSelector(state => state.externalPost.entities);
  const loading = useAppSelector(state => state.externalPost.loading);
  const totalItems = useAppSelector(state => state.externalPost.totalItems);
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, 2, 'id'), pageLocation.search),
  );

  const getAllEntities = () => {
    dispatch(
      getEntities({
        query: `categoryId.equals=${cid}`,
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const size = params.get('size');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        itemsPerPage: parseInt(size),
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage, paginationState.itemsPerPage, paginationState.order, paginationState.sort]);

  const listTemplate = (post: IPost) => {
    return (
      <div className="col-12" key={post.id}>
        <div className={classNames('flex flex-column xl:flex-row xl:align-items-start p-4 gap-4')}>
          {/* <img
            className="w-9 sm:w-16rem xl:w-10rem shadow-2 block xl:block mx-auto border-round"
            src={`https://primefaces.org/cdn/primereact/images/product/${product.image}`}
            alt={product.name}
          /> */}
          <div className="flex flex-column sm:flex-row justify-content-between align-items-center xl:align-items-start flex-1 gap-4">
            <div className="flex flex-column align-items-center sm:align-items-start gap-3">
              <div className="text-2xl font-bold text-900">
                <Link to={`/external/post/${cid}/${post.id}`}>{post.title}</Link>
              </div>
              {/* <Rating value={product.rating} readOnly cancel={false}></Rating> */}
              <div className="flex align-items-center gap-3">
                <span className="flex align-items-center gap-2">
                  <i className="pi pi-tag"></i>
                  <span className="font-semibold">{post.category?.name}</span>
                </span>
                {/* <Tag value={product.inventoryStatus} severity={getSeverity(product)}></Tag> */}
              </div>
            </div>
            <div className="flex sm:flex-column align-items-center sm:align-items-end gap-3 sm:gap-2">
              <span className="text-2xl font-semibold">${post.status}</span>
              {/* <Button icon="pi pi-shopping-cart" className="p-button-rounded" disabled={product.inventoryStatus === 'OUTOFSTOCK'}></Button> */}
            </div>
          </div>
        </div>
      </div>
    );
  };

  const itemTemplate = (post, layout) => {
    console.log(post);
    if (!post) {
      return;
    }

    if (layout === 'list') return listTemplate(post);
    else if (layout === 'grid') return;
  };

  const onPage = (e: DataViewPageEvent) => {
    setPaginationState({
      ...paginationState,
      activePage: e.page + 1,
    });
  };

  return (
    <FullPageLayout>
      <div>导航</div>
      <div>
        <div>多新闻列表</div>
        <div className="l-card">
          <DataView
            value={postList}
            itemTemplate={itemTemplate}
            layout={'list'}
            emptyMessage={translate('lxmcmsApp.post.home.notFound')}
            lazy
            loading={loading}
            paginator
            onPage={onPage}
            first={(paginationState.activePage - 1) * paginationState.itemsPerPage} //current page start index
            rows={paginationState.itemsPerPage}
            totalRecords={totalItems}
          />
        </div>
      </div>
    </FullPageLayout>
  );
};

export default ExternalPost;
