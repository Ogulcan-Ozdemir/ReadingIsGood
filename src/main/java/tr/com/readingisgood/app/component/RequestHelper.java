package tr.com.readingisgood.app.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import tr.com.readingisgood.app.model.user.User;
import tr.com.readingisgood.app.service.user.UserService;

@Component
public class RequestHelper {

    @Autowired
    private UserService userService;

    public User getRequestUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userService.findByEmail(email);
        Assert.notNull(user, "user not found");
        return user;
    }
}
