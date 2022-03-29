package tr.com.readingisgood.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.readingisgood.app.component.ResponseHelper;
import tr.com.readingisgood.app.model.BaseRespDTO;
import tr.com.readingisgood.app.model.statistics.MonthlyOrderStatisticRespDTO;
import tr.com.readingisgood.app.service.statistics.StatisticsService;

import java.util.HashMap;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping(path = "/monthlyOrder")
//    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<BaseRespDTO<?>> getMonthlyOrder() {
        HashMap<String, MonthlyOrderStatisticRespDTO> statisticRespDTO = statisticsService.getMonthlyOrder();
        return ResponseHelper.getResponseEntity(HttpStatus.OK, statisticRespDTO);
    }


}
