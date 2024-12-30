import FullPageLayout from 'app/shared/layout/full-page-layout';
import React from 'react';
import { Link } from 'react-router-dom';

const ExternalPost: React.FC = () => {
  return (
    <FullPageLayout>
      <div>
        <div>多新闻列表</div>
        <div>
          <Link to={'/external/post/4/1'}>新闻1</Link>
        </div>
      </div>
    </FullPageLayout>
  );
};

export default ExternalPost;
