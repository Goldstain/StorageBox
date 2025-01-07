package storagebox.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ExchangeRateModel {

    private String cc;
    private double rate;
    private String exchangedate;
}
