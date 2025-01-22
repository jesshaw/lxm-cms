import React, { useState, useEffect, useRef } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import FullPageLayout from 'app/shared/layout/full-page-layout';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { getEntity } from './external-static-page.reducer';
import { Menu } from 'primereact/menu';
import { MenuItem } from 'primereact/menuitem';
import { BreadCrumb } from 'primereact/breadcrumb';
import { findPathToRoot, getSecondLevelMenu, homeMenuItem, renderMenuItems } from 'app/shared/util/lxm-utils';
import { ICategory } from 'app/shared/model/category.model';

const ExternalStaticPageDetail: React.FC = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const { cid } = useParams<'cid'>();
  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const entity = useAppSelector(state => state.externalStaticPage.entity);
  const loading = useAppSelector(state => state.externalStaticPage.loading);

  const categories: ICategory[] = useAppSelector(state => state.externalCategory.entities);
  const items: MenuItem[] = getSecondLevelMenu(categories, cid, navigate);
  const breadItems: MenuItem[] = findPathToRoot(categories, cid, navigate);

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
        <div className="l-site-content">
          {!loading && (
            <>
              <h1>{entity.title}</h1>
              <div dangerouslySetInnerHTML={{ __html: entity.content }} />
            </>
          )}
        </div>
      </div>
    </FullPageLayout>
  );
};

export default ExternalStaticPageDetail;
