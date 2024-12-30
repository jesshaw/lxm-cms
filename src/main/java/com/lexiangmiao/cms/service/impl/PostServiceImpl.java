package com.lexiangmiao.cms.service.impl;

import com.lexiangmiao.cms.domain.Post;
import com.lexiangmiao.cms.repository.PostRepository;
import com.lexiangmiao.cms.service.PostService;
import com.lexiangmiao.cms.service.dto.PostDto;
import com.lexiangmiao.cms.service.mapper.PostMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.lexiangmiao.cms.domain.Post}.
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Override
    public PostDto save(PostDto postDto) {
        log.debug("Request to save Post : {}", postDto);
        Post post = postMapper.toEntity(postDto);
        post = postRepository.save(post);
        return postMapper.toDto(post);
    }

    @Override
    public PostDto update(PostDto postDto) {
        log.debug("Request to update Post : {}", postDto);
        Post post = postMapper.toEntity(postDto);
        post = postRepository.save(post);
        return postMapper.toDto(post);
    }

    @Override
    public Optional<PostDto> partialUpdate(PostDto postDto) {
        log.debug("Request to partially update Post : {}", postDto);

        return postRepository
            .findById(postDto.getId())
            .map(existingPost -> {
                postMapper.partialUpdate(existingPost, postDto);

                return existingPost;
            })
            .map(postRepository::save)
            .map(postMapper::toDto);
    }

    public Page<PostDto> findAllWithEagerRelationships(Pageable pageable) {
        return postRepository.findAllWithEagerRelationships(pageable).map(postMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostDto> findOne(Long id) {
        log.debug("Request to get Post : {}", id);
        return postRepository.findOneWithEagerRelationships(id).map(postMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Post : {}", id);
        postRepository.deleteById(id);
    }
}
