package io.github.eduoneproject.eduone.business.kernel.security.auth;

import io.github.eduoneproject.eduone.business.kernel.security.model.Principal;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

/**
 * 认证主体
 *
 * @author summerain0
 */
public class PrincipalAuthenticationToken extends AbstractAuthenticationToken {
    /**
     * 认证主体
     */
    private final Principal principal;

    /**
     * 已认证构造
     *
     * @param principal   认证主体
     * @param authorities 认证权限
     */
    public PrincipalAuthenticationToken(Principal principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        setAuthenticated(true);
    }

    /**
     * 未认证构造
     *
     * @param principal 认证主体
     */
    public PrincipalAuthenticationToken(Principal principal) {
        super(Collections.emptyList());
        this.principal = principal;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}