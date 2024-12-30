package com.lexiangmiao.cms.web.rest;

import com.lexiangmiao.cms.repository.CategoryRepository;
import com.lexiangmiao.cms.security.PermissionConstants;
import com.lexiangmiao.cms.security.ResourceConstants;
import com.lexiangmiao.cms.service.CategoryService;
import com.lexiangmiao.cms.service.dto.CategoryDto;
import com.lexiangmiao.cms.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.lexiangmiao.cms.domain.Category}.
 */
@RestController
@RequestMapping("/api/external/categories")
public class ExternalCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalCategoryResource.class);

    private static final String ENTITY_NAME = "category";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoryService categoryService;

    public ExternalCategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * {@code GET  /categories} : get all the categories.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categories in body.
     */
    @GetMapping("")
    public List<CategoryDto> getAllCategories(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all Categories");
        return categoryService.findAll();
    }

    /**
     * {@code GET  /categories/:id} : get the "id" category.
     *
     * @param id the id of the categoryDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoryDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Category : {}", id);
        Optional<CategoryDto> categoryDto = categoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoryDto);
    }
}
