package com.lexiangmiao.cms.web.rest;

import com.lexiangmiao.cms.repository.StaticPageRepository;
import com.lexiangmiao.cms.security.PermissionConstants;
import com.lexiangmiao.cms.security.ResourceConstants;
import com.lexiangmiao.cms.service.StaticPageQueryService;
import com.lexiangmiao.cms.service.StaticPageService;
import com.lexiangmiao.cms.service.criteria.StaticPageCriteria;
import com.lexiangmiao.cms.service.dto.StaticPageDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.lexiangmiao.cms.domain.StaticPage}.
 */
@RestController
@RequestMapping("/api/static-pages")
public class StaticPageResource {

    private static final Logger LOG = LoggerFactory.getLogger(StaticPageResource.class);

    private static final String ENTITY_NAME = "staticPage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StaticPageService staticPageService;

    private final StaticPageRepository staticPageRepository;

    private final StaticPageQueryService staticPageQueryService;

    public StaticPageResource(
        StaticPageService staticPageService,
        StaticPageRepository staticPageRepository,
        StaticPageQueryService staticPageQueryService
    ) {
        this.staticPageService = staticPageService;
        this.staticPageRepository = staticPageRepository;
        this.staticPageQueryService = staticPageQueryService;
    }

    /**
     * {@code POST  /static-pages} : Create a new staticPage.
     *
     * @param staticPageDto the staticPageDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new staticPageDto, or with status {@code 400 (Bad Request)} if the staticPage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("@lxmAuth.hasPermission(\"" + ResourceConstants.STATIC_PAGE + "\",\"" + PermissionConstants.EDIT + "\")")
    public ResponseEntity<StaticPageDto> createStaticPage(@Valid @RequestBody StaticPageDto staticPageDto) throws URISyntaxException {
        LOG.debug("REST request to save StaticPage : {}", staticPageDto);
        if (staticPageDto.getId() != null) {
            throw new BadRequestAlertException("A new staticPage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        staticPageDto = staticPageService.save(staticPageDto);
        return ResponseEntity.created(new URI("/api/static-pages/" + staticPageDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, staticPageDto.getId().toString()))
            .body(staticPageDto);
    }

    /**
     * {@code PUT  /static-pages/:id} : Updates an existing staticPage.
     *
     * @param id the id of the staticPageDto to save.
     * @param staticPageDto the staticPageDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staticPageDto,
     * or with status {@code 400 (Bad Request)} if the staticPageDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the staticPageDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("@lxmAuth.hasPermission(\"" + ResourceConstants.STATIC_PAGE + "\",\"" + PermissionConstants.EDIT + "\")")
    public ResponseEntity<StaticPageDto> updateStaticPage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StaticPageDto staticPageDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update StaticPage : {}, {}", id, staticPageDto);
        if (staticPageDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, staticPageDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!staticPageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        staticPageDto = staticPageService.update(staticPageDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, staticPageDto.getId().toString()))
            .body(staticPageDto);
    }

    /**
     * {@code PATCH  /static-pages/:id} : Partial updates given fields of an existing staticPage, field will ignore if it is null
     *
     * @param id the id of the staticPageDto to save.
     * @param staticPageDto the staticPageDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staticPageDto,
     * or with status {@code 400 (Bad Request)} if the staticPageDto is not valid,
     * or with status {@code 404 (Not Found)} if the staticPageDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the staticPageDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("@lxmAuth.hasPermission(\"" + ResourceConstants.STATIC_PAGE + "\",\"" + PermissionConstants.EDIT + "\")")
    public ResponseEntity<StaticPageDto> partialUpdateStaticPage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StaticPageDto staticPageDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StaticPage partially : {}, {}", id, staticPageDto);
        if (staticPageDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, staticPageDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!staticPageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StaticPageDto> result = staticPageService.partialUpdate(staticPageDto);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, staticPageDto.getId().toString())
        );
    }

    /**
     * {@code GET  /static-pages} : get all the staticPages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of staticPages in body.
     */
    @GetMapping("")
    @PreAuthorize("@lxmAuth.hasPermission(\"" + ResourceConstants.STATIC_PAGE + "\",\"" + PermissionConstants.LIST + "\")")
    public ResponseEntity<List<StaticPageDto>> getAllStaticPages(
        StaticPageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get StaticPages by criteria: {}", criteria);

        Page<StaticPageDto> page = staticPageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /static-pages/count} : count all the staticPages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    @PreAuthorize("@lxmAuth.hasPermission(\"" + ResourceConstants.STATIC_PAGE + "\",\"" + PermissionConstants.LIST + "\")")
    public ResponseEntity<Long> countStaticPages(StaticPageCriteria criteria) {
        LOG.debug("REST request to count StaticPages by criteria: {}", criteria);
        return ResponseEntity.ok().body(staticPageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /static-pages/:id} : get the "id" staticPage.
     *
     * @param id the id of the staticPageDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the staticPageDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @PreAuthorize("@lxmAuth.hasPermission(\"" + ResourceConstants.STATIC_PAGE + "\",\"" + PermissionConstants.VIEW + "\")")
    public ResponseEntity<StaticPageDto> getStaticPage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StaticPage : {}", id);
        Optional<StaticPageDto> staticPageDto = staticPageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(staticPageDto);
    }

    /**
     * {@code DELETE  /static-pages/:id} : delete the "id" staticPage.
     *
     * @param id the id of the staticPageDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@lxmAuth.hasPermission(\"" + ResourceConstants.STATIC_PAGE + "\",\"" + PermissionConstants.DELETE + "\")")
    public ResponseEntity<Void> deleteStaticPage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StaticPage : {}", id);
        staticPageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
