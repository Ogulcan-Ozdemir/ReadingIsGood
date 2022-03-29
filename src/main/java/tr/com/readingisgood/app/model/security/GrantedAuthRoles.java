package tr.com.readingisgood.app.model.security;

import org.springframework.security.core.GrantedAuthority;

public enum GrantedAuthRoles implements GrantedAuthority {

    ADMIN, CUSTOMER;

    public String getAuthority() {
        return name();
    }


}
