import './external-site.css';

import React from 'react';
import { Route } from 'react-router-dom';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import ExternalNavbar from './external-navbar';
import ExternalStaticPage from './external-static-page';
import ExternalStaticPageDetail from './external-static-page-detail';
import ExternalPost from './external-post';
import ExternalPostDetail from './external-post-detail';
import ExternalHome from './external-home';

export default () => {
  return (
    <div id="l-site">
      <ExternalNavbar />
      <ErrorBoundaryRoutes>
        <Route index element={<ExternalHome />} />
        <Route path="page">
          <Route path=":cid">
            <Route index element={<ExternalStaticPage />} />
            <Route path=":id" element={<ExternalStaticPageDetail />} />
          </Route>
        </Route>
        <Route path="post">
          <Route path=":cid">
            <Route index element={<ExternalPost />} />
            <Route path=":id" element={<ExternalPostDetail />} />
          </Route>
        </Route>
      </ErrorBoundaryRoutes>

      <div className="l-site-footer">乐香喵版权所有 © 2025</div>
    </div>
  );
};
