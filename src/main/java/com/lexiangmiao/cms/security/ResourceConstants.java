package com.lexiangmiao.cms.security;

import com.lexiangmiao.cms.domain.DictionaryEntry;
import java.util.Arrays;
import java.util.List;

public final class ResourceConstants {

    public static final String AUTHORITY = "AUTHORITY";
    public static final String RESOURCE = "RESOURCE";
    public static final String EMPLOYEE = "EMPLOYEE";
    public static final String CATEGORY = "CATEGORY";
    public static final String STATIC_PAGE = "STATIC_PAGE";
    public static final String POST = "POST";

    // jhipster-needle-add-entity-to-resource-constants - Jhipster will add entities to the resource constants here

    private ResourceConstants() {}

    protected enum Resource {
        AUTHORITY(ResourceConstants.AUTHORITY),
        RESOURCE(ResourceConstants.RESOURCE),
        EMPLOYEE(ResourceConstants.EMPLOYEE),
        CATEGORY(ResourceConstants.CATEGORY),
        STATIC_PAGE(ResourceConstants.STATIC_PAGE),
        POST(ResourceConstants.POST);

        // jhipster-needle-add-entity-to-resource-enum - Jhipster will add entities to the resource enum here

        private final String name;

        Resource(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static List<DictionaryEntry> getAllResources() {
        return Arrays.stream(Resource.values()).map(o -> new DictionaryEntry(o.getName(), o.getName())).toList();
    }
}
