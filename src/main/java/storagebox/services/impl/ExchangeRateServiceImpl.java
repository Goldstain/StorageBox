package storagebox.services.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import storagebox.dto.ExchangeRateModel;
import storagebox.services.ExchangeRateService;

import java.util.Arrays;
import java.util.LinkedHashMap;
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
        Map<String, Double> currencyData = new LinkedHashMap<>();
        String[] currencyCodes = {"EUR", "USD", "GBP"};

        ExchangeRateModel[] rates = restTemplate.getForObject(
                apiUrl, ExchangeRateModel[].class);

        for (String currencyCode : currencyCodes) {
            getExchangeRate(currencyCode, rates)
                    .ifPresent(rate -> currencyData.put(currencyCode, rate));
        }

        return currencyData;
    }


    private Optional<Double> getExchangeRate(String currency, ExchangeRateModel[] rates) {
        if (rates != null) {
            return Arrays.stream(rates)
                    .filter(rate -> currency.equalsIgnoreCase(rate.getCc()))
                    .map(rate -> Math.round(rate.getRate() * 100.0) / 100.0)
                    .findFirst();
        }
        return Optional.empty();
    }


}
