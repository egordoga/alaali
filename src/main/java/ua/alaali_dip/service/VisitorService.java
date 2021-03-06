package ua.alaali_dip.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.alaali_dip.entity.Role;
import ua.alaali_dip.entity.Visitor;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class VisitorService implements UserDetailsService {

    private final ServiceDB serviceDB;

    @Autowired
    public VisitorService(ServiceDB serviceDB) {
        this.serviceDB = serviceDB;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Visitor visitor = serviceDB.findVisitorByEmail(email);
        if (visitor == null) {
            throw new UsernameNotFoundException("Неверно введенный пароль или email");
        }
        return new User(visitor.getEmail(), visitor.getPass(), mapRolesToAuthorities(visitor.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
