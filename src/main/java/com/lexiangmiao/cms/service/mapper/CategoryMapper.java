package com.lexiangmiao.cms.service.mapper;

import com.lexiangmiao.cms.domain.Category;
import com.lexiangmiao.cms.service.dto.CategoryDto;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDto}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDto, Category> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "categoryName")
    CategoryDto toDto(Category s);

    @Named("categoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryDto toDtoCategoryName(Category category);
}
