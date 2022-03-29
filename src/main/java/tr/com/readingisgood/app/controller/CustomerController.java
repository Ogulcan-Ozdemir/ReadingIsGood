package tr.com.readingisgood.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.readingisgood.app.component.RequestHelper;
import tr.com.readingisgood.app.component.ResponseHelper;
import tr.com.readingisgood.app.model.BaseRespDTO;
import tr.com.readingisgood.app.model.order.BookOrderQueryForCustomerRespDTO;
import tr.com.readingisgood.app.model.user.UserDTO;
import tr.com.readingisgood.app.model.order.BookOrder;
import tr.com.readingisgood.app.model.security.GrantedAuthRoles;
import tr.com.readingisgood.app.model.user.User;
import tr.com.readingisgood.app.service.order.BookOrderService;
import tr.com.readingisgood.app.service.user.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static tr.com.readingisgood.app.component.ResponseHelper.getResponseEntity;

@RestController
@RequestMapping
public class CustomerController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookOrderService bookOrderService;

    @Autowired
    private RequestHelper requestHelper;

    @PostMapping(path = "/auth/user/customer/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BaseRespDTO<?>> register(@Valid @RequestBody UserDTO userDTO) {
        userService.register(userDTO, List.of(GrantedAuthRoles.CUSTOMER));
        return ResponseHelper.getResponseEntity(HttpStatus.OK, Void.class);
    }

    @GetMapping(path = "/api/customer/getOrders")
    ResponseEntity<BaseRespDTO<?>> getBooks() {
        User user = requestHelper.getRequestUser();
        List<BookOrder> bookOrders = bookOrderService.findOrderByCustomerId(user.getId());
        List<BookOrderQueryForCustomerRespDTO> respDTO = bookOrders.stream().map(BookOrderQueryForCustomerRespDTO::new).collect(Collectors.toList());
        return ResponseHelper.getResponseEntity(HttpStatus.OK, respDTO);
    }

}
