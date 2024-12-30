import { Menubar } from 'primereact/menubar';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getEntities } from './external-category.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { ICategory } from 'app/shared/model/category.model';
import { MenuItem } from 'primereact/menuitem';

function transformToMenuItems(categories: ICategory[]): MenuItem[] {
  // categories.sort((a, b) => (a.sort ?? 0) - (b.sort ?? 0));
  const sortedCategories = [...categories].sort((a, b) => (a.sort ?? 0) - (b.sort ?? 0));

  const navigate = useNavigate();
  const idMap: Map<number, MenuItem> = new Map();
  const rootItems: MenuItem[] = [];
  rootItems.push({
    label: '首页',
    icon: 'pi pi-home',
    command: () => {
      navigate('/external');
    },
  });

  // Step 1: Initialize map with MenuItem for each ICategory
  sortedCategories.forEach(category => {
    if (category.id !== undefined) {
      idMap.set(category.id, {
        label: category.name ?? '',
        command: () => {
          navigate(`/external/page/${category.id}`);
        },
        // items: [], // 确保 items 是 MenuItem[] 类型
      });
    }
  });

  // Step 2: Build tree structure
  sortedCategories.forEach(category => {
    const menuItem = idMap.get(category.id!);
    if (menuItem && category.parent?.id) {
      const parentMenuItem = idMap.get(category.parent.id);
      if (parentMenuItem) {
        // 确保 items 是数组类型
        parentMenuItem.items = parentMenuItem.items || [];
        // parentMenuItem.items.push(menuItem);
        (parentMenuItem.items as MenuItem[]).push(menuItem);
      }
    } else if (menuItem) {
      // If no parent, it's a root item
      rootItems.push(menuItem);
    }
  });

  return rootItems;
}

const ExternalNavbar: React.FC = () => {
  const dispatch = useAppDispatch();

  const getAllEntities = () => {
    dispatch(getEntities({}));
  };

  useEffect(() => {
    getAllEntities();
  }, []);

  const navigate = useNavigate();

  const categoryList: ICategory[] = useAppSelector(state => state.externalCategory.entities);

  const defaultItems: MenuItem[] = transformToMenuItems(categoryList);

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
    <div>
      <Menubar className="custom-menubar" model={defaultItems} start={start} end={end} />
    </div>
  );
};

export default ExternalNavbar;
