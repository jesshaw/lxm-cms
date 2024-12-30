package com.lexiangmiao.cms.domain;

import static com.lexiangmiao.cms.domain.CategoryTestSamples.*;
import static com.lexiangmiao.cms.domain.StaticPageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lexiangmiao.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StaticPageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StaticPage.class);
        StaticPage staticPage1 = getStaticPageSample1();
        StaticPage staticPage2 = new StaticPage();
        assertThat(staticPage1).isNotEqualTo(staticPage2);

        staticPage2.setId(staticPage1.getId());
        assertThat(staticPage1).isEqualTo(staticPage2);

        staticPage2 = getStaticPageSample2();
        assertThat(staticPage1).isNotEqualTo(staticPage2);
    }

    @Test
    void categoryTest() throws Exception {
        StaticPage staticPage = getStaticPageRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        staticPage.setCategory(categoryBack);
        assertThat(staticPage.getCategory()).isEqualTo(categoryBack);

        staticPage.category(null);
        assertThat(staticPage.getCategory()).isNull();
    }
}
