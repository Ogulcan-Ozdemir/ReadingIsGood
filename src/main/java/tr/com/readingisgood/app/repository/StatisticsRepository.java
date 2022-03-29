package tr.com.readingisgood.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tr.com.readingisgood.app.model.order.BookOrder;

import java.util.List;

public interface StatisticsRepository extends JpaRepository<BookOrder, Long> {

//    @Query("select b from BookOrder b GROUP BY month(b.orderedAt)")
//    List<BookOrder> getMonthlyOrder();

    @Query(value = "SELECT year(BO.ORDERED_AT), monthname(BO.ORDERED_AT), BO.ID\n" +
            "FROM BOOK_ORDER BO\n" +
            "GROUP BY year(BO.ORDERED_AT), monthname(BO.ORDERED_AT), BO.ID;", nativeQuery = true)
    List<BookOrder> getMonthlyOrder();

}
