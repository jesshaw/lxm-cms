import React, { useState, useEffect, useRef } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
// import { Button, Table } from 'reactstrap';
import { Toolbar } from 'primereact/toolbar';
import { DataTable, DataTableFilterMeta, DataTableStateEvent } from 'primereact/datatable';
import { MultiSelect, MultiSelectChangeEvent } from 'primereact/multiselect';
import { Column } from 'primereact/column';
import { Button } from 'primereact/button';
import { Dialog } from 'primereact/dialog';
import { byteSize, Translate, translate, TranslatorContext, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
// import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
// import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, deleteEntity } from './static-page.reducer';
import { IStaticPage } from 'app/shared/model/static-page.model';
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
  title: { value: null, matchMode: FilterMatchMode.STARTS_WITH },

  status: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
};

export const StaticPage = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const [filters, setFilters] = useState<DataTableFilterMeta>(defaultFilters);

  useEffect(() => {
    dispatch(setBreadItems([MenuItemsData.homeMenuItem, MenuItemsData.entitesMenuItem, MenuItemsData.staticPageMenuItem]));
  }, []);

  const staticPageList = useAppSelector(state => state.staticPage.entities);
  const loading = useAppSelector(state => state.staticPage.loading);
  const totalItems = useAppSelector(state => state.staticPage.totalItems);

  const getAllEntities = query => {
    dispatch(
      getEntities({
        query,
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    const queryString = convertFiltersToQueryString(filters);
    getAllEntities(queryString);
    const endURL = `?${queryString}&page=${paginationState.activePage}&size=${paginationState.itemsPerPage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.itemsPerPage, paginationState.order, paginationState.sort, filters]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const size = params.get('size');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        itemsPerPage: parseInt(size),
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const onSort = (e: DataTableStateEvent) => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: e.sortField,
    });
  };

  const onPage = (e: DataTableStateEvent) => {
    setPaginationState({
      ...paginationState,
      activePage: e.page + 1,
      itemsPerPage: e.rows,
    });
  };

  const onFilter = (e: DataTableStateEvent) => {
    setFilters(e.filters);
  };

  const handleSyncList = () => {
    sortEntities();
  };

  let emptyStaticPage: IStaticPage = {};

  const [deleteStaticPageDialog, setDeleteStaticPageDialog] = useState<boolean>(false);
  const [staticPage, setStaticPage] = useState<IStaticPage>(emptyStaticPage);
  const updateSuccess = useAppSelector(state => state.staticPage.updateSuccess);

  const hideDeleteStaticPageDialog = () => {
    setDeleteStaticPageDialog(false);
  };

  const dt = useRef<DataTable<IStaticPage[]>>(null);

  const startToolbarTemplate = () => {
    return (
      <div className="flex flex-wrap gap-2">
        <Button
          label={translate('entity.action.refresh')}
          icon={`pi ${loading ? 'pi-spin' : ''} pi-refresh`}
          onClick={handleSyncList}
          disabled={loading}
        />
        <Button label={translate('entity.action.new')} icon="pi pi-plus" severity="success" onClick={() => navigate('/static-page/new')} />
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

  const confirmDeleteStaticPage = (staticPage: IStaticPage) => {
    setStaticPage(staticPage);
    setDeleteStaticPageDialog(true);
  };

  const deleteStaticPage = () => {
    dispatch(deleteEntity(staticPage.id));
  };

  useEffect(() => {
    if (updateSuccess && deleteStaticPageDialog) {
      setDeleteStaticPageDialog(false);
      sortEntities();
      setStaticPage(emptyStaticPage);
    }
  }, [updateSuccess]);

  const deleteStaticPageDialogFooter = (
    <>
      <Button label={translate('entity.action.cancel')} icon="pi pi-times" outlined onClick={hideDeleteStaticPageDialog} />
      <Button label={translate('entity.action.delete')} icon="pi pi-check" severity="danger" onClick={deleteStaticPage} />
    </>
  );

  const allColumns: LxmColumnProps[] = [
    {
      field: 'id',
      headerKey: 'lxmcmsApp.staticPage.id',
      sortable: true,
    },
    {
      field: 'title',
      headerKey: 'lxmcmsApp.staticPage.title',
      sortable: true,
      filter: true,
    },
    {
      field: 'content',
      headerKey: 'lxmcmsApp.staticPage.content',
      sortable: true,
    },
    {
      field: 'status',
      headerKey: 'lxmcmsApp.staticPage.status',
      sortable: true,
      filter: true,
    },
    {
      field: 'category.name',
      headerKey: 'lxmcmsApp.staticPage.category',
      sortable: true,
      body: rowData => {
        return (
          rowData.category && <Button text label={rowData.category.name} onClick={() => navigate(`/category/${rowData.category.id}`)} />
        );
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
              onClick={() => navigate(`/static-page/${rowData.id}`)}
            />
            <Button
              icon="pi pi-pencil"
              tooltip={translate('entity.action.edit')}
              tooltipOptions={{ position: 'top' }}
              rounded
              outlined
              className="mr-2"
              onClick={() =>
                navigate(
                  `/static-page/${rowData.id}/edit?page=${paginationState.activePage}&size=${paginationState.itemsPerPage}&sort=${paginationState.sort},${paginationState.order}`,
                )
              }
            />
            <Button
              icon="pi pi-trash"
              tooltip={translate('entity.action.delete')}
              tooltipOptions={{ position: 'top' }}
              rounded
              outlined
              severity="danger"
              onClick={() => confirmDeleteStaticPage(rowData)}
            />
          </>
        );
      },
    },
  ];

  //localStorage key used to store display columns
  const entityColumnsKey = 'lxmcrmApp.staticPage.cloumns';

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
        <Translate contentKey="lxmcmsApp.staticPage.home.title">Static Pages</Translate>
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
          value={staticPageList}
          dataKey="id"
          selectionMode="single"
          header={header}
          emptyMessage={translate('lxmcmsApp.staticPage.home.notFound')}
          onSort={onSort} //sort by backend
          sortField={paginationState.sort}
          sortOrder={paginationState.order === ASC ? -1 : 1}
          onPage={onPage} //sort by backend
          filters={filters}
          onFilter={onFilter}
          lazy
          loading={loading}
          paginator
          first={(paginationState.activePage - 1) * paginationState.itemsPerPage} //current page start index
          rows={paginationState.itemsPerPage}
          totalRecords={totalItems}
          rowsPerPageOptions={[5, 10, 20, 50]}
          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
          currentPageReportTemplate={translate('global.item-count')}
        >
          {dynamicColumns}
        </DataTable>
      </div>

      <Dialog
        visible={deleteStaticPageDialog}
        style={{ width: '32rem' }}
        breakpoints={{ '960px': '75vw', '641px': '90vw' }}
        header={translate('entity.delete.title')}
        modal
        footer={deleteStaticPageDialogFooter}
        onHide={hideDeleteStaticPageDialog}
      >
        <div className="confirmation-content">
          <i className="pi pi-exclamation-triangle mr-3" style={{ fontSize: '2rem' }} />
          {staticPage && (
            <Translate contentKey="lxmcmsApp.staticPage.delete.question" interpolate={{ id: staticPage.id }}>
              Are you sure you want to delete this StaticPage?
            </Translate>
          )}
        </div>
      </Dialog>
    </>
  );
};

export default StaticPage;
