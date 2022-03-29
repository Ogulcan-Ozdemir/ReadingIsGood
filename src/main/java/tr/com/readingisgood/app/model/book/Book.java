package tr.com.readingisgood.app.model.book;

import tr.com.readingisgood.app.model.warehouse.Warehouse;
import tr.com.readingisgood.app.model.genre.GENRE;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String authorName;

    @Column(nullable = false)
    private LocalDate publishedAt;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "genreId", referencedColumnName = "id")
    private GENRE genre;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    private List<Warehouse> warehouse;

    public Book() {}

    public Book(String name, String authorName, LocalDate publishedAt, BigDecimal price, GENRE genre) {
        this.name = name;
        this.authorName = authorName;
        this.publishedAt = publishedAt;
        this.price = price;
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public LocalDate getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDate publishedAt) {
        this.publishedAt = publishedAt;
    }

    public GENRE getGenre() {
        return genre;
    }

    public void setGenre(GENRE genre) {
        this.genre = genre;
    }

    public List<Warehouse> getWarehouseEntry() {
        return warehouse;
    }

    public void setWarehouseEntry(List<Warehouse> warehouse) {
        this.warehouse = warehouse;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
