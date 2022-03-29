package tr.com.readingisgood.app.model.book;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BookRespDTO {

    private Long id;
    private String name;
    private String authorName;
    private LocalDate publishedAt;
    private BigDecimal price;
    private String genre;

    public BookRespDTO(Book book) {
        this.id = book.getId();
        this.name = book.getName();
        this.authorName = book.getAuthorName();
        this.publishedAt = book.getPublishedAt();
        this.price = book.getPrice();
        this.genre = book.getGenre().getName();
    }
}
