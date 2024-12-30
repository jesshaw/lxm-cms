package com.lexiangmiao.cms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lexiangmiao.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PostDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostDto.class);
        PostDto postDto1 = new PostDto();
        postDto1.setId(1L);
        PostDto postDto2 = new PostDto();
        assertThat(postDto1).isNotEqualTo(postDto2);
        postDto2.setId(postDto1.getId());
        assertThat(postDto1).isEqualTo(postDto2);
        postDto2.setId(2L);
        assertThat(postDto1).isNotEqualTo(postDto2);
        postDto1.setId(null);
        assertThat(postDto1).isNotEqualTo(postDto2);
    }
}
