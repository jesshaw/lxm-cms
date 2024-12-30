package com.lexiangmiao.cms.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.lexiangmiao.cms.domain.Category} entity.
 */
@Schema(description = "栏目")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoryDto implements Serializable {

    private Long id;

    @Size(max = 100)
    private String name;

    @Size(max = 255)
    private String contentType;

    private Integer sort;

    private CategoryDto parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public CategoryDto getParent() {
        return parent;
    }

    public void setParent(CategoryDto parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoryDto)) {
            return false;
        }

        CategoryDto categoryDto = (CategoryDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, categoryDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoryDto{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", contentType='" + getContentType() + "'" +
            ", sort=" + getSort() +
            ", parent=" + getParent() +
            "}";
    }
}
