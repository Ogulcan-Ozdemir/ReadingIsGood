package tr.com.readingisgood.app.component;

import org.springframework.stereotype.Component;
import tr.com.readingisgood.app.model.book.BookRespDTO;
import tr.com.readingisgood.app.model.book.BookStockDTO;
import tr.com.readingisgood.app.model.book.Book;
import tr.com.readingisgood.app.model.book.BookSaveDTO;
import tr.com.readingisgood.app.model.genre.GENRE;
import tr.com.readingisgood.app.repository.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    private final GenreRepository genreRepository;

    public BookMapper(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Book toEntity(BookSaveDTO bookSaveDTO) {
        GENRE genre = genreRepository.findByName(bookSaveDTO.getGenre());
        return new Book(bookSaveDTO.getName(), bookSaveDTO.getAuthorName(), bookSaveDTO.getPublishedAt(), bookSaveDTO.getPrice(), genre);
    }

    public static BookSaveDTO toBookSaveDTO(Book book) {
        return new BookSaveDTO(book.getName(), book.getAuthorName(), book.getPublishedAt(), book.getPrice(), book.getGenre().getName());
    }

    public static BookStockDTO toBookStockDTO(Book book, Integer count) {
        return new BookStockDTO(book.getName(), count);
    }

    public static BookStockDTO toBookStockDTO(String bookName, Integer count) {
        return new BookStockDTO(bookName, count);
    }

    public static BookRespDTO toBookRespDTO(Book book){
        return new BookRespDTO(book);
    }

    public static List<BookRespDTO> toBookRespDTO(List<Book> books){
        return books.stream().map(BookMapper::toBookRespDTO).collect(Collectors.toList());
    }

}
