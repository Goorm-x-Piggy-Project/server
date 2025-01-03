package com.piggymetrics.statistics.client;

import com.piggymetrics.statistics.domain.Currency;
import com.piggymetrics.statistics.domain.ExchangeRatesContainer;
import com.piggymetrics.statistics.domain.ExchangeRatesContainer.ExchangeRate;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "${rates.url}", name = "rates-client", fallback = ExchangeRatesClientFallback.class)
@Primary
public interface ExchangeRatesClient {

    @RequestMapping(method = RequestMethod.GET, value = "/")
    List<ExchangeRate> getRates(
            @RequestParam("authkey") String authKey,
            @RequestParam("searchdate") String searchDate,
            @RequestParam("data") String dataType
    );

}
