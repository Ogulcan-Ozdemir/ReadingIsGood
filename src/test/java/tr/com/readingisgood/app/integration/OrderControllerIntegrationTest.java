package tr.com.readingisgood.app.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tr.com.readingisgood.app.ReadingIsGoodApplication;
import tr.com.readingisgood.app.model.BaseRespDTO;
import tr.com.readingisgood.app.model.book.BookStockDTO;
import tr.com.readingisgood.app.model.order.BookOrder;
import tr.com.readingisgood.app.model.order.BookOrderRespDTO;
import tr.com.readingisgood.app.model.user.User;
import tr.com.readingisgood.app.model.user.UserDTO;
import tr.com.readingisgood.app.model.warehouse.Warehouse;
import tr.com.readingisgood.app.misc.IntegrationHelpers;
import tr.com.readingisgood.app.model.book.BookSaveDTO;
import tr.com.readingisgood.app.model.order.BookOrderReqDTO;
import tr.com.readingisgood.app.repository.warehouse.WarehouseRepository;
import tr.com.readingisgood.app.service.order.BookOrderService;
import tr.com.readingisgood.app.service.user.UserService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = ReadingIsGoodApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test-integration.properties")
public class OrderControllerIntegrationTest {

    private static final String ORDER_URL_PREFIX = "/api/order";
    private UserDTO newCustomerDTO;
    private String token;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IntegrationHelpers testHelpers;

    @Autowired
    private UserService userService;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private BookOrderService bookOrderService;

    @Test
    @WithMockUser(username = IntegrationHelpers.testCustomerEmail, authorities = {"ADMIN", "CUSTOMER"})
    public void givenNewOrder_whenBookOrder_thenStatus200() throws Exception {

        BookSaveDTO newBook = testHelpers.addRandomBookThanReturn();
        BookSaveDTO newBook2 = testHelpers.addRandomBookThanReturn();

        BookStockDTO newBookStockDTO = testHelpers.addBookToStockThanReturn(newBook, 1);
        BookStockDTO newBookStockDTO2 = testHelpers.addBookToStockThanReturn(newBook2, 1);

        List<String> bookName = List.of(newBook.getName(), newBook2.getName());
        BookOrderReqDTO bookOrderReqDTO = new BookOrderReqDTO(bookName);

        ResultActions resultAction = mvc.perform(post(ORDER_URL_PREFIX + "/new")
                .content(objectMapper.writeValueAsString(bookOrderReqDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token));

        resultAction.andExpect(status().isOk());

        String contentString = resultAction.andReturn().getResponse().getContentAsString();
        BaseRespDTO<Long> orderIdResponse = objectMapper.readValue(contentString, new TypeReference<>() {});
        Long orderId = orderIdResponse.getPayload();

        BookOrder savedOrder = bookOrderService.findOrderById(orderId);
        assertEquals(orderId, savedOrder.getId());

        List<Long> savedOrderIds = savedOrder.getWarehouseStockEntries().stream().map(Warehouse::getId).collect(Collectors.toList());
        List<Long> savedOrderWarehouseEntryIds = warehouseRepository.findAllById(savedOrderIds).stream().map(Warehouse::getId).collect(Collectors.toList());
        Collections.sort(savedOrderIds);
        Collections.sort(savedOrderWarehouseEntryIds);
        assertEquals(savedOrderWarehouseEntryIds, savedOrderIds);

        User orderedCustomer = userService.findByEmail(newCustomerDTO.getEmail());
        Assertions.assertEquals(orderedCustomer.getId(), savedOrder.getOrderedCustomer().getId());
    }

    @Test
    @WithMockUser(username = IntegrationHelpers.testCustomerEmail, authorities = {"ADMIN", "CUSTOMER"})
    public void givenOrder_whenGetBookOrder_thenStatus200() throws Exception {

        User newCustomer = userService.findByEmail(newCustomerDTO.getEmail());
        BookSaveDTO newBook = testHelpers.addRandomBookThanReturn();
        BookStockDTO newBookStockDTO = testHelpers.addBookToStockThanReturn(newBook, 1);
        List<String> bookName = List.of(newBook.getName());
        BookOrderReqDTO bookOrderReqDTO = new BookOrderReqDTO(bookName);

        Long expectedOrderId = bookOrderService.orderNew(bookOrderReqDTO, newCustomer);

        ResultActions resultAction = mvc.perform(get(ORDER_URL_PREFIX + "/get")
                .queryParam("orderId", String.valueOf(expectedOrderId))
                .header("Authorization", "Bearer " + token));

        resultAction.andExpect(status().isOk());

        String contentString = resultAction.andReturn().getResponse().getContentAsString();
        BaseRespDTO<BookOrderRespDTO> bookOrderResponse = objectMapper.readValue(contentString, new TypeReference<>() {});
        BookOrderRespDTO bookOrder = bookOrderResponse.getPayload();

        assertEquals(expectedOrderId, bookOrder.getOrderId());

        List<Long> savedOrderIds = bookOrder.getWarehouseStockIds();
        List<Long> savedOrderWarehouseEntryIds = warehouseRepository.findAllById(savedOrderIds).stream().map(Warehouse::getId).collect(Collectors.toList());
        assertEquals(savedOrderWarehouseEntryIds, bookOrder.getWarehouseStockIds());

        User orderedCustomer = userService.findByEmail(bookOrder.getOrderedUserEmail());
        assertEquals(newCustomer.getEmail(), orderedCustomer.getEmail());
    }

    @Test
    @WithMockUser(username = IntegrationHelpers.testCustomerEmail, authorities = {"ADMIN", "CUSTOMER"})
    public void givenDates_whenGetWithInterval_thenStatus200() throws Exception {

        User newCustomer = userService.findByEmail(newCustomerDTO.getEmail());

        BookSaveDTO newBook = testHelpers.addRandomBookThanReturn();
        BookStockDTO newBookStockDTO = testHelpers.addBookToStockThanReturn(newBook, 1);
        BookStockDTO newBookStockDTO2 = testHelpers.addBookToStockThanReturn(newBook, 1);
        BookStockDTO newBookStockDTO3 = testHelpers.addBookToStockThanReturn(newBook, 1);
        List<String> bookName = List.of(newBook.getName());

        BookOrderReqDTO bookOrderReqDTO = new BookOrderReqDTO(bookName);
        Long expectedOrderId1 = bookOrderService.orderNew(bookOrderReqDTO, newCustomer);
        Long expectedOrderId2 = bookOrderService.orderNew(bookOrderReqDTO, newCustomer);
        Long expectedOrderId3 = bookOrderService.orderNew(bookOrderReqDTO, newCustomer);

        Long[] expectedOrderIds = {expectedOrderId1, expectedOrderId2, expectedOrderId3};

        ResultActions resultAction = mvc.perform(get(ORDER_URL_PREFIX + "/getWithInterval")
                .queryParam("startDate", LocalDate.now().toString())
                .queryParam("endDate", LocalDate.now().toString())
                .header("Authorization", "Bearer " + token));

        resultAction.andExpect(status().isOk());

        String contentString = resultAction.andReturn().getResponse().getContentAsString();
        BaseRespDTO<List<BookOrderRespDTO>> bookOrderResponse = objectMapper.readValue(contentString, new TypeReference<>() {});
        List<BookOrderRespDTO> bookOrders = bookOrderResponse.getPayload();

        for (Long expectedOrderId : expectedOrderIds) {
            BookOrderRespDTO bookOrder = bookOrders.stream()
                    .filter(bookOrderRespDTO -> bookOrderRespDTO.getOrderId().equals(expectedOrderId))
                    .findFirst().get();

            List<Long> savedOrderWarehouseEntryIds = warehouseRepository.findAllById(bookOrder.getWarehouseStockIds()).stream().map(Warehouse::getId).collect(Collectors.toList());

            User orderedCustomer = userService.findByEmail(bookOrder.getOrderedUserEmail());

            assertEquals(expectedOrderId, bookOrder.getOrderId());
            assertEquals(savedOrderWarehouseEntryIds, bookOrder.getWarehouseStockIds());
            assertEquals(newCustomer.getEmail(), orderedCustomer.getEmail());
        }

    }

    @BeforeAll
    public void setUp() {
        testHelpers.populateGenres();
        newCustomerDTO = testHelpers.registerTestCustomerAndReturn();
        token = userService.login(newCustomerDTO);
    }

}
