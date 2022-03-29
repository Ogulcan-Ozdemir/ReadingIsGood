package tr.com.readingisgood.app.model.order;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BookOrderIntervalReqDTO {

    @JsonDeserialize(as = LocalDate.class)
    private LocalDate startDate;

    @JsonDeserialize(as = LocalDate.class)
    private LocalDate endDate;

}
