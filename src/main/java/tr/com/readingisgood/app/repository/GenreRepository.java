package tr.com.readingisgood.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.com.readingisgood.app.model.genre.GENRE;

public interface GenreRepository extends JpaRepository<GENRE, Long> {

    @Query("select g from GENRE g  where g.name in :name")
    GENRE findByName(@Param("name") String name);

}
