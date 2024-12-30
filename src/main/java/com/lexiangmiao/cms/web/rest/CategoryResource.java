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
@RequestMapping("/api/categories")
public class CategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryResource.class);

    private static final String ENTITY_NAME = "category";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoryService categoryService;

    private final CategoryRepository categoryRepository;

    public CategoryResource(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    /**
     * {@code POST  /categories} : Create a new category.
     *
     * @param categoryDto the categoryDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoryDto, or with status {@code 400 (Bad Request)} if the category has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("@lxmAuth.hasPermission(\"" + ResourceConstants.CATEGORY + "\",\"" + PermissionConstants.EDIT + "\")")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) throws URISyntaxException {
        LOG.debug("REST request to save Category : {}", categoryDto);
        if (categoryDto.getId() != null) {
            throw new BadRequestAlertException("A new category cannot already have an ID", ENTITY_NAME, "idexists");
        }
        categoryDto = categoryService.save(categoryDto);
        return ResponseEntity.created(new URI("/api/categories/" + categoryDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, categoryDto.getId().toString()))
            .body(categoryDto);
    }

    /**
     * {@code PUT  /categories/:id} : Updates an existing category.
     *
     * @param id the id of the categoryDto to save.
     * @param categoryDto the categoryDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryDto,
     * or with status {@code 400 (Bad Request)} if the categoryDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoryDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("@lxmAuth.hasPermission(\"" + ResourceConstants.CATEGORY + "\",\"" + PermissionConstants.EDIT + "\")")
    public ResponseEntity<CategoryDto> updateCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategoryDto categoryDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update Category : {}, {}", id, categoryDto);
        if (categoryDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        categoryDto = categoryService.update(categoryDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryDto.getId().toString()))
            .body(categoryDto);
    }

    /**
     * {@code PATCH  /categories/:id} : Partial updates given fields of an existing category, field will ignore if it is null
     *
     * @param id the id of the categoryDto to save.
     * @param categoryDto the categoryDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryDto,
     * or with status {@code 400 (Bad Request)} if the categoryDto is not valid,
     * or with status {@code 404 (Not Found)} if the categoryDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoryDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("@lxmAuth.hasPermission(\"" + ResourceConstants.CATEGORY + "\",\"" + PermissionConstants.EDIT + "\")")
    public ResponseEntity<CategoryDto> partialUpdateCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategoryDto categoryDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Category partially : {}, {}", id, categoryDto);
        if (categoryDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoryDto> result = categoryService.partialUpdate(categoryDto);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryDto.getId().toString())
        );
    }

    /**
     * {@code GET  /categories} : get all the categories.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categories in body.
     */
    @GetMapping("")
    @PreAuthorize("@lxmAuth.hasPermission(\"" + ResourceConstants.CATEGORY + "\",\"" + PermissionConstants.LIST + "\")")
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
    @PreAuthorize("@lxmAuth.hasPermission(\"" + ResourceConstants.CATEGORY + "\",\"" + PermissionConstants.VIEW + "\")")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Category : {}", id);
        Optional<CategoryDto> categoryDto = categoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoryDto);
    }

    /**
     * {@code DELETE  /categories/:id} : delete the "id" category.
     *
     * @param id the id of the categoryDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@lxmAuth.hasPermission(\"" + ResourceConstants.CATEGORY + "\",\"" + PermissionConstants.DELETE + "\")")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Category : {}", id);
        categoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
