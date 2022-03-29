package tr.com.readingisgood.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.readingisgood.app.component.BookMapper;
import tr.com.readingisgood.app.component.ResponseHelper;
import tr.com.readingisgood.app.model.BaseRespDTO;
import tr.com.readingisgood.app.model.book.BookRespDTO;
import tr.com.readingisgood.app.model.book.BookStockDTO;
import tr.com.readingisgood.app.model.book.BookSaveDTO;
import tr.com.readingisgood.app.service.book.BookService;
import tr.com.readingisgood.app.service.warehouse.WarehouseService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

import static tr.com.readingisgood.app.component.ResponseHelper.getResponseEntity;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private BookMapper bookMapper;

    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<BaseRespDTO<?>> register(@Valid @RequestBody BookSaveDTO bookSaveDTO) {
        bookService.save(bookMapper.toEntity(bookSaveDTO));
        return ResponseHelper.getResponseEntity(HttpStatus.OK, Void.class);
    }

    @PostMapping(path = "/addStock", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<BaseRespDTO<?>> addStock(@Valid @RequestBody BookStockDTO bookStockDTO) {
        warehouseService.addToStock(bookStockDTO);
        return ResponseHelper.getResponseEntity(HttpStatus.OK, Void.class);
    }

    @GetMapping(path = "/getAll")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    ResponseEntity<BaseRespDTO<?>> getBooks() {
        List<BookRespDTO> bookList = BookMapper.toBookRespDTO(bookService.findAll());
        return ResponseHelper.getResponseEntity(HttpStatus.OK, bookList);
    }

    @GetMapping(path = "/getAllBookStock")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    ResponseEntity<BaseRespDTO<?>> getAllBookStock() {
        HashMap<String, Integer> stockMap = warehouseService.findBookStocks();
        return ResponseHelper.getResponseEntity(HttpStatus.OK, stockMap);
    }

}
