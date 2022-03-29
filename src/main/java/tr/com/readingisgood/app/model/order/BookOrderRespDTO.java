package tr.com.readingisgood.app.model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import tr.com.readingisgood.app.model.warehouse.Warehouse;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class BookOrderRespDTO {

    private Long orderId;

    private List<Long> warehouseStockIds;

    private String orderedUserEmail;

    private String orderedAt;

    public BookOrderRespDTO() {}

    public BookOrderRespDTO(BookOrder bookOrder) {
        this.orderId = bookOrder.getId();
        this.warehouseStockIds = bookOrder.getWarehouseStockEntries().stream().map(Warehouse::getId).collect(Collectors.toList());
        this.orderedUserEmail = bookOrder.getOrderedCustomer().getEmail();
        this.orderedAt = bookOrder.getOrderedAt().toString();
    }


}
