package tr.com.readingisgood.app.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tr.com.readingisgood.app.ReadingIsGoodApplication;
import tr.com.readingisgood.app.model.user.UserDTO;
import tr.com.readingisgood.app.model.security.GrantedAuthRoles;
import tr.com.readingisgood.app.model.user.User;
import tr.com.readingisgood.app.repository.UserRepository;
import tr.com.readingisgood.app.service.user.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static tr.com.readingisgood.app.misc.IntegrationHelpers.getRandomUserDTO;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ReadingIsGoodApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test-integration.properties")
public class CustomerControllerIntegrationTest {

    private static final String CUSTOMER_USER_URL_PREFIX = "/auth/user/customer";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void givenCustomerUser_whenRegister_thenStatus200() throws Exception {
        UserDTO registeringUser = getRandomUserDTO();

        ResultActions resultAction = mvc.perform(post(CUSTOMER_USER_URL_PREFIX + "/register")
                .content(objectMapper.writeValueAsString(registeringUser))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(status().isOk());

        User registeredUser = userRepository.findByEmail(registeringUser.getEmail());
        assertEquals(registeringUser.getEmail(), registeredUser.getEmail());
        assertEquals(List.of(GrantedAuthRoles.CUSTOMER), registeredUser.getRoles());
    }

    @Test
    public void givenCustomerUser_whenAlreadyRegister_thenLoginShouldSuccess() throws Exception {
        UserDTO registeringUser = getRandomUserDTO();
        userService.register(registeringUser, List.of(GrantedAuthRoles.CUSTOMER));

        ResultActions resultAction = mvc.perform(post(CUSTOMER_USER_URL_PREFIX + "/login")
                .content(objectMapper.writeValueAsString(registeringUser))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(status().isOk());
    }

    @Test
    public void givenCustomerUser_whenAlreadyRegisterAndLogin_thenLogoutShouldSuccess() throws Exception {
        UserDTO registeringUser = getRandomUserDTO();
        userService.register(registeringUser, List.of(GrantedAuthRoles.CUSTOMER));
        String token = userService.login(registeringUser);

        ResultActions resultAction = mvc.perform(post(CUSTOMER_USER_URL_PREFIX + "/logout")
                .content(token)
                .contentType(MediaType.TEXT_PLAIN_VALUE));

        resultAction.andExpect(status().isOk());
    }

}
