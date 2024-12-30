import React, { useState, useEffect, useRef } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
// import { Button, Table } from 'reactstrap';
import { Toolbar } from 'primereact/toolbar';
import { DataTable, DataTableFilterMeta, DataTableStateEvent } from 'primereact/datatable';
import { MultiSelect, MultiSelectChangeEvent } from 'primereact/multiselect';
import { Column } from 'primereact/column';
import { Button } from 'primereact/button';
import { Dialog } from 'primereact/dialog';
import { Translate, translate, TranslatorContext, getSortState } from 'react-jhipster';
// import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
// import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, deleteEntity } from './category.reducer';
import { ICategory } from 'app/shared/model/category.model';
import { MenuItemsData, setBreadItems } from 'app/shared/reducers/ui';
import { FilterMatchMode, FilterOperator } from 'primereact/api';
import { classNames } from 'primereact/utils';
import { Slider } from 'primereact/slider';
import {
  booleanFilterTemplate,
  convertFiltersToQueryString,
  dateFilterTemplate,
  numericFilterTemplate,
  LxmColumnProps,
} from 'app/shared/util/lxm-utils';

const defaultFilters: DataTableFilterMeta = {
  name: { value: null, matchMode: FilterMatchMode.STARTS_WITH },

  contentType: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
};

export const Category = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const [filters, setFilters] = useState<DataTableFilterMeta>(defaultFilters);

  useEffect(() => {
    dispatch(setBreadItems([MenuItemsData.homeMenuItem, MenuItemsData.entitesMenuItem, MenuItemsData.categoryMenuItem]));
  }, []);

  const categoryList = useAppSelector(state => state.category.entities);
  const loading = useAppSelector(state => state.category.loading);

  const getAllEntities = query => {
    dispatch(
      getEntities({
        query,
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    const queryString = convertFiltersToQueryString(filters);
    getAllEntities(queryString);
    const endURL = `?${queryString}&sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort, filters]);

  const onSort = (e: DataTableStateEvent) => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: e.sortField,
    });
  };

  const onFilter = (e: DataTableStateEvent) => {
    setFilters(e.filters);
  };

  const handleSyncList = () => {
    sortEntities();
  };

  let emptyCategory: ICategory = {};

  const [deleteCategoryDialog, setDeleteCategoryDialog] = useState<boolean>(false);
  const [category, setCategory] = useState<ICategory>(emptyCategory);
  const updateSuccess = useAppSelector(state => state.category.updateSuccess);

  const hideDeleteCategoryDialog = () => {
    setDeleteCategoryDialog(false);
  };

  const dt = useRef<DataTable<ICategory[]>>(null);

  const startToolbarTemplate = () => {
    return (
      <div className="flex flex-wrap gap-2">
        <Button
          label={translate('entity.action.refresh')}
          icon={`pi ${loading ? 'pi-spin' : ''} pi-refresh`}
          onClick={handleSyncList}
          disabled={loading}
        />
        <Button label={translate('entity.action.new')} icon="pi pi-plus" severity="success" onClick={() => navigate('/category/new')} />
      </div>
    );
  };

  const endToolbarTemplate = () => {
    return (
      <>
        <Button label={translate('entity.action.export')} icon="pi pi-upload" className="p-button-help" onClick={exportCSV} />
      </>
    );
  };

  const exportCSV = () => {
    dt.current?.exportCSV();
  };

  const confirmDeleteCategory = (category: ICategory) => {
    setCategory(category);
    setDeleteCategoryDialog(true);
  };

  const deleteCategory = () => {
    dispatch(deleteEntity(category.id));
  };

  useEffect(() => {
    if (updateSuccess && deleteCategoryDialog) {
      setDeleteCategoryDialog(false);
      sortEntities();
      setCategory(emptyCategory);
    }
  }, [updateSuccess]);

  const deleteCategoryDialogFooter = (
    <>
      <Button label={translate('entity.action.cancel')} icon="pi pi-times" outlined onClick={hideDeleteCategoryDialog} />
      <Button label={translate('entity.action.delete')} icon="pi pi-check" severity="danger" onClick={deleteCategory} />
    </>
  );

  const allColumns: LxmColumnProps[] = [
    {
      field: 'id',
      headerKey: 'lxmcmsApp.category.id',
      sortable: true,
    },
    {
      field: 'name',
      headerKey: 'lxmcmsApp.category.name',
      sortable: true,
      filter: true,
    },
    {
      field: 'contentType',
      headerKey: 'lxmcmsApp.category.contentType',
      sortable: true,
      filter: true,
    },
    {
      field: 'sort',
      headerKey: 'lxmcmsApp.category.sort',
      sortable: true,
    },
    {
      field: 'parent.name',
      headerKey: 'lxmcmsApp.category.parent',
      sortable: true,
      body: rowData => {
        return rowData.parent && <Button text label={rowData.parent.name} onClick={() => navigate(`/category/${rowData.parent.id}`)} />;
      },
    },
    {
      field: 'lOperate',
      headerKey: 'entity.cloumn.operation',
      exportable: false,
      style: { minWidth: '12rem' },
      body: rowData => {
        return (
          <>
            <Button
              icon="pi pi-eye"
              tooltip={translate('entity.action.view')}
              tooltipOptions={{ position: 'top' }}
              rounded
              outlined
              className="mr-2"
              onClick={() => navigate(`/category/${rowData.id}`)}
            />
            <Button
              icon="pi pi-pencil"
              tooltip={translate('entity.action.edit')}
              tooltipOptions={{ position: 'top' }}
              rounded
              outlined
              className="mr-2"
              onClick={() => navigate(`/category/${rowData.id}/edit?sort=${sortState.sort},${sortState.order}`)}
            />
            <Button
              icon="pi pi-trash"
              tooltip={translate('entity.action.delete')}
              tooltipOptions={{ position: 'top' }}
              rounded
              outlined
              severity="danger"
              onClick={() => confirmDeleteCategory(rowData)}
            />
          </>
        );
      },
    },
  ];

  //localStorage key used to store display columns
  const entityColumnsKey = 'lxmcrmApp.category.cloumns';

  const getDefaultSelectedColumns = () => {
    let storageColumns = localStorage.getItem(entityColumnsKey);
    console.log(storageColumns);
    let defaultSelectedColumns;
    if (storageColumns !== null) {
      defaultSelectedColumns = JSON.parse(storageColumns);
    } else {
      let willBeDisplayedColumns = allColumns.length <= 6 ? allColumns.slice(0, allColumns.length - 2) : allColumns.slice(0, 5);
      willBeDisplayedColumns.push(allColumns[allColumns.length - 1]); // push last operation comlumn
      defaultSelectedColumns = willBeDisplayedColumns.map(item => ({
        header: translate(item.headerKey),
        headerKey: item.headerKey,
        field: item.field,
      }));
    }
    return defaultSelectedColumns;
  };

  const [selectedColumns, setSelectedColumns] = useState(getDefaultSelectedColumns());

  const columnOptions = allColumns.map(item => ({
    header: translate(item.headerKey),
    headerKey: item.headerKey,
    field: item.field,
  }));

  useEffect(() => {
    let _selectedColumns = [...selectedColumns];
    _selectedColumns.forEach(item => (item.header = translate(item.headerKey)));
    // console.log(_selectedColumns);
    setSelectedColumns(_selectedColumns);
  }, [TranslatorContext.context.locale]);

  const onColumnToggle = (event: MultiSelectChangeEvent) => {
    let _selectedColumns = [...selectedColumns];
    const selectedOption = event.selectedOption;
    if (Array.isArray(selectedOption)) {
      if (_selectedColumns.length < columnOptions.length) {
        _selectedColumns = columnOptions;
      } else {
        _selectedColumns = [];
      }
    } else {
      const existingIndex = _selectedColumns.findIndex(existingCol => existingCol.field === selectedOption.field);
      if (existingIndex !== -1) {
        _selectedColumns.splice(existingIndex, 1);
      } else {
        _selectedColumns.push(columnOptions.find(item => item.field === selectedOption.field));
      }
    }

    setSelectedColumns(_selectedColumns);
    localStorage.setItem(entityColumnsKey, JSON.stringify(_selectedColumns));
  };

  const header = (
    <div className="l-datatable-header">
      <h5>
        <Translate contentKey="lxmcmsApp.category.home.title">Categories</Translate>
      </h5>
      <MultiSelect
        value={selectedColumns}
        options={columnOptions}
        optionLabel="header"
        filter
        onChange={onColumnToggle}
        className="l-select-columns"
        placeholder={translate('entity.cloumn.placeholder')}
        display="chip"
      />
    </div>
  );

  const dynamicColumns = selectedColumns
    .filter(selectedCol => allColumns.some(col => col.field === selectedCol.field))
    .map(selectedCol => {
      const column = allColumns.find(col => col.field === selectedCol.field);
      return (
        <Column
          key={column?.field}
          field={column?.field}
          header={translate(column?.headerKey)}
          body={column?.body ? column.body : undefined}
          sortable={column?.sortable}
          filter={column?.filter}
          dataType={column?.dataType}
          showFilterOperator={column?.showFilterOperator}
          showFilterMatchModes={column?.showFilterMatchModes}
          filterElement={column?.filterElement}
          exportable={column?.exportable}
          style={column?.style}
        />
      );
    });

  return (
    <>
      <div className="l-card">
        <Toolbar className="l-toolbar" start={startToolbarTemplate} end={endToolbarTemplate}></Toolbar>
        <DataTable
          ref={dt}
          value={categoryList}
          dataKey="id"
          selectionMode="single"
          header={header}
          emptyMessage={translate('lxmcmsApp.category.home.notFound')}
          onSort={onSort} //sort by backend
          sortField={sortState.sort}
          sortOrder={sortState.order === ASC ? -1 : 1}
        >
          {dynamicColumns}
        </DataTable>
      </div>

      <Dialog
        visible={deleteCategoryDialog}
        style={{ width: '32rem' }}
        breakpoints={{ '960px': '75vw', '641px': '90vw' }}
        header={translate('entity.delete.title')}
        modal
        footer={deleteCategoryDialogFooter}
        onHide={hideDeleteCategoryDialog}
      >
        <div className="confirmation-content">
          <i className="pi pi-exclamation-triangle mr-3" style={{ fontSize: '2rem' }} />
          {category && (
            <Translate contentKey="lxmcmsApp.category.delete.question" interpolate={{ id: category.id }}>
              Are you sure you want to delete this Category?
            </Translate>
          )}
        </div>
      </Dialog>
    </>
  );
};

export default Category;
