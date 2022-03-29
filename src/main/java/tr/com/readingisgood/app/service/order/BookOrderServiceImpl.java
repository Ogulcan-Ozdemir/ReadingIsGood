package tr.com.readingisgood.app.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import tr.com.readingisgood.app.model.exception.ApplicationException;
import tr.com.readingisgood.app.model.order.BookOrder;
import tr.com.readingisgood.app.model.order.BookOrderReqDTO;
import tr.com.readingisgood.app.model.user.User;
import tr.com.readingisgood.app.model.warehouse.Warehouse;
import tr.com.readingisgood.app.repository.warehouse.WarehouseRepositoryAdapter;
import tr.com.readingisgood.app.model.book.Book;
import tr.com.readingisgood.app.repository.BookOrderRepository;
import tr.com.readingisgood.app.repository.BookRepository;
import tr.com.readingisgood.app.repository.warehouse.WarehouseRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class BookOrderServiceImpl implements BookOrderService {

    @Autowired
    private BookOrderRepository bookOrderRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private WarehouseRepositoryAdapter warehouseRepositoryAdapter;

    @Override
    public Long orderNew(BookOrderReqDTO bookOrderReqDTO, User orderedUser) {
        Set<Warehouse> warehouseStockEntries = new HashSet<>();
        for(String bookName : bookOrderReqDTO.getBookNames()){
            Book book = bookRepository.findByName(bookName);
            Assert.notNull(book, "Book " + bookOrderReqDTO.getBookNames() + " not registered yet");

            Warehouse warehouseEntry = warehouseRepositoryAdapter.findStockByBookNameV2(bookName);
            Assert.notNull(warehouseEntry, "Book " + book.getName() + " not in stock");

            warehouseStockEntries.add(warehouseEntry);
        }

        BookOrder order = bookOrderRepository.saveAndFlush(new BookOrder(warehouseStockEntries, orderedUser, LocalDate.now()));

        for(Warehouse warehouseEntry : warehouseStockEntries){
            warehouseEntry.setOrder(order);
            warehouseRepository.saveAndFlush(warehouseEntry);
        }
        return order.getId();
    }

    @Override
    public Long orderNew(BookOrderReqDTO bookOrderReqDTO, User orderedUser, LocalDate storedAt) {
        Set<Warehouse> warehouseStockEntries = new HashSet<>();
        for(String bookName : bookOrderReqDTO.getBookNames()){
            Book book = bookRepository.findByName(bookName);
            Assert.notNull(book, "Book " + bookOrderReqDTO.getBookNames() + " not registered yet");

            Warehouse warehouseEntry = warehouseRepositoryAdapter.findStockByBookNameV2(bookName);
            Assert.notNull(warehouseEntry, "Book " + book.getName() + " not in stock");

            warehouseStockEntries.add(warehouseEntry);
        }

        BookOrder order = bookOrderRepository.saveAndFlush(new BookOrder(warehouseStockEntries, orderedUser, storedAt));

        for(Warehouse warehouseEntry : warehouseStockEntries){
            warehouseEntry.setOrder(order);
            warehouseRepository.saveAndFlush(warehouseEntry);
        }
        return order.getId();
    }

    @Override
    public BookOrder findOrderById(Long orderId) {
        Optional<BookOrder> order = bookOrderRepository.findById(orderId);
        if(order.isEmpty()){
            throw new ApplicationException("Order " + orderId + " not found");
        }
        return order.get();
    }

    @Override
    public List<BookOrder> findOrdersByInterval(LocalDate startDate, LocalDate endDate) {
        return bookOrderRepository.findByDateIntervals(startDate, endDate);
    }

    @Override
    public List<BookOrder> findOrderByCustomerId(Long customerId) {
        return bookOrderRepository.findBookOrderByCustomerId(customerId);
    }

}
