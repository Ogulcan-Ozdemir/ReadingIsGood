package tr.com.readingisgood.app.config.security.jwt;

import tr.com.readingisgood.app.model.exception.WebRequestException;
import tr.com.readingisgood.app.model.user.User;
import tr.com.readingisgood.app.config.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import tr.com.readingisgood.app.service.jwt.JwtService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

  // TODO from env
  // @Value("${security.jwt.token.secret-key:secret-key}")
  private String secretKey = "secret-jwt-key";

  // @Value("${security.jwt.token.secret-key:secret-key}")
  public static long validityInMilliseconds = 3600000; // 1h

  @Autowired
  private UserDetailsServiceImpl userDetail;

  @Autowired
  private JwtService jwtService;

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public String createToken(User user) {

    Claims claims = Jwts.claims().setSubject(user.getEmail());
    claims.put("auth",  user.getRoles().stream().map(roles -> new SimpleGrantedAuthority(roles.getAuthority())).collect(Collectors.toList()));

    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  public String createToken(User user, Date now, Date expiredAt) {

    Claims claims = Jwts.claims().setSubject(user.getEmail());
    claims.put("auth",  user.getRoles().stream().map(roles -> new SimpleGrantedAuthority(roles.getAuthority())).collect(Collectors.toList()));

    return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiredAt)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = userDetail.loadUserByUsername(getUsername(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUsername(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
  }

  public String resolveToken(HttpServletRequest req) {
    String bearerToken = req.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      jwtService.isTokenValid(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      throw new WebRequestException("Expired or invalid Jwt token", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public boolean invalidateToken(String token) {
    try {
      Jwts.parser().setSigningKey(secretKey).parse(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      throw new WebRequestException("Expired or invalid Jwt token", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
