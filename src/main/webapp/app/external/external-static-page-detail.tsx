import React, { useState, useEffect, useRef } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import FullPageLayout from 'app/shared/layout/full-page-layout';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { getEntity } from './external-static-page.reducer';
import { Menu } from 'primereact/menu';
import { MenuItem } from 'primereact/menuitem';
import { BreadCrumb } from 'primereact/breadcrumb';
import { findPathToRoot, getSecondLevelMenu, renderMenuItems } from 'app/shared/util/lxm-utils';
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

  const items: MenuItem[] = getSecondLevelMenu(categories, cid);

  const breadItems: MenuItem[] = findPathToRoot(categories, cid);
  const home: MenuItem = {
    icon: 'pi pi-home',
    command: () => {
      navigate('/external/');
    },
  };

  return (
    <FullPageLayout>
      <div>
        <BreadCrumb model={breadItems} home={home} />
      </div>
      <div>
        <div className="">
          <Menu model={renderMenuItems(items, cid)} />
        </div>
        {/* <div>四级菜单</div> */}
        {!loading && (
          <div>
            <div>{entity.title}</div>
            <div>{entity.content}</div>
          </div>
        )}
      </div>
    </FullPageLayout>
  );
};

export default ExternalStaticPageDetail;
