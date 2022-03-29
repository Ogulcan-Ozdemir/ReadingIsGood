package tr.com.readingisgood.app.service.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.com.readingisgood.app.model.warehouse.Warehouse;
import tr.com.readingisgood.app.model.book.Book;
import tr.com.readingisgood.app.model.order.BookOrder;
import tr.com.readingisgood.app.model.statistics.MonthlyOrderStatisticRespDTO;
import tr.com.readingisgood.app.repository.StatisticsRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Override
    public HashMap<String, MonthlyOrderStatisticRespDTO> getMonthlyOrder() {
        List<BookOrder> bookOrders = statisticsRepository.findAll();
        HashMap<String, MonthlyOrderStatisticRespDTO> statisticRespDTO = new HashMap<>();
        for(BookOrder bookOrder : bookOrders){
            String currentOrderMonth = bookOrder.getOrderedAt().getMonth().toString();
            Integer totalBookCount = bookOrder.getWarehouseStockEntries().size();
            BigDecimal totalPurchasedAmount = bookOrder.getWarehouseStockEntries().stream()
                    .map(Warehouse::getBook)
                    .map(Book::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if(statisticRespDTO.containsKey(currentOrderMonth)){
                MonthlyOrderStatisticRespDTO respDTO = statisticRespDTO.get(currentOrderMonth);
                respDTO.addTotalOrderCount(1);
                respDTO.addTotalBookCount(totalBookCount);
                respDTO.addTotalPurchasedAmount(totalPurchasedAmount);
                statisticRespDTO.put(currentOrderMonth, respDTO);
            }else{
                statisticRespDTO.put(currentOrderMonth, new MonthlyOrderStatisticRespDTO(1, totalBookCount, totalPurchasedAmount));
            }
        }
        return statisticRespDTO;
    }

}
