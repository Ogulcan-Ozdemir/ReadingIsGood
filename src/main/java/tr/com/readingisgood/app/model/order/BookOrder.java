package tr.com.readingisgood.app.model.order;

import tr.com.readingisgood.app.model.user.User;
import tr.com.readingisgood.app.model.warehouse.Warehouse;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class BookOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToOne
//    @JoinColumn(name = "warehouseId", referencedColumnName = "id")
//    private Warehouse warehouseStock;

    @OneToMany(mappedBy="bookOrder")
    private Set<Warehouse> warehouseStockEntries;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User orderedUser;

    @Column(nullable = false)
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate orderedAt;

    public BookOrder() {}

    public BookOrder(Set<Warehouse> warehouseStockEntries, User orderedUser, LocalDate orderedAt) {
        this.warehouseStockEntries = warehouseStockEntries;
        this.orderedUser = orderedUser;
        this.orderedAt = orderedAt;
    }

//    public BookOrder(Warehouse warehouseStock, User orderedUser, LocalDate orderedAt) {
//        this.warehouseStock = warehouseStock;
//        this.orderedUser = orderedUser;
//        this.orderedAt = orderedAt;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Warehouse getWarehouseStock() {
//        return warehouseStock;
//    }
//
//    public void setWarehouseStock(Warehouse warehouseStock) {
//        this.warehouseStock = warehouseStock;
//    }

    public LocalDate getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(LocalDate orderedAt) {
        this.orderedAt = orderedAt;
    }

    public User getOrderedCustomer() {
        return orderedUser;
    }

    public void setOrderedCustomer(User orderedUser) {
        this.orderedUser = orderedUser;
    }

    public Set<Warehouse> getWarehouseStockEntries() {
        return warehouseStockEntries;
    }

    public void setWarehouseStockEntries(Set<Warehouse> warehouseStockEntries) {
        this.warehouseStockEntries = warehouseStockEntries;
    }

}
