package com.lexiangmiao.cms.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.lexiangmiao.cms.domain.StaticPage} entity.
 */
@Schema(description = "页面")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StaticPageDto implements Serializable {

    private Long id;

    @Size(max = 255)
    private String title;

    @Lob
    private String content;

    @Size(max = 100)
    private String status;

    private CategoryDto category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StaticPageDto)) {
            return false;
        }

        StaticPageDto staticPageDto = (StaticPageDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, staticPageDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StaticPageDto{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", status='" + getStatus() + "'" +
            ", category=" + getCategory() +
            "}";
    }
}
