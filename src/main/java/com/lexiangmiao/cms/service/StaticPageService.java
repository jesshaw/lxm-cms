package com.lexiangmiao.cms.service;

import com.lexiangmiao.cms.service.dto.StaticPageDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.lexiangmiao.cms.domain.StaticPage}.
 */
public interface StaticPageService {
    /**
     * Save a staticPage.
     *
     * @param staticPageDto the entity to save.
     * @return the persisted entity.
     */
    StaticPageDto save(StaticPageDto staticPageDto);

    /**
     * Updates a staticPage.
     *
     * @param staticPageDto the entity to update.
     * @return the persisted entity.
     */
    StaticPageDto update(StaticPageDto staticPageDto);

    /**
     * Partially updates a staticPage.
     *
     * @param staticPageDto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StaticPageDto> partialUpdate(StaticPageDto staticPageDto);

    /**
     * Get all the staticPages with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StaticPageDto> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" staticPage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StaticPageDto> findOne(Long id);

    /**
     * Delete the "id" staticPage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
