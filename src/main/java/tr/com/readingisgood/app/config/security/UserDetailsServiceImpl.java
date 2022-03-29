package tr.com.readingisgood.app.config.security;

import tr.com.readingisgood.app.model.security.GrantedAuthRoles;
import tr.com.readingisgood.app.model.user.User;
import tr.com.readingisgood.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        return org.springframework.security.core.userdetails.User.withUsername(email)
                .password(user.getPassword())
                .accountExpired(false)
                .accountLocked(false)
                .roles(String.valueOf(user.getRoles().stream().map(GrantedAuthRoles::getAuthority)))
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
