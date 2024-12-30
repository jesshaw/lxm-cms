import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StaticPage from './static-page';
import StaticPageDetail from './static-page-detail';
import StaticPageUpdate from './static-page-update';

const StaticPageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StaticPage />} />
    <Route path="new" element={<StaticPageUpdate />} />
    <Route path=":id">
      <Route index element={<StaticPageDetail />} />
      <Route path="edit" element={<StaticPageUpdate />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StaticPageRoutes;
