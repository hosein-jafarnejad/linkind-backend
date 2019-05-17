package ir.projects.learner.security;

import ir.projects.learner.model.dto.accessnode.DTOAccessNode;
import ir.projects.learner.model.dto.role.DTORolePure;
import ir.projects.learner.model.dto.user.DTOUserPure;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AuthenticatedUser implements UserDetails, CredentialsContainer {

    private DTOUserPure user;
    private List<SimpleGrantedAuthority> privileges;


    public AuthenticatedUser(DTOUserPure user) {
        this.user = user;
        this.privileges = initialPrivileges(user.getRoles());
    }

    public List<SimpleGrantedAuthority> getPrivileges() {
        return privileges;
    }

    private List<SimpleGrantedAuthority> initialPrivileges(List<DTORolePure> roles) {
        List<SimpleGrantedAuthority> privileges = new ArrayList<>();
        List<DTOAccessNode> accessNodes = new ArrayList<>();

        for (DTORolePure rolePure : roles) {
            accessNodes.addAll(rolePure.getAccessNodes());
        }

        for (DTOAccessNode accessNode : accessNodes) {
            privileges.add(new SimpleGrantedAuthority(accessNode.getKey()));
        }

        return privileges;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (DTORolePure role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getKey()));
        }

        return authorities;
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }

    @Override
    public void eraseCredentials() {
        this.user.setPassword(null);
    }

    public long getId() {
        return user.getId();
    }

    public boolean isAdmin() {
        return user.getAdmin();
    }
}
