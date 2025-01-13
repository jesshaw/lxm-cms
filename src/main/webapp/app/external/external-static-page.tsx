import React, { useState, useEffect, useRef } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import FullPageLayout from 'app/shared/layout/full-page-layout';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { getEntities } from './external-static-page.reducer';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { getPaginationState, translate } from 'react-jhipster';
import { ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { DataView, DataViewPageEvent } from 'primereact/dataview';
import { IPost, defaultValue } from 'app/shared/model/post.model';
import { classNames } from 'primereact/utils';
import { Menu } from 'primereact/menu';
import { MenuItem } from 'primereact/menuitem';
import { BreadCrumb } from 'primereact/breadcrumb';
import { findPathToRoot, getSecondLevelMenu, homeMenuItem, renderMenuItems } from 'app/shared/util/lxm-utils';
import { ICategory } from 'app/shared/model/category.model';

const ExternalStaticPage: React.FC = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const pageLocation = useLocation();

  const { cid } = useParams<'cid'>();

  const categories: ICategory[] = useAppSelector(state => state.externalCategory.entities);
  const items: MenuItem[] = getSecondLevelMenu(categories, cid, navigate);
  const breadItems: MenuItem[] = findPathToRoot(categories, cid, navigate);

  const entities = useAppSelector(state => state.externalStaticPage.entities);
  const loading = useAppSelector(state => state.externalStaticPage.loading);
  const totalItems = useAppSelector(state => state.externalStaticPage.totalItems);
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
    console.log(cid);
    console.log(categories);
    getAllEntities();
  }, [cid]);

  useEffect(() => {
    if (totalItems == 1) {
      navigate(`/external/page/${cid}/${entities[0].id}`);
    }
  }, [totalItems]);

  return (
    <FullPageLayout>
      <div>
        <BreadCrumb model={breadItems} home={homeMenuItem(navigate)} />
      </div>
      <div className="l-site-body">
        {items && (
          <div className="l-site-sidebar">
            <Menu model={renderMenuItems(items, cid)} />
          </div>
        )}
        <div className="l-site-content">未配置内容页</div>
      </div>
    </FullPageLayout>
  );
};

export default ExternalStaticPage;
