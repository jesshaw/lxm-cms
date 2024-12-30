import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
// import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Button } from 'primereact/button';
import { Tooltip } from 'primereact/tooltip';
import { Translate, translate, byteSize } from 'react-jhipster';
// import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './static-page.reducer';

export const StaticPageDetail = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const staticPageEntity = useAppSelector(state => state.staticPage.entity);

  return (
    <div className="l-card">
      <h5>
        <Translate contentKey="lxmcmsApp.staticPage.detail.title">StaticPage</Translate>
      </h5>
      <div className="l-form">
        <div>
          <label id="id" data-pr-position="top" data-pr-at="left+5 top-5" data-pr-tooltip="">
            <Translate contentKey="global.field.id">ID</Translate>
          </label>
          <Tooltip target="#id" />
          <div>{staticPageEntity.id}</div>
        </div>
        <div>
          <label id="title" data-pr-position="top" data-pr-at="left+5 top-5" data-pr-tooltip={translate('lxmcmsApp.staticPage.help.title')}>
            <Translate contentKey="lxmcmsApp.staticPage.title">Title</Translate>
          </label>
          <Tooltip target="#title" />
          <div>{staticPageEntity.title}</div>
        </div>
        <div>
          <label
            id="content"
            data-pr-position="top"
            data-pr-at="left+5 top-5"
            data-pr-tooltip={translate('lxmcmsApp.staticPage.help.content')}
          >
            <Translate contentKey="lxmcmsApp.staticPage.content">Content</Translate>
          </label>
          <Tooltip target="#content" />
          <div>{staticPageEntity.content}</div>
        </div>
        <div>
          <label
            id="status"
            data-pr-position="top"
            data-pr-at="left+5 top-5"
            data-pr-tooltip={translate('lxmcmsApp.staticPage.help.status')}
          >
            <Translate contentKey="lxmcmsApp.staticPage.status">Status</Translate>
          </label>
          <Tooltip target="#status" />
          <div>{staticPageEntity.status}</div>
        </div>
        <div>
          <label>
            <Translate contentKey="lxmcmsApp.staticPage.category">Category</Translate>
          </label>
          <div>{staticPageEntity.category ? staticPageEntity.category.name : ''}</div>
        </div>
      </div>

      <div className="l-form-footer">
        <Button label={translate('entity.action.back')} icon="pi pi-arrow-left" outlined onClick={() => navigate(-1)} />
        <Button
          label={translate('entity.action.edit')}
          icon="pi pi-save"
          onClick={() => navigate(`/static-page/${staticPageEntity.id}/edit`)}
        />
      </div>
    </div>
  );
};

export default StaticPageDetail;
