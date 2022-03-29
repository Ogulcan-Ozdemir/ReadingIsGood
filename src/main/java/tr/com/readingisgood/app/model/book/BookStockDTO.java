package tr.com.readingisgood.app.model.book;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class BookStockDTO {

    @NotEmpty
    private String name;

    @Min(1)
    @NotNull
    private Integer count;

}
