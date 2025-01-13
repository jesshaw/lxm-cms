import * as React from 'react';
import dayjs from 'dayjs';
import { FilterMatchMode } from 'primereact/api';
import { Calendar } from 'primereact/calendar';
import { DataTableFilterMeta, DataTableFilterMetaData, DataTableOperatorFilterMetaData } from 'primereact/datatable';
import { InputNumber } from 'primereact/inputnumber';
import { TriStateCheckbox } from 'primereact/tristatecheckbox';
import { Slider } from 'primereact/slider';
import { Button } from 'reactstrap';
import { ColumnProps } from 'primereact/column';
import { ICategory } from '../model/category.model';
import { MenuItem } from 'primereact/menuitem';
import { useNavigate } from 'react-router';

export interface LxmColumnProps extends ColumnProps {
  headerKey?: string; // for translate
}

export const convertFiltersToQueryString = (filters: DataTableFilterMeta): string => {
  const params = new URLSearchParams();

  Object.entries(filters).forEach(([field, filter]) => {
    // 判断字段是否为 DataTableOperatorFilterMetaData 类型
    if ((filter as DataTableOperatorFilterMetaData).constraints) {
      const operatorFilter = filter as DataTableOperatorFilterMetaData;
      operatorFilter.constraints.forEach((constraint, index) => {
        if (constraint.value != null) {
          const filterOptions = convertField(field, constraint as DataTableFilterMetaData);
          // console.log(filterOption);
          filterOptions.forEach(item => {
            params.append(item?.field, item?.value);
          });
        }
      });
    } else if ((filter as DataTableFilterMetaData).value != null) {
      const filterOptions = convertField(field, filter as DataTableFilterMetaData);
      // console.log(filterOption);
      filterOptions.forEach(item => {
        params.append(item?.field, item?.value);
      });
    }
  });

  return params.toString();
};

const convertField = (field: string, filter: DataTableFilterMetaData) => {
  switch (filter.matchMode) {
    case FilterMatchMode.CONTAINS:
      return [{ field: `${field}.contains`, value: `%${filter.value}%` }];
    case FilterMatchMode.STARTS_WITH:
      return [{ field: `${field}.contains`, value: `${filter.value}%` }];
    case FilterMatchMode.ENDS_WITH:
      return [{ field: `${field}.contains`, value: `%${filter.value}` }];
    case FilterMatchMode.NOT_CONTAINS:
      return [{ field: `${field}.doesNotContain`, value: `%${filter.value}%` }];
    case FilterMatchMode.EQUALS:
      return [{ field: `${field}.equals`, value: filter.value }];
    case FilterMatchMode.NOT_EQUALS:
      return [{ field: `${field}.notEquals`, value: filter.value }];
    case FilterMatchMode.LESS_THAN:
      return [{ field: `${field}.lessThan`, value: filter.value }];
    case FilterMatchMode.LESS_THAN_OR_EQUAL_TO:
      return [{ field: `${field}.lessThanOrEqual`, value: filter.value }];
    case FilterMatchMode.GREATER_THAN:
      return [{ field: `${field}.greaterThan`, value: filter.value }];
    case FilterMatchMode.GREATER_THAN_OR_EQUAL_TO:
      return [{ field: `${field}.greaterThanOrEqual`, value: filter.value }];
    case FilterMatchMode.DATE_IS:
      return [{ field: `${field}.equals`, value: dayjs(filter.value).format('YYYY-MM-DD') }];
    case FilterMatchMode.DATE_IS_NOT:
      return [{ field: `${field}.notEquals`, value: dayjs(filter.value).format('YYYY-MM-DD') }];
    case FilterMatchMode.DATE_BEFORE:
      return [{ field: `${field}.lessThan`, value: dayjs(filter.value).format('YYYY-MM-DD') }];
    case FilterMatchMode.DATE_AFTER:
      return [{ field: `${field}.greaterThan`, value: dayjs(filter.value).format('YYYY-MM-DD') }];
    case FilterMatchMode.IN:
      return [{ field: `${field}.in`, value: filter.value }];
    case FilterMatchMode.NOT_IN:
      return [{ field: `${field}.notIn`, value: filter.value }];
    case FilterMatchMode.BETWEEN:
      return [
        { field: `${field}.greaterThanOrEqual`, value: filter.value[0] },
        { field: `${field}.lessThanOrEqual`, value: filter.value[1] },
      ];
    default:
      return null;
  }
};

const filterClearTemplate = options => {
  return <Button type="button" icon="pi pi-times" onClick={options.filterClearCallback} className="p-button-secondary"></Button>;
};

export const dateFilterTemplate = options => {
  return (
    <Calendar
      value={options.value}
      onChange={e => options.filterCallback(e.value, options.index)}
      dateFormat="yy-mm-dd"
      placeholder="yyyy-mm-dd"
      mask="9999-99-99"
    />
  );
};

export const numericFilterTemplate = options => {
  return (
    <InputNumber
      useGrouping={false}
      min={1}
      minFractionDigits={0} // 最小小数位数为 0
      maxFractionDigits={0} // 最大小数位数为 0
      value={options.value}
      onChange={e => options.filterCallback(e.value, options.index)}
    />
  );
};

export const booleanFilterTemplate = options => {
  return <TriStateCheckbox value={options.value} onChange={e => options.filterCallback(e.value)} />;
};

// MultiSelect sample by users: [{id:1,login:'admin'},{id:1,login:'user'}]
// const assignNameFilterTemplate = options => {
//   return (
//     <MultiSelect
//       value={options.value ?? ''}
//       options={users}
//       onChange={e => options.filterCallback(e.value)}
//       optionLabel="login"
//       optionValue="id"
//       placeholder="Any"
//       className="p-column-filter"
//     />
//   );
// };

// percent filter
const percentFilterTemplate = options => {
  return (
    <React.Fragment>
      <Slider value={options.value} onChange={e => options.filterCallback(e.value)} range className="m-3"></Slider>
      <div className="align-items-center justify-content-between flex px-2">
        <span>{options.value ? options.value[0] : 0}</span>
        <span>{options.value ? options.value[1] : 100}</span>
      </div>
    </React.Fragment>
  );
};

export const transformToMenuItems = (categories: ICategory[], navigate: Function): MenuItem[] => {
  // categories.sort((a, b) => (a.sort ?? 0) - (b.sort ?? 0));
  const sortedCategories = [...categories].sort((a, b) => (a.sort ?? 0) - (b.sort ?? 0));

  // const navigate = useNavigate();
  const idMap: Map<number, MenuItem> = new Map();
  const rootItems: MenuItem[] = [];
  rootItems.push({
    id: '0',
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
        id: category.id.toString(),
        label: category.name ?? '',
        command: () => {
          navigate(`/external/${category.contentType}/${category.id}`);
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
        parentMenuItem.command = null;
      }
    } else if (menuItem) {
      // If no parent, it's a root item
      rootItems.push(menuItem);
    }
  });

  return rootItems;
};

/**
 * 根据 id 获取所在层级的第二层菜单及其子菜单
 * @param menuItems 菜单项数组
 * @param id 目标菜单项的 id
 * @returns 第二层菜单及其子菜单，若未找到返回 null
 */
export const getSecondLevelMenu = (categories: ICategory[], id: string, navigate: Function): MenuItem[] | null => {
  let secondLevelMenu: MenuItem[] | null = null;
  if (categories == null) return secondLevelMenu;
  const menuItems = transformToMenuItems(categories, navigate);

  function findParentMenu(items: MenuItem[], level: number, parent?: MenuItem): boolean {
    for (const item of items) {
      if (item.id === id) {
        // 如果当前层级为第三层或更深层级，返回到第二层的父菜单
        if (level >= 2 && parent) {
          secondLevelMenu = (parent.items as MenuItem[]) || null;
        } else if (level === 1) {
          secondLevelMenu = items;
        }
        return true;
      }

      if (item.items) {
        const foundInSubmenu = findParentMenu(item.items as MenuItem[], level + 1, item);
        if (foundInSubmenu) {
          // 如果当前项是目标项的父项，并且是第二层，返回其子菜单
          if (level === 1 && parent) {
            secondLevelMenu = (parent.items as MenuItem[]) || null;
          }
          return true;
        }
      }
    }
    return false;
  }

  // 从根层级（0层）开始查找
  findParentMenu(menuItems, 0);
  return secondLevelMenu;
};

// 递归渲染菜单项，并添加点击事件
export const renderMenuItems = (items: MenuItem[], selectedId: string): MenuItem[] =>
  items?.map(item => ({
    ...item,
    className: item.id == selectedId ? 'selected-menu-item' : '',
    items: item.items ? renderMenuItems(item.items as MenuItem[], selectedId) : undefined,
  }));

/**
 * 根据 id 查找从自身到最上层的所有节点
 * @param menuItems 菜单项数组
 * @param id 目标菜单项的 id
 * @returns 包含从自身到最上层的所有节点数组
 */
export const findPathToRoot = (categories: ICategory[], id: string, navigate: Function): MenuItem[] => {
  let path: MenuItem[] = [];
  const menuItems: MenuItem[] = transformToMenuItems(categories, navigate);
  if (menuItems == null) return path;

  function findMenu(items: MenuItem[], currentPath: MenuItem[]): boolean {
    for (const item of items) {
      // 将当前节点加入路径
      currentPath.push(item);

      if (item.id === id) {
        path = [...currentPath]; // 找到目标节点时记录路径
        return true;
      }

      if (item.items) {
        // 深度优先搜索子菜单
        const foundInSubmenu = findMenu(item.items as MenuItem[], currentPath);
        if (foundInSubmenu) {
          return true;
        }
      }

      // 当前节点不在路径上，移除
      currentPath.pop();
    }
    return false;
  }

  findMenu(menuItems, []);
  return path;
};

export const homeMenuItem = (navigate: Function): MenuItem => {
  return {
    icon: 'pi pi-home',
    command: () => {
      navigate('/external');
    },
  };
};
