package com.lexiangmiao.cms.service.mapper;

import com.lexiangmiao.cms.domain.Category;
import com.lexiangmiao.cms.domain.StaticPage;
import com.lexiangmiao.cms.service.dto.CategoryDto;
import com.lexiangmiao.cms.service.dto.StaticPageDto;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StaticPage} and its DTO {@link StaticPageDto}.
 */
@Mapper(componentModel = "spring")
public interface StaticPageMapper extends EntityMapper<StaticPageDto, StaticPage> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryName")
    StaticPageDto toDto(StaticPage s);

    @Named("categoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryDto toDtoCategoryName(Category category);
}
