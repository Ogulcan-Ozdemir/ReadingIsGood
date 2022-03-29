package tr.com.readingisgood.app.service.warehouse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import tr.com.readingisgood.app.component.BookMapper;
import tr.com.readingisgood.app.model.book.BookStockDTO;
import tr.com.readingisgood.app.model.warehouse.Warehouse;
import tr.com.readingisgood.app.model.book.Book;
import tr.com.readingisgood.app.repository.warehouse.WarehouseRepository;
import tr.com.readingisgood.app.service.book.BookService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private BookService bookService;

    public void addToStock(BookStockDTO bookStockDTO){
        Book book = bookService.findByName(bookStockDTO.getName());
        Assert.notNull(book, "Book named " + bookStockDTO.getName() + " not in store");
        IntStream.range(0, bookStockDTO.getCount()).forEach(value -> {
            warehouseRepository.save(new Warehouse(book, Instant.now()));
        });
    }

    @Override
    public HashMap<String, Integer> findBookStocks() {
        List<Warehouse> warehouseStock = warehouseRepository.findAll().stream().filter(stock -> Objects.isNull(stock.getOrder())).collect(Collectors.toList());
        HashMap<String, Integer> stockDTO = new HashMap<>();
        for(Warehouse warehouse : warehouseStock){
            String bookName = warehouse.getBook().getName();
            if(stockDTO.containsKey(bookName)){
                stockDTO.compute(bookName, (s, stockCount) -> stockCount + 1);
            }else {
                stockDTO.put(bookName, 1);
            }
        }
        return stockDTO;
    }

    @Override
    public BookStockDTO findStockByName(String bookName) {
        List<Warehouse> warehouseEntry = warehouseRepository.findStockByBookName(bookName);
        return BookMapper.toBookStockDTO(bookName, warehouseEntry.size());
    }

}
