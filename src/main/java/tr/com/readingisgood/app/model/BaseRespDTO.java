package tr.com.readingisgood.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseRespDTO<T> {

    private T payload;

    public BaseRespDTO() {}

}
