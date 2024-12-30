package com.lexiangmiao.cms.repository;

import com.lexiangmiao.cms.domain.StaticPage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StaticPage entity.
 */
@Repository
public interface StaticPageRepository extends JpaRepository<StaticPage, Long>, JpaSpecificationExecutor<StaticPage> {
    default Optional<StaticPage> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<StaticPage> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<StaticPage> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select staticPage from StaticPage staticPage left join fetch staticPage.category",
        countQuery = "select count(staticPage) from StaticPage staticPage"
    )
    Page<StaticPage> findAllWithToOneRelationships(Pageable pageable);

    @Query("select staticPage from StaticPage staticPage left join fetch staticPage.category")
    List<StaticPage> findAllWithToOneRelationships();

    @Query("select staticPage from StaticPage staticPage left join fetch staticPage.category where staticPage.id =:id")
    Optional<StaticPage> findOneWithToOneRelationships(@Param("id") Long id);
}
