import FullPageLayout from 'app/shared/layout/full-page-layout';
import React from 'react';

const ExternalHome: React.FC = () => {
  return (
    <FullPageLayout>
      <div>导航</div>
      <div>
        <h5>快捷入口</h5>
      </div>
      <div>
        <h5>资讯动态</h5>
      </div>
    </FullPageLayout>
  );
};

export default ExternalHome;
