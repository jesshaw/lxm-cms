package com.lexiangmiao.cms.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class StaticPageCriteriaTest {

    @Test
    void newStaticPageCriteriaHasAllFiltersNullTest() {
        var staticPageCriteria = new StaticPageCriteria();
        assertThat(staticPageCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void staticPageCriteriaFluentMethodsCreatesFiltersTest() {
        var staticPageCriteria = new StaticPageCriteria();

        setAllFilters(staticPageCriteria);

        assertThat(staticPageCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void staticPageCriteriaCopyCreatesNullFilterTest() {
        var staticPageCriteria = new StaticPageCriteria();
        var copy = staticPageCriteria.copy();

        assertThat(staticPageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(staticPageCriteria)
        );
    }

    @Test
    void staticPageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var staticPageCriteria = new StaticPageCriteria();
        setAllFilters(staticPageCriteria);

        var copy = staticPageCriteria.copy();

        assertThat(staticPageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(staticPageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var staticPageCriteria = new StaticPageCriteria();

        assertThat(staticPageCriteria).hasToString("StaticPageCriteria{}");
    }

    private static void setAllFilters(StaticPageCriteria staticPageCriteria) {
        staticPageCriteria.id();
        staticPageCriteria.title();
        staticPageCriteria.status();
        staticPageCriteria.categoryId();
        staticPageCriteria.distinct();
    }

    private static Condition<StaticPageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<StaticPageCriteria> copyFiltersAre(StaticPageCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
