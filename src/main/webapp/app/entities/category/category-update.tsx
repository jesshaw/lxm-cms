import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
// import { Button, Row, Col, FormText, UncontrolledTooltip, } from 'reactstrap';
import { isNumber, Translate, translate, TranslatorContext, ValidatedField, ValidatedForm } from 'react-jhipster';
// import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useForm, Controller } from 'react-hook-form';
import { InputNumber } from 'primereact/inputnumber';
import { InputText } from 'primereact/inputtext';
import { Dropdown } from 'primereact/dropdown';
import { Button } from 'primereact/button';
import { Calendar } from 'primereact/calendar';
import { InputSwitch } from 'primereact/inputswitch';
import { InputTextarea } from 'primereact/inputtextarea';
import { classNames } from 'primereact/utils';
import dayjs from 'dayjs';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getCategories } from 'app/entities/category/category.reducer';
import { ICategory, defaultValue } from 'app/shared/model/category.model';
import { getEntity, updateEntity, createEntity, reset as resetEntity } from './category.reducer';

export const CategoryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const categories = useAppSelector(state => state.category.entities);
  const categoryEntity = useAppSelector(state => state.category.entity);
  const loading = useAppSelector(state => state.category.loading);
  const updating = useAppSelector(state => state.category.updating);
  const updateSuccess = useAppSelector(state => state.category.updateSuccess);

  const handleClose = () => {
    navigate('/category');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(resetEntity());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCategories({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const {
    control,
    formState: { errors },
    handleSubmit,
    reset,
    trigger,
  } = useForm({
    defaultValues: defaultValue,
  });

  useEffect(() => {
    if (categoryEntity) {
      // aync data update the form
      reset(categoryEntity);
    }
  }, [categoryEntity, reset, TranslatorContext.context.locale]);

  const onSubmit = data => {
    // console.log('submit data:', data);
    if (isNew) {
      dispatch(createEntity(data));
    } else {
      dispatch(updateEntity(data));
    }

    reset();
  };

  const getFormErrorMessage = name => {
    return errors[name] && <small className="p-error">{errors[name].message}</small>;
  };

  return (
    <div className="l-card">
      {loading ? (
        <p>Loading...</p>
      ) : (
        <>
          <h5>
            <Translate contentKey="lxmcmsApp.category.home.createOrEditLabel">Create or edit a Category</Translate>
          </h5>

          <form onSubmit={handleSubmit(onSubmit)}>
            <div className="l-form">
              {!isNew && (
                <div>
                  <label htmlFor="id">
                    <Translate contentKey="global.field.id" />
                  </label>
                  <div>
                    <Controller
                      control={control}
                      name="id"
                      render={({ field }) => (
                        <InputNumber id={field.name} onChange={e => field.onChange(e.value)} value={field.value} disabled />
                      )}
                    />
                  </div>
                </div>
              )}
              <div>
                <label htmlFor="name">
                  <Translate contentKey="lxmcmsApp.category.name" />
                </label>
                <div>
                  <Controller
                    control={control}
                    name="name"
                    rules={{
                      maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                    }}
                    render={({ field, fieldState }) => (
                      <InputText
                        id={field.name}
                        {...field}
                        value={field.value ? field.value : ''}
                        onBlur={() => {
                          trigger('name');
                        }}
                        className={classNames({
                          'p-invalid': fieldState.invalid,
                        })}
                        tooltipOptions={{ position: 'top' }}
                        tooltip={translate('lxmcmsApp.category.help.name')}
                      />
                    )}
                  />
                  {getFormErrorMessage('name')}
                </div>
              </div>
              <div>
                <label htmlFor="contentType">
                  <Translate contentKey="lxmcmsApp.category.contentType" />
                </label>
                <div>
                  <Controller
                    control={control}
                    name="contentType"
                    rules={{
                      maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                    }}
                    render={({ field, fieldState }) => (
                      <InputText
                        id={field.name}
                        {...field}
                        value={field.value ? field.value : ''}
                        onBlur={() => {
                          trigger('contentType');
                        }}
                        className={classNames({
                          'p-invalid': fieldState.invalid,
                        })}
                        tooltipOptions={{ position: 'top' }}
                        tooltip={translate('lxmcmsApp.category.help.contentType')}
                      />
                    )}
                  />
                  {getFormErrorMessage('contentType')}
                </div>
              </div>
              <div>
                <label htmlFor="sort">
                  <Translate contentKey="lxmcmsApp.category.sort" />
                </label>
                <div>
                  <Controller
                    control={control}
                    name="sort"
                    render={({ field, fieldState }) => (
                      <InputNumber
                        id={field.name}
                        onChange={e => field.onChange(e.value)}
                        value={field.value}
                        onBlur={() => {
                          trigger('sort');
                        }}
                        className={classNames({
                          'p-invalid': fieldState.invalid,
                        })}
                        tooltipOptions={{ position: 'top' }}
                        tooltip={translate('lxmcmsApp.category.help.sort')}
                      />
                    )}
                  />
                  {getFormErrorMessage('sort')}
                </div>
              </div>

              <div>
                <label htmlFor="parent">
                  <Translate contentKey="lxmcmsApp.category.parent" />
                </label>
                <div>
                  <Controller
                    control={control}
                    name="parent"
                    render={({ field, fieldState }) => (
                      <Dropdown
                        id={field.name}
                        value={field?.value?.id}
                        onChange={e => field.onChange(categories.find(it => it.id === e.value))}
                        options={categories}
                        optionValue="id"
                        optionLabel="name"
                        showClear
                        onBlur={() => {
                          trigger('parent');
                        }}
                        className={classNames({
                          'p-invalid': fieldState.invalid,
                        })}
                        tooltipOptions={{ position: 'top' }}
                        tooltip={translate('lxmcmsApp.category.help.parent')}
                      />
                    )}
                  />
                </div>
              </div>
            </div>

            <div className="l-form-footer">
              <Button type="button" label={translate('entity.action.back')} icon="pi pi-arrow-left" outlined onClick={() => navigate(-1)} />
              <Button type="submit" label={translate('entity.action.save')} icon="pi pi-save" disabled={updating} />
            </div>
          </form>
        </>
      )}
    </div>
  );
};

export default CategoryUpdate;
