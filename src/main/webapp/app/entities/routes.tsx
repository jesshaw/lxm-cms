import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Resource from './resource';
import Employee from './employee';
import Category from './category';
import StaticPage from './static-page';
import Post from './post';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="resource/*" element={<Resource />} />
        <Route path="employee/*" element={<Employee />} />
        <Route path="category/*" element={<Category />} />
        <Route path="static-page/*" element={<StaticPage />} />
        <Route path="post/*" element={<Post />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
