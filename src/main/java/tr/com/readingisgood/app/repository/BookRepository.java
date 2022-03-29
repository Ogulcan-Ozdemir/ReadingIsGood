package tr.com.readingisgood.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.com.readingisgood.app.model.book.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b  where b.name in :name")
    Book findByName(@Param("name") String name);

}
