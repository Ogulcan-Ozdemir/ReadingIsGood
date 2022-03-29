package tr.com.readingisgood.app.repository;

import tr.com.readingisgood.app.model.security.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JwtRepository extends JpaRepository<Jwt, Long> {

    @Query("select j from Jwt j  where j.token in :token")
    Jwt findByToken(@Param("token") String token);

    @Query("select j from Jwt j where j.user.email in :email")
    List<Jwt> findTokensByUserEmail(@Param("email") String email);

}