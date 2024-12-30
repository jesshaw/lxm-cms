package com.lexiangmiao.cms.web.rest;

import com.lexiangmiao.cms.repository.PostRepository;
import com.lexiangmiao.cms.security.PermissionConstants;
import com.lexiangmiao.cms.security.ResourceConstants;
import com.lexiangmiao.cms.service.PostQueryService;
import com.lexiangmiao.cms.service.PostService;
import com.lexiangmiao.cms.service.criteria.PostCriteria;
import com.lexiangmiao.cms.service.dto.PostDto;
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
 * REST controller for managing {@link com.lexiangmiao.cms.domain.Post}.
 */
@RestController
@RequestMapping("/api/posts")
public class PostResource {

    private static final Logger LOG = LoggerFactory.getLogger(PostResource.class);

    private static final String ENTITY_NAME = "post";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostService postService;

    private final PostRepository postRepository;

    private final PostQueryService postQueryService;

    public PostResource(PostService postService, PostRepository postRepository, PostQueryService postQueryService) {
        this.postService = postService;
        this.postRepository = postRepository;
        this.postQueryService = postQueryService;
    }

    /**
     * {@code POST  /posts} : Create a new post.
     *
     * @param postDto the postDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postDto, or with status {@code 400 (Bad Request)} if the post has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("@lxmAuth.hasPermission(\"" + ResourceConstants.POST + "\",\"" + PermissionConstants.EDIT + "\")")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) throws URISyntaxException {
        LOG.debug("REST request to save Post : {}", postDto);
        if (postDto.getId() != null) {
            throw new BadRequestAlertException("A new post cannot already have an ID", ENTITY_NAME, "idexists");
        }
        postDto = postService.save(postDto);
        return ResponseEntity.created(new URI("/api/posts/" + postDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, postDto.getId().toString()))
            .body(postDto);
    }

    /**
     * {@code PUT  /posts/:id} : Updates an existing post.
     *
     * @param id the id of the postDto to save.
     * @param postDto the postDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postDto,
     * or with status {@code 400 (Bad Request)} if the postDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("@lxmAuth.hasPermission(\"" + ResourceConstants.POST + "\",\"" + PermissionConstants.EDIT + "\")")
    public ResponseEntity<PostDto> updatePost(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PostDto postDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update Post : {}, {}", id, postDto);
        if (postDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        postDto = postService.update(postDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postDto.getId().toString()))
            .body(postDto);
    }

    /**
     * {@code PATCH  /posts/:id} : Partial updates given fields of an existing post, field will ignore if it is null
     *
     * @param id the id of the postDto to save.
     * @param postDto the postDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postDto,
     * or with status {@code 400 (Bad Request)} if the postDto is not valid,
     * or with status {@code 404 (Not Found)} if the postDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the postDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("@lxmAuth.hasPermission(\"" + ResourceConstants.POST + "\",\"" + PermissionConstants.EDIT + "\")")
    public ResponseEntity<PostDto> partialUpdatePost(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PostDto postDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Post partially : {}, {}", id, postDto);
        if (postDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PostDto> result = postService.partialUpdate(postDto);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postDto.getId().toString())
        );
    }

    /**
     * {@code GET  /posts} : get all the posts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of posts in body.
     */
    @GetMapping("")
    @PreAuthorize("@lxmAuth.hasPermission(\"" + ResourceConstants.POST + "\",\"" + PermissionConstants.LIST + "\")")
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
    @GetMapping("/count")
    @PreAuthorize("@lxmAuth.hasPermission(\"" + ResourceConstants.POST + "\",\"" + PermissionConstants.LIST + "\")")
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
    @GetMapping("/{id}")
    @PreAuthorize("@lxmAuth.hasPermission(\"" + ResourceConstants.POST + "\",\"" + PermissionConstants.VIEW + "\")")
    public ResponseEntity<PostDto> getPost(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Post : {}", id);
        Optional<PostDto> postDto = postService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postDto);
    }

    /**
     * {@code DELETE  /posts/:id} : delete the "id" post.
     *
     * @param id the id of the postDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@lxmAuth.hasPermission(\"" + ResourceConstants.POST + "\",\"" + PermissionConstants.DELETE + "\")")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Post : {}", id);
        postService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
