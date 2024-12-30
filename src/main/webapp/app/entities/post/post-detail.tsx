import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
// import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Button } from 'primereact/button';
import { Tooltip } from 'primereact/tooltip';
import { Translate, translate, byteSize } from 'react-jhipster';
// import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './post.reducer';

export const PostDetail = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const postEntity = useAppSelector(state => state.post.entity);

  return (
    <div className="l-card">
      <h5>
        <Translate contentKey="lxmcmsApp.post.detail.title">Post</Translate>
      </h5>
      <div className="l-form">
        <div>
          <label id="id" data-pr-position="top" data-pr-at="left+5 top-5" data-pr-tooltip="">
            <Translate contentKey="global.field.id">ID</Translate>
          </label>
          <Tooltip target="#id" />
          <div>{postEntity.id}</div>
        </div>
        <div>
          <label id="title" data-pr-position="top" data-pr-at="left+5 top-5" data-pr-tooltip={translate('lxmcmsApp.post.help.title')}>
            <Translate contentKey="lxmcmsApp.post.title">Title</Translate>
          </label>
          <Tooltip target="#title" />
          <div>{postEntity.title}</div>
        </div>
        <div>
          <label id="content" data-pr-position="top" data-pr-at="left+5 top-5" data-pr-tooltip={translate('lxmcmsApp.post.help.content')}>
            <Translate contentKey="lxmcmsApp.post.content">Content</Translate>
          </label>
          <Tooltip target="#content" />
          <div>{postEntity.content}</div>
        </div>
        <div>
          <label id="status" data-pr-position="top" data-pr-at="left+5 top-5" data-pr-tooltip={translate('lxmcmsApp.post.help.status')}>
            <Translate contentKey="lxmcmsApp.post.status">Status</Translate>
          </label>
          <Tooltip target="#status" />
          <div>{postEntity.status}</div>
        </div>
        <div>
          <label>
            <Translate contentKey="lxmcmsApp.post.category">Category</Translate>
          </label>
          <div>{postEntity.category ? postEntity.category.name : ''}</div>
        </div>
      </div>

      <div className="l-form-footer">
        <Button label={translate('entity.action.back')} icon="pi pi-arrow-left" outlined onClick={() => navigate(-1)} />
        <Button label={translate('entity.action.edit')} icon="pi pi-save" onClick={() => navigate(`/post/${postEntity.id}/edit`)} />
      </div>
    </div>
  );
};

export default PostDetail;
