package com.lexiangmiao.cms.security;

import com.lexiangmiao.cms.domain.Resource;
import java.util.Collection;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User {

    private Set<Resource> resources;

    public CustomUserDetails(
        String username,
        String password,
        Collection<? extends GrantedAuthority> authorities,
        Set<Resource> resources
    ) {
        super(username, password, authorities);
        this.resources = resources;
    }

    public Set<Resource> getResources() {
        return resources;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }
}
