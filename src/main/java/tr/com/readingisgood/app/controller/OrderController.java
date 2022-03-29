package tr.com.readingisgood.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.readingisgood.app.component.RequestHelper;
import tr.com.readingisgood.app.component.ResponseHelper;
import tr.com.readingisgood.app.model.BaseRespDTO;
import tr.com.readingisgood.app.model.order.BookOrder;
import tr.com.readingisgood.app.model.order.BookOrderReqDTO;
import tr.com.readingisgood.app.model.order.BookOrderRespDTO;
import tr.com.readingisgood.app.model.user.User;
import tr.com.readingisgood.app.service.order.BookOrderService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static tr.com.readingisgood.app.component.ResponseHelper.getResponseEntity;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private BookOrderService bookOrderService;

    @Autowired
    private RequestHelper requestHelper;

    @PostMapping(path = "/new", consumes = MediaType.APPLICATION_JSON_VALUE)
//  @PreAuthorize("hasRole('CUSTOMER')")
    ResponseEntity<BaseRespDTO<?>> register(@Valid @RequestBody BookOrderReqDTO bookOrderReqDTO) {
        User user = requestHelper.getRequestUser();
        Long orderId = bookOrderService.orderNew(bookOrderReqDTO, user);
        return ResponseHelper.getResponseEntity(HttpStatus.OK, orderId);
    }

    @GetMapping(path = "/get")
//  @PreAuthorize("hasRole('CUSTOMER')")
    ResponseEntity<BaseRespDTO<?>> get(@Valid @RequestParam("orderId") Long orderId) {
        BookOrder bookOrder = bookOrderService.findOrderById(orderId);
        return ResponseHelper.getResponseEntity(HttpStatus.OK, new BookOrderRespDTO(bookOrder));
    }

    @GetMapping(path = "/getWithInterval")
//  @PreAuthorize("hasRole('CUSTOMER')")
    ResponseEntity<BaseRespDTO<?>> getWithInterval(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                   @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<BookOrder> bookOrders = bookOrderService.findOrdersByInterval(startDate, endDate);
        List<BookOrderRespDTO> respDTO = bookOrders.stream().map(BookOrderRespDTO::new).collect(Collectors.toList());
        return ResponseHelper.getResponseEntity(HttpStatus.OK, respDTO);
    }

}
