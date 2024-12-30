package com.lexiangmiao.cms.web.rest;

import static com.lexiangmiao.cms.domain.StaticPageAsserts.*;
import static com.lexiangmiao.cms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lexiangmiao.cms.IntegrationTest;
import com.lexiangmiao.cms.domain.Category;
import com.lexiangmiao.cms.domain.StaticPage;
import com.lexiangmiao.cms.repository.StaticPageRepository;
import com.lexiangmiao.cms.service.StaticPageService;
import com.lexiangmiao.cms.service.dto.StaticPageDto;
import com.lexiangmiao.cms.service.mapper.StaticPageMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StaticPageResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StaticPageResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/static-pages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StaticPageRepository staticPageRepository;

    @Mock
    private StaticPageRepository staticPageRepositoryMock;

    @Autowired
    private StaticPageMapper staticPageMapper;

    @Mock
    private StaticPageService staticPageServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStaticPageMockMvc;

    private StaticPage staticPage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StaticPage createEntity(EntityManager em) {
        StaticPage staticPage = new StaticPage().title(DEFAULT_TITLE).content(DEFAULT_CONTENT).status(DEFAULT_STATUS);
        return staticPage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StaticPage createUpdatedEntity(EntityManager em) {
        StaticPage staticPage = new StaticPage().title(UPDATED_TITLE).content(UPDATED_CONTENT).status(UPDATED_STATUS);
        return staticPage;
    }

    @BeforeEach
    public void initTest() {
        staticPage = createEntity(em);
    }

    @Test
    @Transactional
    void createStaticPage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StaticPage
        StaticPageDto staticPageDto = staticPageMapper.toDto(staticPage);
        var returnedStaticPageDto = om.readValue(
            restStaticPageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(staticPageDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StaticPageDto.class
        );

        // Validate the StaticPage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStaticPage = staticPageMapper.toEntity(returnedStaticPageDto);
        assertStaticPageUpdatableFieldsEquals(returnedStaticPage, getPersistedStaticPage(returnedStaticPage));
    }

    @Test
    @Transactional
    void createStaticPageWithExistingId() throws Exception {
        // Create the StaticPage with an existing ID
        staticPage.setId(1L);
        StaticPageDto staticPageDto = staticPageMapper.toDto(staticPage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStaticPageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(staticPageDto)))
            .andExpect(status().isBadRequest());

        // Validate the StaticPage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStaticPages() throws Exception {
        // Initialize the database
        staticPageRepository.saveAndFlush(staticPage);

        // Get all the staticPageList
        restStaticPageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(staticPage.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStaticPagesWithEagerRelationshipsIsEnabled() throws Exception {
        when(staticPageServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStaticPageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(staticPageServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStaticPagesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(staticPageServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStaticPageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(staticPageRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getStaticPage() throws Exception {
        // Initialize the database
        staticPageRepository.saveAndFlush(staticPage);

        // Get the staticPage
        restStaticPageMockMvc
            .perform(get(ENTITY_API_URL_ID, staticPage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(staticPage.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getStaticPagesByIdFiltering() throws Exception {
        // Initialize the database
        staticPageRepository.saveAndFlush(staticPage);

        Long id = staticPage.getId();

        defaultStaticPageFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultStaticPageFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultStaticPageFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStaticPagesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        staticPageRepository.saveAndFlush(staticPage);

        // Get all the staticPageList where title equals to
        defaultStaticPageFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllStaticPagesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        staticPageRepository.saveAndFlush(staticPage);

        // Get all the staticPageList where title in
        defaultStaticPageFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllStaticPagesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        staticPageRepository.saveAndFlush(staticPage);

        // Get all the staticPageList where title is not null
        defaultStaticPageFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllStaticPagesByTitleContainsSomething() throws Exception {
        // Initialize the database
        staticPageRepository.saveAndFlush(staticPage);

        // Get all the staticPageList where title contains
        defaultStaticPageFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllStaticPagesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        staticPageRepository.saveAndFlush(staticPage);

        // Get all the staticPageList where title does not contain
        defaultStaticPageFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllStaticPagesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        staticPageRepository.saveAndFlush(staticPage);

        // Get all the staticPageList where status equals to
        defaultStaticPageFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllStaticPagesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        staticPageRepository.saveAndFlush(staticPage);

        // Get all the staticPageList where status in
        defaultStaticPageFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllStaticPagesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        staticPageRepository.saveAndFlush(staticPage);

        // Get all the staticPageList where status is not null
        defaultStaticPageFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllStaticPagesByStatusContainsSomething() throws Exception {
        // Initialize the database
        staticPageRepository.saveAndFlush(staticPage);

        // Get all the staticPageList where status contains
        defaultStaticPageFiltering("status.contains=" + DEFAULT_STATUS, "status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllStaticPagesByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        staticPageRepository.saveAndFlush(staticPage);

        // Get all the staticPageList where status does not contain
        defaultStaticPageFiltering("status.doesNotContain=" + UPDATED_STATUS, "status.doesNotContain=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllStaticPagesByCategoryIsEqualToSomething() throws Exception {
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            staticPageRepository.saveAndFlush(staticPage);
            category = CategoryResourceIT.createEntity(em);
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        em.persist(category);
        em.flush();
        staticPage.setCategory(category);
        staticPageRepository.saveAndFlush(staticPage);
        Long categoryId = category.getId();
        // Get all the staticPageList where category equals to categoryId
        defaultStaticPageShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the staticPageList where category equals to (categoryId + 1)
        defaultStaticPageShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    private void defaultStaticPageFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultStaticPageShouldBeFound(shouldBeFound);
        defaultStaticPageShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStaticPageShouldBeFound(String filter) throws Exception {
        restStaticPageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(staticPage.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));

        // Check, that the count call also returns 1
        restStaticPageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStaticPageShouldNotBeFound(String filter) throws Exception {
        restStaticPageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStaticPageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStaticPage() throws Exception {
        // Get the staticPage
        restStaticPageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStaticPage() throws Exception {
        // Initialize the database
        staticPageRepository.saveAndFlush(staticPage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the staticPage
        StaticPage updatedStaticPage = staticPageRepository.findById(staticPage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStaticPage are not directly saved in db
        em.detach(updatedStaticPage);
        updatedStaticPage.title(UPDATED_TITLE).content(UPDATED_CONTENT).status(UPDATED_STATUS);
        StaticPageDto staticPageDto = staticPageMapper.toDto(updatedStaticPage);

        restStaticPageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, staticPageDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(staticPageDto))
            )
            .andExpect(status().isOk());

        // Validate the StaticPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStaticPageToMatchAllProperties(updatedStaticPage);
    }

    @Test
    @Transactional
    void putNonExistingStaticPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        staticPage.setId(longCount.incrementAndGet());

        // Create the StaticPage
        StaticPageDto staticPageDto = staticPageMapper.toDto(staticPage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaticPageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, staticPageDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(staticPageDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaticPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStaticPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        staticPage.setId(longCount.incrementAndGet());

        // Create the StaticPage
        StaticPageDto staticPageDto = staticPageMapper.toDto(staticPage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaticPageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(staticPageDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaticPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStaticPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        staticPage.setId(longCount.incrementAndGet());

        // Create the StaticPage
        StaticPageDto staticPageDto = staticPageMapper.toDto(staticPage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaticPageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(staticPageDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StaticPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStaticPageWithPatch() throws Exception {
        // Initialize the database
        staticPageRepository.saveAndFlush(staticPage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the staticPage using partial update
        StaticPage partialUpdatedStaticPage = new StaticPage();
        partialUpdatedStaticPage.setId(staticPage.getId());

        restStaticPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStaticPage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStaticPage))
            )
            .andExpect(status().isOk());

        // Validate the StaticPage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStaticPageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStaticPage, staticPage),
            getPersistedStaticPage(staticPage)
        );
    }

    @Test
    @Transactional
    void fullUpdateStaticPageWithPatch() throws Exception {
        // Initialize the database
        staticPageRepository.saveAndFlush(staticPage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the staticPage using partial update
        StaticPage partialUpdatedStaticPage = new StaticPage();
        partialUpdatedStaticPage.setId(staticPage.getId());

        partialUpdatedStaticPage.title(UPDATED_TITLE).content(UPDATED_CONTENT).status(UPDATED_STATUS);

        restStaticPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStaticPage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStaticPage))
            )
            .andExpect(status().isOk());

        // Validate the StaticPage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStaticPageUpdatableFieldsEquals(partialUpdatedStaticPage, getPersistedStaticPage(partialUpdatedStaticPage));
    }

    @Test
    @Transactional
    void patchNonExistingStaticPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        staticPage.setId(longCount.incrementAndGet());

        // Create the StaticPage
        StaticPageDto staticPageDto = staticPageMapper.toDto(staticPage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaticPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, staticPageDto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(staticPageDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaticPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStaticPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        staticPage.setId(longCount.incrementAndGet());

        // Create the StaticPage
        StaticPageDto staticPageDto = staticPageMapper.toDto(staticPage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaticPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(staticPageDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaticPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStaticPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        staticPage.setId(longCount.incrementAndGet());

        // Create the StaticPage
        StaticPageDto staticPageDto = staticPageMapper.toDto(staticPage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaticPageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(staticPageDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StaticPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStaticPage() throws Exception {
        // Initialize the database
        staticPageRepository.saveAndFlush(staticPage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the staticPage
        restStaticPageMockMvc
            .perform(delete(ENTITY_API_URL_ID, staticPage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return staticPageRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected StaticPage getPersistedStaticPage(StaticPage staticPage) {
        return staticPageRepository.findById(staticPage.getId()).orElseThrow();
    }

    protected void assertPersistedStaticPageToMatchAllProperties(StaticPage expectedStaticPage) {
        assertStaticPageAllPropertiesEquals(expectedStaticPage, getPersistedStaticPage(expectedStaticPage));
    }

    protected void assertPersistedStaticPageToMatchUpdatableProperties(StaticPage expectedStaticPage) {
        assertStaticPageAllUpdatablePropertiesEquals(expectedStaticPage, getPersistedStaticPage(expectedStaticPage));
    }
}
