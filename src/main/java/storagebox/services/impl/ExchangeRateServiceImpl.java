package storagebox.services.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import storagebox.dto.ExchangeRateModel;
import storagebox.services.ExchangeRateService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final RestTemplate restTemplate;

    @Value("${nbu.api.url}")
    private String apiUrl;

    @Override
    public Map<String, Double> getExchangeRateMap() {
        Map<String, Double> currencyData = new HashMap<>();

        getExchangeRate("USD")
                .ifPresent(rate -> currencyData.put("USD", rate));

        getExchangeRate("EUR")
                .ifPresent(rate -> currencyData.put("EUR", rate));

        return currencyData;
    }


    private Optional<Double> getExchangeRate(String currency) {
        ExchangeRateModel[] rates = restTemplate.getForObject(
                apiUrl, ExchangeRateModel[].class);

        if (rates != null) {
            return Arrays.stream(rates)
                    .filter(rate -> currency.equalsIgnoreCase(rate.getCc()))
                    .map(rate -> Math.round(rate.getRate() * 100.0) / 100.0)
                    .findFirst();
        }
        return Optional.empty();
    }


}
