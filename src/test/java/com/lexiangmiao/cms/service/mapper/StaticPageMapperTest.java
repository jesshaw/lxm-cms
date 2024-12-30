package com.lexiangmiao.cms.service.mapper;

import static com.lexiangmiao.cms.domain.StaticPageAsserts.*;
import static com.lexiangmiao.cms.domain.StaticPageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StaticPageMapperTest {

    private StaticPageMapper staticPageMapper;

    @BeforeEach
    void setUp() {
        staticPageMapper = new StaticPageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStaticPageSample1();
        var actual = staticPageMapper.toEntity(staticPageMapper.toDto(expected));
        assertStaticPageAllPropertiesEquals(expected, actual);
    }
}
