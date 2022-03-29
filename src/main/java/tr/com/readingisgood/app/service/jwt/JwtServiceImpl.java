package tr.com.readingisgood.app.service.jwt;

import tr.com.readingisgood.app.repository.JwtRepository;
import tr.com.readingisgood.app.model.exception.ApplicationException;
import tr.com.readingisgood.app.model.security.Jwt;
import tr.com.readingisgood.app.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class JwtServiceImpl implements JwtService{

    @Autowired
    private JwtRepository jwtRepository;

    public void expireToken(String token){
        Jwt jwt = jwtRepository.findByToken(token);
        if(jwt == null){
            throw new ApplicationException("Token not in repo");
        }
        Timestamp now = convertToTimeStamp(new Date());
        if(jwt.getExpiration().before(now)){
            throw new ApplicationException("Token already expired");
        }
        jwt.setExpiration(convertToTimeStamp(now));
        jwtRepository.save(jwt);
    }

    public void insertToken(User user, String token, Date issuedAt, Date expiredAt){
        Jwt jwt = jwtRepository.findByToken(token);
        if(jwt != null){
            throw new ApplicationException("Token already in repo");
        }
        jwtRepository.save(new Jwt(user, token, convertToTimeStamp(issuedAt), convertToTimeStamp(expiredAt)));
    }

    @Override
    public boolean isTokenValid(String token) {
        Jwt jwt = jwtRepository.findByToken(token);
        if(Objects.isNull(jwt)){
            throw new ApplicationException("Token " + token + " not in repo");
        }

        Timestamp now = Timestamp.from(Instant.now());
        return jwt.getIssuedAt().before(now) && jwt.getExpiration().after(now);
    }

    @Override
    public List<Jwt> getUserValidTokens(User user) {
        List<Jwt> jwts = jwtRepository.findTokensByUserEmail(user.getEmail());
        return jwts.stream().filter(jwt -> isTokenValid(jwt.getToken())).collect(Collectors.toList());
    }

    private static Timestamp convertToTimeStamp(Date date){
        return new Timestamp(date.toInstant().toEpochMilli());
    }

}
