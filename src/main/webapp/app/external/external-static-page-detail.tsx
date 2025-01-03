import React, { useState, useEffect, useRef } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import FullPageLayout from 'app/shared/layout/full-page-layout';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { getEntity } from './external-static-page.reducer';

const ExternalStaticPageDetail: React.FC = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const entity = useAppSelector(state => state.externalStaticPage.entity);
  const loading = useAppSelector(state => state.externalStaticPage.loading);
  return (
    <FullPageLayout>
      <div>导航</div>
      <div>
        <div>三级菜单</div>
        <div>四级菜单</div>
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
