package com.lexiangmiao.cms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lexiangmiao.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StaticPageDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StaticPageDto.class);
        StaticPageDto staticPageDto1 = new StaticPageDto();
        staticPageDto1.setId(1L);
        StaticPageDto staticPageDto2 = new StaticPageDto();
        assertThat(staticPageDto1).isNotEqualTo(staticPageDto2);
        staticPageDto2.setId(staticPageDto1.getId());
        assertThat(staticPageDto1).isEqualTo(staticPageDto2);
        staticPageDto2.setId(2L);
        assertThat(staticPageDto1).isNotEqualTo(staticPageDto2);
        staticPageDto1.setId(null);
        assertThat(staticPageDto1).isNotEqualTo(staticPageDto2);
    }
}
