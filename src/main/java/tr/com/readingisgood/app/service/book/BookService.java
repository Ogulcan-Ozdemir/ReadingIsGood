package tr.com.readingisgood.app.service.book;

import tr.com.readingisgood.app.model.book.Book;

import java.util.List;

public interface BookService {

    Book findByName(String name);

    void save(Book book);

    List<Book> findAll();

}
