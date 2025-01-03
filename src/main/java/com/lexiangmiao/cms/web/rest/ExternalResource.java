package com.lexiangmiao.cms.web.rest;

import com.lexiangmiao.cms.service.*;
import com.lexiangmiao.cms.service.criteria.PostCriteria;
import com.lexiangmiao.cms.service.criteria.StaticPageCriteria;
import com.lexiangmiao.cms.service.dto.CategoryDto;
import com.lexiangmiao.cms.service.dto.PostDto;
import com.lexiangmiao.cms.service.dto.StaticPageDto;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.lexiangmiao.cms.domain.Category}.
 */
@RestController
@RequestMapping("/api/external")
public class ExternalResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoryService categoryService;
    private final PostQueryService postQueryService;
    private final PostService postService;
    private final StaticPageQueryService staticPageQueryService;
    private final StaticPageService staticPageService;

    public ExternalResource(
        CategoryService categoryService,
        PostQueryService postQueryService,
        PostService postService,
        StaticPageQueryService staticPageQueryService,
        StaticPageService staticPageService
    ) {
        this.categoryService = categoryService;
        this.postQueryService = postQueryService;
        this.postService = postService;
        this.staticPageQueryService = staticPageQueryService;
        this.staticPageService = staticPageService;
    }

    /**
     * {@code GET  /categories} : get all the categories.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categories in body.
     */
    @GetMapping("/categories")
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
    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Category : {}", id);
        Optional<CategoryDto> categoryDto = categoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoryDto);
    }

    /**
     * {@code GET  /posts} : get all the posts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of posts in body.
     */
    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getAllPosts(
        PostCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Posts by criteria: {}", criteria);
        Page<PostDto> page = postQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /posts/count} : count all the posts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/posts/count")
    public ResponseEntity<Long> countPosts(PostCriteria criteria) {
        LOG.debug("REST request to count Posts by criteria: {}", criteria);
        return ResponseEntity.ok().body(postQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /posts/:id} : get the "id" post.
     *
     * @param id the id of the postDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Post : {}", id);
        Optional<PostDto> postDto = postService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postDto);
    }

    /**
     * {@code GET  /static-pages} : get all the staticPages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of staticPages in body.
     */
    @GetMapping("/static-pages")
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
    @GetMapping("/static-pages/count")
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
    @GetMapping("/static-pages/{id}")
    public ResponseEntity<StaticPageDto> getStaticPage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StaticPage : {}", id);
        Optional<StaticPageDto> staticPageDto = staticPageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(staticPageDto);
    }
}
