package com.lexiangmiao.cms.service.impl;

import com.lexiangmiao.cms.domain.StaticPage;
import com.lexiangmiao.cms.repository.StaticPageRepository;
import com.lexiangmiao.cms.service.StaticPageService;
import com.lexiangmiao.cms.service.dto.StaticPageDto;
import com.lexiangmiao.cms.service.mapper.StaticPageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.lexiangmiao.cms.domain.StaticPage}.
 */
@Service
@Transactional
public class StaticPageServiceImpl implements StaticPageService {

    private final Logger log = LoggerFactory.getLogger(StaticPageServiceImpl.class);

    private final StaticPageRepository staticPageRepository;

    private final StaticPageMapper staticPageMapper;

    public StaticPageServiceImpl(StaticPageRepository staticPageRepository, StaticPageMapper staticPageMapper) {
        this.staticPageRepository = staticPageRepository;
        this.staticPageMapper = staticPageMapper;
    }

    @Override
    public StaticPageDto save(StaticPageDto staticPageDto) {
        log.debug("Request to save StaticPage : {}", staticPageDto);
        StaticPage staticPage = staticPageMapper.toEntity(staticPageDto);
        staticPage = staticPageRepository.save(staticPage);
        return staticPageMapper.toDto(staticPage);
    }

    @Override
    public StaticPageDto update(StaticPageDto staticPageDto) {
        log.debug("Request to update StaticPage : {}", staticPageDto);
        StaticPage staticPage = staticPageMapper.toEntity(staticPageDto);
        staticPage = staticPageRepository.save(staticPage);
        return staticPageMapper.toDto(staticPage);
    }

    @Override
    public Optional<StaticPageDto> partialUpdate(StaticPageDto staticPageDto) {
        log.debug("Request to partially update StaticPage : {}", staticPageDto);

        return staticPageRepository
            .findById(staticPageDto.getId())
            .map(existingStaticPage -> {
                staticPageMapper.partialUpdate(existingStaticPage, staticPageDto);

                return existingStaticPage;
            })
            .map(staticPageRepository::save)
            .map(staticPageMapper::toDto);
    }

    public Page<StaticPageDto> findAllWithEagerRelationships(Pageable pageable) {
        return staticPageRepository.findAllWithEagerRelationships(pageable).map(staticPageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StaticPageDto> findOne(Long id) {
        log.debug("Request to get StaticPage : {}", id);
        return staticPageRepository.findOneWithEagerRelationships(id).map(staticPageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete StaticPage : {}", id);
        staticPageRepository.deleteById(id);
    }
}
