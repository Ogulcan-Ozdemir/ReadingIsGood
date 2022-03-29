package tr.com.readingisgood.app.model.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MonthlyOrderStatisticRespDTO {

//    private String monthName;

    private Integer totalOrderCount;

    private Integer totalBookCount;

    private BigDecimal totalPurchasedAmount;

    public void addTotalOrderCount(Integer orderCount) {
        this.totalOrderCount += orderCount;
    }

    public void addTotalBookCount(Integer bookCount) {
        this.totalBookCount += bookCount;
    }

    public void addTotalPurchasedAmount(BigDecimal purchasedAmount) {
        this.totalPurchasedAmount = this.totalPurchasedAmount.add(purchasedAmount);
    }


}
