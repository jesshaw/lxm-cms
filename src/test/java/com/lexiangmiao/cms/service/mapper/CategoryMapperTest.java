package com.lexiangmiao.cms.service.mapper;

import static com.lexiangmiao.cms.domain.CategoryAsserts.*;
import static com.lexiangmiao.cms.domain.CategoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryMapperTest {

    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        categoryMapper = new CategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCategorySample1();
        var actual = categoryMapper.toEntity(categoryMapper.toDto(expected));
        assertCategoryAllPropertiesEquals(expected, actual);
    }
}
