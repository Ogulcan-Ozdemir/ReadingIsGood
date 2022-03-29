package tr.com.readingisgood.app.service.jwt;

import tr.com.readingisgood.app.model.security.Jwt;
import tr.com.readingisgood.app.model.user.User;

import java.util.Date;
import java.util.List;

public interface JwtService {

    void expireToken(String token);

    void insertToken(User user, String token, Date issuedAt, Date expiredAt);

    boolean isTokenValid(String token);

    List<Jwt> getUserValidTokens(User user);
}
