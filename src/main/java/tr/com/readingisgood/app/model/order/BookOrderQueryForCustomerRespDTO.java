package tr.com.readingisgood.app.model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import tr.com.readingisgood.app.model.warehouse.Warehouse;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class BookOrderQueryForCustomerRespDTO {

    private Long orderId;

    private List<Long> warehouseStockIds;

    private String orderedAt;

    public BookOrderQueryForCustomerRespDTO() {}

    public BookOrderQueryForCustomerRespDTO(BookOrder bookOrder) {
        this.orderId = bookOrder.getId();
        this.warehouseStockIds = bookOrder.getWarehouseStockEntries().stream().map(Warehouse::getId).collect(Collectors.toList());
        this.orderedAt = bookOrder.getOrderedAt().toString();
    }


}
