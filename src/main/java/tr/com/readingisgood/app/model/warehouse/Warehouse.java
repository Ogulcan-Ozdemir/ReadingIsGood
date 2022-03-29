package tr.com.readingisgood.app.model.warehouse;

import tr.com.readingisgood.app.model.book.Book;
import tr.com.readingisgood.app.model.order.BookOrder;

import javax.persistence.*;
import java.time.Instant;

@Entity
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bookId", referencedColumnName = "id")
    private Book book;

//    @OneToOne
//    @JoinColumn(name = "orderId", referencedColumnName = "id")
//    private BookOrder bookOrder;

    @ManyToOne
    @JoinColumn(name = "orderId", referencedColumnName = "id")
    private BookOrder bookOrder;

    @Column(nullable = false)
    private Instant storedAt;

    public Warehouse() {}

    public Warehouse(Book book, Instant storedAt) {
        this.book = book;
        this.storedAt = storedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public BookOrder getOrder() {
        return bookOrder;
    }

    public void setOrder(BookOrder bookOrder) {
        this.bookOrder = bookOrder;
    }

    public Instant getStoredAt() {
        return storedAt;
    }

    public void setStoredAt(Instant storedAt) {
        this.storedAt = storedAt;
    }
}