package tr.com.readingisgood.app.service.order;

import tr.com.readingisgood.app.model.order.BookOrder;
import tr.com.readingisgood.app.model.order.BookOrderReqDTO;
import tr.com.readingisgood.app.model.user.User;

import java.time.LocalDate;
import java.util.List;

public interface BookOrderService {

    Long orderNew(BookOrderReqDTO bookOrderReqDTO, User orderedUser);

    Long orderNew(BookOrderReqDTO bookOrderReqDTO, User orderedUser, LocalDate storedAt);

    BookOrder findOrderById(Long orderId);

    List<BookOrder> findOrdersByInterval(LocalDate startDate, LocalDate endDate);

    List<BookOrder> findOrderByCustomerId(Long customerId);

}
