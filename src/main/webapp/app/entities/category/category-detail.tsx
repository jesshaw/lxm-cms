import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
// import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Button } from 'primereact/button';
import { Tooltip } from 'primereact/tooltip';
import { Translate, translate } from 'react-jhipster';
// import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './category.reducer';

export const CategoryDetail = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const categoryEntity = useAppSelector(state => state.category.entity);

  return (
    <div className="l-card">
      <h5>
        <Translate contentKey="lxmcmsApp.category.detail.title">Category</Translate>
      </h5>
      <div className="l-form">
        <div>
          <label id="id" data-pr-position="top" data-pr-at="left+5 top-5" data-pr-tooltip="">
            <Translate contentKey="global.field.id">ID</Translate>
          </label>
          <Tooltip target="#id" />
          <div>{categoryEntity.id}</div>
        </div>
        <div>
          <label id="name" data-pr-position="top" data-pr-at="left+5 top-5" data-pr-tooltip={translate('lxmcmsApp.category.help.name')}>
            <Translate contentKey="lxmcmsApp.category.name">Name</Translate>
          </label>
          <Tooltip target="#name" />
          <div>{categoryEntity.name}</div>
        </div>
        <div>
          <label
            id="contentType"
            data-pr-position="top"
            data-pr-at="left+5 top-5"
            data-pr-tooltip={translate('lxmcmsApp.category.help.contentType')}
          >
            <Translate contentKey="lxmcmsApp.category.contentType">Content Type</Translate>
          </label>
          <Tooltip target="#contentType" />
          <div>{categoryEntity.contentType}</div>
        </div>
        <div>
          <label id="sort" data-pr-position="top" data-pr-at="left+5 top-5" data-pr-tooltip={translate('lxmcmsApp.category.help.sort')}>
            <Translate contentKey="lxmcmsApp.category.sort">Sort</Translate>
          </label>
          <Tooltip target="#sort" />
          <div>{categoryEntity.sort}</div>
        </div>
        <div>
          <label>
            <Translate contentKey="lxmcmsApp.category.parent">Parent</Translate>
          </label>
          <div>{categoryEntity.parent ? categoryEntity.parent.name : ''}</div>
        </div>
      </div>

      <div className="l-form-footer">
        <Button label={translate('entity.action.back')} icon="pi pi-arrow-left" outlined onClick={() => navigate(-1)} />
        <Button label={translate('entity.action.edit')} icon="pi pi-save" onClick={() => navigate(`/category/${categoryEntity.id}/edit`)} />
      </div>
    </div>
  );
};

export default CategoryDetail;
