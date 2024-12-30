package com.lexiangmiao.cms.service;

import com.lexiangmiao.cms.domain.*; // for static metamodels
import com.lexiangmiao.cms.domain.StaticPage;
import com.lexiangmiao.cms.repository.StaticPageRepository;
import com.lexiangmiao.cms.service.criteria.StaticPageCriteria;
import com.lexiangmiao.cms.service.dto.StaticPageDto;
import com.lexiangmiao.cms.service.mapper.StaticPageMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for executing complex queries for {@link StaticPage} entities in the database.
 * The main input is a {@link StaticPageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link StaticPageDto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StaticPageQueryService extends LxmQueryService<StaticPage> {

    private final Logger log = LoggerFactory.getLogger(StaticPageQueryService.class);

    private final StaticPageRepository staticPageRepository;

    private final StaticPageMapper staticPageMapper;

    public StaticPageQueryService(StaticPageRepository staticPageRepository, StaticPageMapper staticPageMapper) {
        this.staticPageRepository = staticPageRepository;
        this.staticPageMapper = staticPageMapper;
    }

    /**
     * Return a {@link Page} of {@link StaticPageDto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StaticPageDto> findByCriteria(StaticPageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StaticPage> specification = createSpecification(criteria);
        return staticPageRepository.findAll(specification, page).map(staticPageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StaticPageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StaticPage> specification = createSpecification(criteria);
        return staticPageRepository.count(specification);
    }

    /**
     * Function to convert {@link StaticPageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StaticPage> createSpecification(StaticPageCriteria criteria) {
        Specification<StaticPage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StaticPage_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), StaticPage_.title));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), StaticPage_.status));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root -> root.join(StaticPage_.category, JoinType.LEFT).get(Category_.id))
                );
            }
        }
        return specification;
    }
}
