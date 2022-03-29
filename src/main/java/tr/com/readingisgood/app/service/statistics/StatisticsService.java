package tr.com.readingisgood.app.service.statistics;

import tr.com.readingisgood.app.model.statistics.MonthlyOrderStatisticRespDTO;

import java.util.HashMap;

public interface StatisticsService {

    HashMap<String, MonthlyOrderStatisticRespDTO> getMonthlyOrder();
}
