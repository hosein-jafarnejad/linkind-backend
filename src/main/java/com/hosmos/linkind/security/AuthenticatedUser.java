package com.hosmos.linkind.security;

import com.hosmos.linkind.models.UserWithPassword;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AuthenticatedUser implements UserDetails, CredentialsContainer {

    private UserWithPassword user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
//    private List<SimpleGrantedAuthority> privileges;


    public AuthenticatedUser(UserWithPassword user) {
        this.user = user;
//        this.privileges = initialPrivileges(user.getRoles());
    }

//    public List<SimpleGrantedAuthority> getPrivileges() {
//        return privileges;
//    }

//    private List<SimpleGrantedAuthority> initialPrivileges(List<DTORolePure> roles) {
//        List<SimpleGrantedAuthority> privileges = new ArrayList<>();
//        List<DTOAccessNode> accessNodes = new ArrayList<>();
//
//        for (DTORolePure rolePure : roles) {
//            accessNodes.addAll(rolePure.getAccessNodes());
//        }
//
//        for (DTOAccessNode accessNode : accessNodes) {
//            privileges.add(new SimpleGrantedAuthority(accessNode.getKey()));
//        }
//
//        return privileges;
//    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//
//        for (DTORolePure role : user.getRoles()) {
//            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getKey()));
//        }
//
//        return authorities;
//    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getMail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getActivationDate() != null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getActivationDate() != null;
    }

    @Override
    public void eraseCredentials() {
        this.user.setPassword(null);
    }

    public long getId() {
        return user.getId();
    }

    public boolean isAdmin() {
        return true;
    }
}
