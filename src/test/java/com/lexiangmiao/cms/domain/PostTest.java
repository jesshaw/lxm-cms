package com.lexiangmiao.cms.domain;

import static com.lexiangmiao.cms.domain.CategoryTestSamples.*;
import static com.lexiangmiao.cms.domain.PostTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lexiangmiao.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PostTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Post.class);
        Post post1 = getPostSample1();
        Post post2 = new Post();
        assertThat(post1).isNotEqualTo(post2);

        post2.setId(post1.getId());
        assertThat(post1).isEqualTo(post2);

        post2 = getPostSample2();
        assertThat(post1).isNotEqualTo(post2);
    }

    @Test
    void categoryTest() throws Exception {
        Post post = getPostRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        post.setCategory(categoryBack);
        assertThat(post.getCategory()).isEqualTo(categoryBack);

        post.category(null);
        assertThat(post.getCategory()).isNull();
    }
}
