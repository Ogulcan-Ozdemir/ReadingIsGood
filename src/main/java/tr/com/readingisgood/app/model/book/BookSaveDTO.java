package tr.com.readingisgood.app.model.book;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BookSaveDTO {

    @NotEmpty
    private String name;

    @NotEmpty
    private String authorName;

    @JsonDeserialize(as = LocalDate.class)
    private LocalDate publishedAt;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @NotEmpty
    private String genre;

}
