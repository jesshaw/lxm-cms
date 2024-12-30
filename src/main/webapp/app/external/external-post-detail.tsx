import FullPageLayout from 'app/shared/layout/full-page-layout';
import React from 'react';

const ExternalPostDetail: React.FC = () => {
  return (
    <FullPageLayout>
      <div>导航</div>
      <div>
        <div>三级菜单</div>
        <div>四级菜单</div>
        <div>
          <div>新闻动态</div>
          <div>内容</div>
        </div>
      </div>
    </FullPageLayout>
  );
};

export default ExternalPostDetail;
