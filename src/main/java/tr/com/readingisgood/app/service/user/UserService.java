package tr.com.readingisgood.app.service.user;

import tr.com.readingisgood.app.model.security.GrantedAuthRoles;
import tr.com.readingisgood.app.model.user.User;
import tr.com.readingisgood.app.model.user.UserDTO;

import java.util.List;

public interface UserService {

    void register(UserDTO userDTO, List<GrantedAuthRoles> roles);

    String login(UserDTO userDTO);

    void logout(String token);

    User findByEmail(String email);

    boolean isAlreadyLogined(UserDTO userDTO);

}
