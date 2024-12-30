package com.lexiangmiao.cms.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PostCriteriaTest {

    @Test
    void newPostCriteriaHasAllFiltersNullTest() {
        var postCriteria = new PostCriteria();
        assertThat(postCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void postCriteriaFluentMethodsCreatesFiltersTest() {
        var postCriteria = new PostCriteria();

        setAllFilters(postCriteria);

        assertThat(postCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void postCriteriaCopyCreatesNullFilterTest() {
        var postCriteria = new PostCriteria();
        var copy = postCriteria.copy();

        assertThat(postCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(postCriteria)
        );
    }

    @Test
    void postCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var postCriteria = new PostCriteria();
        setAllFilters(postCriteria);

        var copy = postCriteria.copy();

        assertThat(postCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(postCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var postCriteria = new PostCriteria();

        assertThat(postCriteria).hasToString("PostCriteria{}");
    }

    private static void setAllFilters(PostCriteria postCriteria) {
        postCriteria.id();
        postCriteria.title();
        postCriteria.status();
        postCriteria.categoryId();
        postCriteria.distinct();
    }

    private static Condition<PostCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCategoryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PostCriteria> copyFiltersAre(PostCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getCategoryId(), copy.getCategoryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
