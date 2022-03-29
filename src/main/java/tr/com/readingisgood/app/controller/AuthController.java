package tr.com.readingisgood.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.readingisgood.app.model.user.UserDTO;
import tr.com.readingisgood.app.service.user.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth/user/customer")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> login(@Valid @RequestBody UserDTO userDTO) {
        String jwt = userService.login(userDTO);
        return ResponseEntity.ok().body(jwt);
    }

    @PostMapping(path = "/logout", consumes = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<Void> logout(@RequestBody String token) {
        userService.logout(token);
        return ResponseEntity.ok().build();
    }

}
