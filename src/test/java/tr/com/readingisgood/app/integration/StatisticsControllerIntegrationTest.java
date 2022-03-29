package tr.com.readingisgood.app.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tr.com.readingisgood.app.ReadingIsGoodApplication;
import tr.com.readingisgood.app.model.BaseRespDTO;
import tr.com.readingisgood.app.model.order.BookOrder;
import tr.com.readingisgood.app.model.user.UserDTO;
import tr.com.readingisgood.app.model.warehouse.Warehouse;
import tr.com.readingisgood.app.service.statistics.StatisticsService;
import tr.com.readingisgood.app.misc.IntegrationHelpers;
import tr.com.readingisgood.app.model.book.Book;
import tr.com.readingisgood.app.model.statistics.MonthlyOrderStatisticRespDTO;
import tr.com.readingisgood.app.service.user.UserService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = ReadingIsGoodApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test-integration.properties")
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class StatisticsControllerIntegrationTest {

    private static final String STATISTICS_URL_PREFIX = "/api/statistics";

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
    private StatisticsService statisticsService;

    private final OrderStatisticConverter statisticConverter = new OrderStatisticConverter();

    @Test
    @WithMockUser(username = IntegrationHelpers.testCustomerEmail, authorities = {"ADMIN", "CUSTOMER"})
    public void givenOrders_whenGetMonthlyOrder_thenStatus200() throws Exception {

        IntStream.range(0, 5).forEach(index -> testHelpers.addOrderThanReturn());

        ResultActions resultAction = mvc.perform(get(STATISTICS_URL_PREFIX + "/monthlyOrder")
                .header("Authorization", "Bearer " + token));

        resultAction.andExpect(status().isOk());

        String responseContent = resultAction.andReturn().getResponse().getContentAsString();
        BaseRespDTO<HashMap<String, MonthlyOrderStatisticRespDTO>> statisticResp = objectMapper.readValue(responseContent, new TypeReference<>() {});
        HashMap<String, MonthlyOrderStatisticRespDTO> statisticRespDTO = statisticResp.getPayload();

        OrderStatistic actualOrderStatistic = statisticConverter.fromMonthlyOrderStatistic(statisticRespDTO);

        HashMap<String, MonthlyOrderStatisticRespDTO> expectedBookOrders = statisticsService.getMonthlyOrder();
        OrderStatistic expectedOrderStatistic = statisticConverter.fromMonthlyOrderStatistic(expectedBookOrders);

        assertEquals(expectedOrderStatistic, actualOrderStatistic);
    }

    @BeforeAll
    public void setUp() {
        testHelpers.populateGenres();
        newCustomerDTO = testHelpers.registerTestCustomerAndReturn();
        token = testHelpers.loginTestCustomer(newCustomerDTO);
    }

    class OrderStatisticConverter {

        public OrderStatistic fromBookOrderList(List<BookOrder> bookOrders) {
            Integer totalOrderCount = 0;
            Integer totalBookCount = 0;
            BigDecimal totalPurchasedAmount = BigDecimal.ZERO;

            for (BookOrder bookOrder : bookOrders) {
                totalBookCount += bookOrder.getWarehouseStockEntries().size();
                BigDecimal purchasedAmountOfBooks = bookOrder.getWarehouseStockEntries().stream()
                        .map(Warehouse::getBook)
                        .map(Book::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                totalPurchasedAmount = totalPurchasedAmount.add(purchasedAmountOfBooks);
            }

            totalOrderCount += bookOrders.size();

            return new OrderStatistic(totalOrderCount, totalBookCount, totalPurchasedAmount);
        }

        public OrderStatistic fromMonthlyOrderStatistic(HashMap<String, MonthlyOrderStatisticRespDTO> statisticRespDTO) {
            Integer totalOrderCount = 0;
            Integer totalBookCount = 0;
            BigDecimal totalPurchasedAmount = BigDecimal.ZERO;

            for (Map.Entry<String, MonthlyOrderStatisticRespDTO> stat : statisticRespDTO.entrySet()) {
                MonthlyOrderStatisticRespDTO statValue = stat.getValue();
                totalOrderCount += statValue.getTotalOrderCount();
                totalBookCount += statValue.getTotalBookCount();
                totalPurchasedAmount = totalPurchasedAmount.add(statValue.getTotalPurchasedAmount());
            }

            return new OrderStatistic(totalOrderCount, totalBookCount, totalPurchasedAmount);
        }

    }

    @Data
    @AllArgsConstructor
    class OrderStatistic {
        private Integer totalOrderCount;
        private Integer totalBookCount;
        private BigDecimal totalPurchasedAmount;
    }

}

