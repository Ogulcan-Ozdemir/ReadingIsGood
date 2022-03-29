package tr.com.readingisgood.app.model.order;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
public class BookOrderReqDTO {

    @NotEmpty
    private List<String> bookNames;

    public BookOrderReqDTO() {}

}
