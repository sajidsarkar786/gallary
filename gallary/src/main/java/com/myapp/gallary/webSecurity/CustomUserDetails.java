package com.myapp.gallary.webSecurity;

import com.myapp.gallary.repository.UserRepository;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetails implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetails(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return userRepository.findByName(name)
                             .map(user -> new User(user.getName(), user.getPassword(), user.isEnabled(),
                        user.isEnabled(), user.isEnabled(), user.isEnabled(),
                        AuthorityUtils.createAuthorityList("USER")
                ))
                             .orElseThrow(() -> new UsernameNotFoundException("can't find"));
    }
}
