package tr.com.readingisgood.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.com.readingisgood.app.model.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select c from User c  where c.email in :email")
    User findByEmail(@Param("email") String email);

}
