package tr.com.readingisgood.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.com.readingisgood.app.model.order.BookOrder;

import java.time.LocalDate;
import java.util.List;

public interface BookOrderRepository extends JpaRepository<BookOrder, Long> {

    @Query(value = "SELECT BO.* FROM BOOK_ORDER BO, WAREHOUSE W, BOOK B WHERE BO.WAREHOUSE_ID = W.ID AND W.BOOK_ID = B.ID AND B.NAME = :bookName", nativeQuery = true)
    List<BookOrder> findByBookName(@Param("bookName") String bookName);

    @Query("Select BO FROM BookOrder BO WHERE BO.orderedAt BETWEEN :startDate and :endDate ORDER BY BO.id desc")
    List<BookOrder> findByDateIntervals(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("Select BO FROM BookOrder BO WHERE BO.orderedUser.id = :customerId")
    List<BookOrder> findBookOrderByCustomerId(@Param("customerId") Long customerId);

}