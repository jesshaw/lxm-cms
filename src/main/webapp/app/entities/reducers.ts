import dict from 'app/entities/dict/dict.reducer';
import authority from 'app/entities/admin/authority/authority.reducer';
import resource from 'app/entities/resource/resource.reducer';
import employee from 'app/entities/employee/employee.reducer';
import category from 'app/entities/category/category.reducer';
import staticPage from 'app/entities/static-page/static-page.reducer';
import post from 'app/entities/post/post.reducer';
import externalCategory from 'app/external/external-category.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  dict,
  authority,
  resource,
  employee,
  category,
  staticPage,
  post,
  externalCategory,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
