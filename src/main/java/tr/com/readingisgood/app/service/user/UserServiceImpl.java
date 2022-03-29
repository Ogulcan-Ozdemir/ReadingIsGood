package tr.com.readingisgood.app.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tr.com.readingisgood.app.config.security.jwt.JwtProvider;
import tr.com.readingisgood.app.model.exception.ApplicationException;
import tr.com.readingisgood.app.model.security.GrantedAuthRoles;
import tr.com.readingisgood.app.model.security.Jwt;
import tr.com.readingisgood.app.model.user.User;
import tr.com.readingisgood.app.model.user.UserDTO;
import tr.com.readingisgood.app.repository.UserRepository;
import tr.com.readingisgood.app.service.jwt.JwtService;

import java.util.Date;
import java.util.List;

@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void register(UserDTO userDTO, List<GrantedAuthRoles> roles) {
        User user = userRepository.findByEmail(userDTO.getEmail());
        if (user != null) {
            // TODO IF exception says this email name already registered in PROD. Attackers can extract registered emails
            throw new ApplicationException("email already registered");
        }
        userRepository.saveAndFlush(new User(userDTO.getEmail(),
                passwordEncoder.encode(userDTO.getPassword()),
                roles));
    }

    @Override
    public String login(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail());
        if (user == null) {
            throw new ApplicationException("Invalid email/password supplied");
        }
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new ApplicationException("Invalid email/password supplied");
        }

        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime() + JwtProvider.validityInMilliseconds);

        String token = jwtProvider.createToken(user, issuedAt, expiredAt);
        jwtService.insertToken(user, token, issuedAt, expiredAt);
        return token;
    }

    @Override
    public void logout(String token) {
        jwtService.expireToken(token);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean isAlreadyLogined(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail());
        if (user == null) {
            throw new ApplicationException("Invalid email/password supplied");
        }
        List<Jwt> jwts = jwtService.getUserValidTokens(user);
        return !jwts.isEmpty();
    }

}
