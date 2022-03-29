package tr.com.readingisgood.app.service.warehouse;

import tr.com.readingisgood.app.model.book.BookStockDTO;

import java.util.HashMap;

public interface WarehouseService {

    void addToStock(BookStockDTO bookStockDTO);

    HashMap<String, Integer> findBookStocks();

    BookStockDTO findStockByName(String bookName);

}
