import { Menubar } from 'primereact/menubar';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getEntities } from './external-category.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { ICategory } from 'app/shared/model/category.model';
import { MenuItem } from 'primereact/menuitem';
import { transformToMenuItems } from 'app/shared/util/lxm-utils';

const ExternalNavbar: React.FC = () => {
  const dispatch = useAppDispatch();

  const getAllEntities = () => {
    dispatch(getEntities({}));
  };

  const navigate = useNavigate();

  const categories: ICategory[] = useAppSelector(state => state.externalCategory.entities);
  const defaultItems: MenuItem[] = transformToMenuItems(categories, navigate);

  useEffect(() => {
    getAllEntities();
  }, []);

  const start = (
    <div className="brand-icon">
      <a className="cursor-pointer">
        {/* <Logo /> */}
        <span>乐香喵</span>
      </a>
    </div>
  );

  const end = <></>;
  return (
    <div className="l-site-nav">
      <Menubar className="custom-menubar" model={defaultItems} start={start} />
    </div>
  );
};

export default ExternalNavbar;
