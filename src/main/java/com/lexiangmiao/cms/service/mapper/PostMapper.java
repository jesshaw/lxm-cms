package com.lexiangmiao.cms.service.mapper;

import com.lexiangmiao.cms.domain.Category;
import com.lexiangmiao.cms.domain.Post;
import com.lexiangmiao.cms.service.dto.CategoryDto;
import com.lexiangmiao.cms.service.dto.PostDto;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Post} and its DTO {@link PostDto}.
 */
@Mapper(componentModel = "spring")
public interface PostMapper extends EntityMapper<PostDto, Post> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryName")
    PostDto toDto(Post s);

    @Named("categoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryDto toDtoCategoryName(Category category);
}
