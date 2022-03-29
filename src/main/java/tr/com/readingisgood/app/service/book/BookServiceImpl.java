package tr.com.readingisgood.app.service.book;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import tr.com.readingisgood.app.model.book.Book;
import tr.com.readingisgood.app.repository.BookRepository;

import java.util.List;

@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book findByName(String name) {
        return bookRepository.findByName(name);
    }

    @Override
    public void save(Book book) {
        Assert.isNull(findByName(book.getName()), "Book named " + book.getName() + " already saved");
        bookRepository.saveAndFlush(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
