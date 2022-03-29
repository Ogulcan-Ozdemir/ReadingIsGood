package tr.com.readingisgood.app.repository.warehouse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.com.readingisgood.app.model.warehouse.Warehouse;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    @Query("select w from Warehouse w where w.book.id in :id")
    Warehouse findStockByBookId(@Param("id") Long id);

    @Query("select w from Warehouse w, Book b  where w.book.id = b.id and b.name in :name")
    List<Warehouse> findStockByBookName(@Param("name") String name);

}
